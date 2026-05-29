package com.livid.creategravlax.block.entity;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.item.ItemHelper;

import com.livid.creategravlax.registry.ModBlockEntities;
import com.livid.creategravlax.registry.ModFluids;
import com.livid.creategravlax.registry.ModItems;
import com.livid.creategravlax.registry.ModSoundEvents;
import com.livid.creategravlax.recipe.PotionEssenceResolver;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;


public final class CentrifugeBlockEntity extends KineticBlockEntity {
    public static final int TANK_CAPACITY = 1000;
    public static final int INPUT_PER_BATCH = 1000;
    public static final int MOLASSES_PER_BATCH = 400;
    public static final int POTION_WASTE_WATER_PER_BATCH = 500;
    public static final int SACCHARATE_WASTE_WATER_PER_BATCH = 50;
    public static final int ESSENCES_PER_POTION_BATCH = 6;
    public static final int BASE_PROCESSING_TICKS = 256;
    public static final float MINIMUM_OPERATIONAL_RPM = 128.0f;
    private static final int RUNNING_SOUND_PERIOD_TICKS = 9;

    private enum Process {
        NONE, THICK_WHITE_JUICE, THICK_RED_JUICE, CALCIUM_SACCHARATE, CONCENTRATED_POTION
    }

    private final FluidTank inputTank = new FluidTank(TANK_CAPACITY, this::isValidInputFluid) {
        @Override
        protected void onContentsChanged() {
            contentsChanged();
        }
    };

    private final FluidTank outputTank = new FluidTank(TANK_CAPACITY, this::isValidOutputFluid) {
        @Override
        protected void onContentsChanged() {
            contentsChanged();
        }
    };

    private final ItemStackHandler inputInventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            contentsChanged();
        }
    };

    private final ItemStackHandler outputInventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            contentsChanged();
        }
    };

    private FilteringBehaviour filtering;
    private int processingTicks;
    private boolean activelySeparating;
    private boolean operationSoundActive;
    private int runningSoundCooldown;

    private final IFluidHandler sideFluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 2;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return tank == 0 ? inputTank.getFluidInTank(0) : outputTank.getFluidInTank(0);
        }

        @Override
        public int getTankCapacity(int tank) {
            return TANK_CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return tank == 0 && isValidInputFluid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return inputTank.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return outputTank.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return outputTank.drain(maxDrain, action);
        }
    };

    private final IItemHandler topItemHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return inputInventory.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inputInventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return inputInventory.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return inputInventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return inputInventory.isItemValid(slot, stack);
        }
    };

    private final IItemHandler sideOutputHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return outputInventory.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return outputInventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return outputInventory.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return outputInventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    };

    public CentrifugeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENTRIFUGE.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        filtering = new FilteringBehaviour(this, new CentrifugeFilterSlot()).forRecipes();
        behaviours.add(filtering);
    }

    public IFluidHandler getSideFluidHandler() {
        return sideFluidHandler;
    }

    public IItemHandler getTopItemHandler() {
        return topItemHandler;
    }

    public IItemHandler getSideOutputHandler() {
        return sideOutputHandler;
    }

    public boolean hasSolidContents() {
        return hasItems(outputInventory) || hasItems(inputInventory);
    }

    public void returnAllSolidContents(Player player) {
        giveAllItemsToPlayer(outputInventory, player);
        giveAllItemsToPlayer(inputInventory, player);
        processingTicks = 0;
        setActivelySeparating(false);
        stopOperationSound();
        contentsChanged();
    }

    private boolean hasItems(ItemStackHandler inventory) {
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void giveAllItemsToPlayer(ItemStackHandler inventory, Player player) {
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack extracted = inventory.extractItem(slot, Integer.MAX_VALUE, false);
            if (!extracted.isEmpty()) {
                ItemHandlerHelper.giveItemToPlayer(player, extracted);
            }
        }
    }

    public FluidStack getRenderedInputFluid() {
        return inputTank.getFluid().copy();
    }

    public FluidStack getRenderedOutputFluid() {
        return outputTank.getFluid().copy();
    }

    public ItemStackHandler getRenderedInputItems() {
        return inputInventory;
    }

    public ItemStackHandler getRenderedOutputItems() {
        return outputInventory;
    }

    public boolean isActivelySeparating() {
        return activelySeparating;
    }

    private void setActivelySeparating(boolean active) {
        if (activelySeparating == active) {
            return;
        }
        activelySeparating = active;
        setChanged();
        if (level != null && !level.isClientSide) {
            sendData();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) {
            return;
        }

        if (Math.abs(getSpeed()) < MINIMUM_OPERATIONAL_RPM) {
            setActivelySeparating(false);
            stopOperationSound();
            resetProgress();
            return;
        }

        Process process = selectProcess();
        if (process == Process.NONE || !hasOutputRoom(process)) {
            setActivelySeparating(false);
            stopOperationSound();
            resetProgress();
            return;
        }

        setActivelySeparating(true);
        tickOperationSound();
        processingTicks += getProcessingSpeed();

        if (level instanceof ServerLevel serverLevel) {
            spawnSeparationSpiral(serverLevel, process);
        }

        if (processingTicks < BASE_PROCESSING_TICKS) {
            setChanged();
            return;
        }

        consumeInputs(process);
        createOutputs(process);
        processingTicks = 0;
        contentsChanged();

        Process following = selectProcess();
        boolean continues = following != Process.NONE && hasOutputRoom(following);
        if (!continues) {
            setActivelySeparating(false);
            stopOperationSound();
        }
    }

    private Process selectProcess() {
        FluidStack input = inputTank.getFluid();
        if (input.getAmount() >= INPUT_PER_BATCH) {
            Fluid still = FluidHelper.convertToStill(input.getFluid());
            if (still == FluidHelper.convertToStill(ModFluids.THICK_SUGAR_BEET_JUICE.get())
                && filterAllows(List.of(new ItemStack(ModItems.RAW_SUGAR.get())))) {
                return Process.THICK_WHITE_JUICE;
            }
            if (still == FluidHelper.convertToStill(ModFluids.THICK_RED_BEET_JUICE.get())
                && filterAllows(List.of(new ItemStack(ModItems.RED_RAW_SUGAR.get())))) {
                return Process.THICK_RED_JUICE;
            }
            if (still == FluidHelper.convertToStill(ModFluids.CALCIUM_SACCHARATE.get())
                && filterAllows(List.of(new ItemStack(ModItems.CALCIUM_SACCHARATE_PASTE.get())))) {
                return Process.CALCIUM_SACCHARATE;
            }
            if (still == ModFluids.CONCENTRATED_POTION.get().getSource()) {
                ItemStack essence = PotionEssenceResolver.outputFor(input, ESSENCES_PER_POTION_BATCH);
                if (!essence.isEmpty() && filterAllows(List.of(essence))) {
                    return Process.CONCENTRATED_POTION;
                }
            }
        }

        return Process.NONE;
    }

    private boolean filterAllows(List<ItemStack> outputs) {
        if (filtering == null) {
            return true;
        }
        for (ItemStack output : outputs) {
            if (filtering.test(output)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOutputRoom(Process process) {
        FluidStack fluidOutput = fluidOutputFor(process);
        if (!fluidOutput.isEmpty()
            && outputTank.fill(fluidOutput, FluidAction.SIMULATE) != fluidOutput.getAmount()) {
            return false;
        }

        ItemStackHandler simulated = new ItemStackHandler(outputInventory.getSlots());
        for (int slot = 0; slot < outputInventory.getSlots(); slot++) {
            simulated.setStackInSlot(slot, outputInventory.getStackInSlot(slot).copy());
        }
        for (ItemStack output : itemOutputsFor(process)) {
            if (!ItemHandlerHelper.insertItemStacked(simulated, output.copy(), false).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private List<ItemStack> itemOutputsFor(Process process) {
        return switch (process) {
            case THICK_WHITE_JUICE -> List.of(new ItemStack(ModItems.RAW_SUGAR.get(), 10));
            case THICK_RED_JUICE -> List.of(new ItemStack(ModItems.RED_RAW_SUGAR.get(), 10));
            case CALCIUM_SACCHARATE -> List.of(
                new ItemStack(ModItems.CALCIUM_SACCHARATE_PASTE.get(), 10)
            );
            case CONCENTRATED_POTION -> {
                ItemStack essence = PotionEssenceResolver.outputFor(inputTank.getFluid(), ESSENCES_PER_POTION_BATCH);
                yield essence.isEmpty() ? List.of() : List.of(essence);
            }
            default -> List.of();
        };
    }

    private FluidStack fluidOutputFor(Process process) {
        if (process == Process.THICK_WHITE_JUICE || process == Process.THICK_RED_JUICE) {
            return new FluidStack(FluidHelper.convertToStill(ModFluids.MOLASSES.get()), MOLASSES_PER_BATCH);
        }
        if (process == Process.CONCENTRATED_POTION) {
            return new FluidStack(FluidHelper.convertToStill(ModFluids.WASTE_WATER.get()), POTION_WASTE_WATER_PER_BATCH);
        }
        if (process == Process.CALCIUM_SACCHARATE) {
            return new FluidStack(FluidHelper.convertToStill(ModFluids.WASTE_WATER.get()), SACCHARATE_WASTE_WATER_PER_BATCH);
        }
        return FluidStack.EMPTY;
    }

    private void consumeInputs(Process process) {
        if (process == Process.THICK_WHITE_JUICE || process == Process.THICK_RED_JUICE
            || process == Process.CALCIUM_SACCHARATE || process == Process.CONCENTRATED_POTION) {
            inputTank.drain(INPUT_PER_BATCH, FluidAction.EXECUTE);
        }
    }

    private void createOutputs(Process process) {
        FluidStack fluidOutput = fluidOutputFor(process);
        if (!fluidOutput.isEmpty()) {
            outputTank.fill(fluidOutput, FluidAction.EXECUTE);
        }
        for (ItemStack itemOutput : itemOutputsFor(process)) {
            ItemHandlerHelper.insertItemStacked(outputInventory, itemOutput.copy(), false);
        }
    }

    private int findInputSlot(net.minecraft.world.item.Item item) {
        for (int slot = 0; slot < inputInventory.getSlots(); slot++) {
            if (inputInventory.getStackInSlot(slot).is(item)) {
                return slot;
            }
        }
        return -1;
    }

    private int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 16);
    }

    private boolean isValidInputFluid(FluidStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Fluid still = FluidHelper.convertToStill(stack.getFluid());
        return still == FluidHelper.convertToStill(ModFluids.THICK_SUGAR_BEET_JUICE.get())
            || still == FluidHelper.convertToStill(ModFluids.THICK_RED_BEET_JUICE.get())
            || still == FluidHelper.convertToStill(ModFluids.CALCIUM_SACCHARATE.get())
            || still == ModFluids.CONCENTRATED_POTION.get().getSource();
    }

    private boolean isValidOutputFluid(FluidStack stack) {
        return !stack.isEmpty()
            && (FluidHelper.convertToStill(stack.getFluid()) == FluidHelper.convertToStill(ModFluids.MOLASSES.get())
                || FluidHelper.convertToStill(stack.getFluid()) == FluidHelper.convertToStill(ModFluids.WASTE_WATER.get()));
    }

    private void spawnSeparationSpiral(ServerLevel serverLevel, Process process) {
        float red;
        float green;
        float blue;
        switch (process) {
            case THICK_RED_JUICE -> {
                red = .50f;
                green = .08f;
                blue = .20f;
            }
            case THICK_WHITE_JUICE -> {
                red = .82f;
                green = .52f;
                blue = .15f;
            }
            case CALCIUM_SACCHARATE -> {
                red = .86f;
                green = .77f;
                blue = .48f;
            }
            case CONCENTRATED_POTION -> {
                int colour = inputTank.getFluid().getOrDefault(net.minecraft.core.component.DataComponents.POTION_CONTENTS,
                    net.minecraft.world.item.alchemy.PotionContents.EMPTY).getColor();
                red = ((colour >> 16) & 255) / 255f;
                green = ((colour >> 8) & 255) / 255f;
                blue = (colour & 255) / 255f;
            }
            default -> {
                red = .70f;
                green = .70f;
                blue = .70f;
            }
        }

        ColorParticleOption swirl = ColorParticleOption.create(
            ParticleTypes.ENTITY_EFFECT, red, green, blue
        );

        double time = serverLevel.getGameTime() * .42;
        double radius = .23;
        double centreX = worldPosition.getX() + .5;
        double centreZ = worldPosition.getZ() + .5;
        double baseY = worldPosition.getY() + 1.03;

        for (int i = 0; i < 2; i++) {
            double angle = time + i * Math.PI;
            double x = centreX + Math.cos(angle) * radius;
            double z = centreZ + Math.sin(angle) * radius;
            double y = baseY + i * .06 + Math.sin(time * .65 + i) * .025;
            serverLevel.sendParticles(swirl, x, y, z, 1, 0, 0, 0, .012);
        }
    }

    private void tickOperationSound() {
        if (level == null || level.isClientSide) {
            return;
        }

        if (!operationSoundActive) {
            operationSoundActive = true;
            runningSoundCooldown = 5;
            level.playSound(null, worldPosition, ModSoundEvents.CENTRIFUGE_START.get(),
                SoundSource.BLOCKS, .42f, 1.0f);
            return;
        }

        if (runningSoundCooldown-- <= 0) {
            level.playSound(null, worldPosition, ModSoundEvents.CENTRIFUGE_RUNNING.get(),
                SoundSource.BLOCKS, .34f, 1.0f);
            runningSoundCooldown = RUNNING_SOUND_PERIOD_TICKS;
        }
    }

    private void stopOperationSound() {
        if (!operationSoundActive || level == null || level.isClientSide) {
            return;
        }
        operationSoundActive = false;
        runningSoundCooldown = 0;
        level.playSound(null, worldPosition, ModSoundEvents.CENTRIFUGE_STOP.get(),
            SoundSource.BLOCKS, .42f, 1.0f);
    }

    private void contentsChanged() {
        setChanged();
        if (level != null && !level.isClientSide) {
            sendData();
        }
    }

    private void resetProgress() {
        if (processingTicks != 0) {
            processingTicks = 0;
            contentsChanged();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (level != null) {
            ItemHelper.dropContents(level, worldPosition, inputInventory);
            ItemHelper.dropContents(level, worldPosition, outputInventory);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.translatable("create_gravlax.tooltip.centrifuge_contents")
            .withStyle(ChatFormatting.GRAY));

        boolean empty = true;
        if (!inputTank.getFluid().isEmpty()) {
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.input").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": "))
                .append(Component.translatable(inputTank.getFluid().getDescriptionId()).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" " + inputTank.getFluidAmount() + " mB").withStyle(ChatFormatting.AQUA)));
            empty = false;
        }
        if (!outputTank.getFluid().isEmpty()) {
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.output").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(": "))
                .append(Component.translatable(outputTank.getFluid().getDescriptionId()).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" " + outputTank.getFluidAmount() + " mB").withStyle(ChatFormatting.AQUA)));
            empty = false;
        }

        empty = appendItemsToTooltip(tooltip, inputInventory, empty);
        empty = appendItemsToTooltip(tooltip, outputInventory, empty);

        if (empty) {
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.empty")
                    .withStyle(ChatFormatting.DARK_GRAY)));
        }

        if (getSpeed() == 0) {
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.centrifuge_no_rotation")
                    .withStyle(ChatFormatting.DARK_RED)));
        } else if (Math.abs(getSpeed()) < MINIMUM_OPERATIONAL_RPM) {
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.centrifuge_too_slow",
                    (int) MINIMUM_OPERATIONAL_RPM).withStyle(ChatFormatting.GOLD)));
        } else if (activelySeparating && processingTicks > 0) {
            int percent = Math.min(100, processingTicks * 100 / BASE_PROCESSING_TICKS);
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.processing",
                    percent).withStyle(ChatFormatting.GOLD)));
        } else {
            tooltip.add(Component.literal("  ")
                .append(Component.translatable("create_gravlax.tooltip.centrifuge_ready_idle")
                    .withStyle(ChatFormatting.DARK_GRAY)));
        }
        return true;
    }

    private boolean appendItemsToTooltip(List<Component> tooltip, ItemStackHandler handler, boolean empty) {
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            tooltip.add(Component.literal("  ")
                .append(stack.getHoverName().copy().withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" x" + stack.getCount()).withStyle(ChatFormatting.GREEN)));
            empty = false;
        }
        return empty;
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        tag.put("CentrifugeInputTank", inputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("CentrifugeOutputTank", outputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("CentrifugeInputInventory", inputInventory.serializeNBT(registries));
        tag.put("CentrifugeOutputInventory", outputInventory.serializeNBT(registries));
        tag.putInt("CentrifugeProgress", processingTicks);
        tag.putBoolean("CentrifugeActivelySeparating", activelySeparating);
        super.write(tag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        inputTank.readFromNBT(registries, tag.getCompound("CentrifugeInputTank"));
        outputTank.readFromNBT(registries, tag.getCompound("CentrifugeOutputTank"));
        inputInventory.deserializeNBT(registries, tag.getCompound("CentrifugeInputInventory"));
        outputInventory.deserializeNBT(registries, tag.getCompound("CentrifugeOutputInventory"));
        processingTicks = tag.getInt("CentrifugeProgress");
        activelySeparating = tag.getBoolean("CentrifugeActivelySeparating");
        super.read(tag, registries, clientPacket);
    }
}
