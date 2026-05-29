package com.livid.creategravlax.block.entity;

import java.util.Collections;
import java.util.List;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.lang.LangBuilder;

import com.livid.creategravlax.registry.ModBlockEntities;
import com.livid.creategravlax.registry.ModFluids;
import com.livid.creategravlax.registry.ModItems;
import com.livid.creategravlax.fluid.ConcentratedPotionFluid;
import com.livid.creategravlax.recipe.PotionEssenceResolver;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.items.IItemHandler;


public final class EvaporatorBlockEntity extends BasinBlockEntity {
    public static final int JUICE_INPUT_PER_BATCH = 1000;
    public static final int JUICE_OUTPUT_PER_BATCH = 500;
    public static final int SALT_WATER_INPUT_PER_BATCH = 1000;
    public static final int POTION_INPUT_PER_BATCH = 1000;
    public static final int CONCENTRATED_POTION_OUTPUT_PER_BATCH = 500;

    
    public static final int NORMAL_PROCESSING_TICKS = 80;

    private int processingTicks;

    public EvaporatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EVAPORATOR.get(), pos, state);
    }

    public IFluidHandler getEvaporatorFluidCapability() {
        return fluidCapability;
    }

    public IItemHandler getEvaporatorItemCapability() {
        return itemCapability;
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) {
            return;
        }

        FluidStack input = findProcessableInput();
        HeatLevel heat = BlazeBurnerBlock.getHeatLevelOf(level.getBlockState(worldPosition.below()));

        if (!heat.isAtLeast(HeatLevel.KINDLED) || input.isEmpty()) {
            resetProgress();
            return;
        }

        List<ItemStack> itemOutputs = itemOutputsFor(input);
        List<FluidStack> fluidOutputs = fluidOutputsFor(input);
        int requiredInput = requiredInputFor(input);

        if (requiredInput <= 0 || input.getAmount() < requiredInput) {
            resetProgress();
            return;
        }

        if (!passesRecipeFilter(itemOutputs, fluidOutputs)) {
            resetProgress();
            return;
        }

        if (!acceptOutputs(itemOutputs, fluidOutputs, true)) {
            resetProgress();
            return;
        }

        processingTicks += heat == HeatLevel.SEETHING ? 2 : 1;

        if (processingTicks % 16 == 0 && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                ParticleTypes.CLOUD,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 1.05,
                worldPosition.getZ() + 0.5,
                2,
                0.18, 0.02, 0.18,
                0.01
            );
        }

        if (processingTicks < NORMAL_PROCESSING_TICKS) {
            setChanged();
            return;
        }

        inputTank.getCapability().drain(
            input.copyWithAmount(requiredInput),
            FluidAction.EXECUTE
        );
        boolean isPlainWaterTraceSalt = FluidHelper.convertToStill(input.getFluid()) == Fluids.WATER;
        if (!isPlainWaterTraceSalt || level.random.nextFloat() < 0.20f) {
            acceptOutputs(itemOutputs, fluidOutputs, false);
        }

        processingTicks = 0;
        notifyChangeOfContents();
        notifyUpdate();
    }

    private FluidStack findProcessableInput() {
        IFluidHandler inputHandler = inputTank.getCapability();
        for (int tank = 0; tank < inputHandler.getTanks(); tank++) {
            FluidStack stack = inputHandler.getFluidInTank(tank);
            if (!stack.isEmpty() && requiredInputFor(stack) > 0) {
                return stack.copy();
            }
        }
        return FluidStack.EMPTY;
    }

    private int requiredInputFor(FluidStack input) {
        if (input.isEmpty()) {
            return 0;
        }

        Fluid stillFluid = FluidHelper.convertToStill(input.getFluid());
        if (stillFluid == Fluids.WATER
            || stillFluid == FluidHelper.convertToStill(ModFluids.SALT_WATER.get())) {
            return SALT_WATER_INPUT_PER_BATCH;
        }
        if (stillFluid == FluidHelper.convertToStill(ModFluids.SUGAR_BEET_JUICE.get())
            || stillFluid == FluidHelper.convertToStill(ModFluids.RED_BEET_JUICE.get())) {
            return JUICE_INPUT_PER_BATCH;
        }
        if (stillFluid == AllFluids.POTION.get().getSource() && PotionEssenceResolver.hasSupportedEssence(input)) {
            return POTION_INPUT_PER_BATCH;
        }
        return 0;
    }

    private List<FluidStack> fluidOutputsFor(FluidStack input) {
        if (input.isEmpty()) {
            return Collections.emptyList();
        }

        Fluid stillFluid = FluidHelper.convertToStill(input.getFluid());
        if (stillFluid == FluidHelper.convertToStill(ModFluids.SUGAR_BEET_JUICE.get())) {
            return List.of(new FluidStack(
                FluidHelper.convertToStill(ModFluids.THICK_SUGAR_BEET_JUICE.get()),
                JUICE_OUTPUT_PER_BATCH
            ));
        }
        if (stillFluid == FluidHelper.convertToStill(ModFluids.RED_BEET_JUICE.get())) {
            return List.of(new FluidStack(
                FluidHelper.convertToStill(ModFluids.THICK_RED_BEET_JUICE.get()),
                JUICE_OUTPUT_PER_BATCH
            ));
        }
        if (stillFluid == AllFluids.POTION.get().getSource() && PotionEssenceResolver.hasSupportedEssence(input)) {
            return List.of(ConcentratedPotionFluid.concentrate(input, CONCENTRATED_POTION_OUTPUT_PER_BATCH));
        }
        return Collections.emptyList();
    }

    private List<ItemStack> itemOutputsFor(FluidStack input) {
        if (!input.isEmpty()) {
            Fluid stillFluid = FluidHelper.convertToStill(input.getFluid());
            if (stillFluid == Fluids.WATER
                || stillFluid == FluidHelper.convertToStill(ModFluids.SALT_WATER.get())) {
                return List.of(new ItemStack(ModItems.SALT.get()));
            }
        }
        return Collections.emptyList();
    }

    private boolean passesRecipeFilter(List<ItemStack> items, List<FluidStack> fluids) {
        if (!items.isEmpty()) {
            return getFilter().test(items.get(0));
        }
        if (!fluids.isEmpty()) {
            return getFilter().test(fluids.get(0));
        }
        return false;
    }

    


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.translatable("create_gravlax.tooltip.evaporator_contents")
            .withStyle(ChatFormatting.GRAY));

        boolean empty = true;

        if (itemCapability != null) {
            for (int slot = 0; slot < itemCapability.getSlots(); slot++) {
                ItemStack stack = itemCapability.getStackInSlot(slot);
                if (stack.isEmpty()) {
                    continue;
                }
                CreateLang.text("")
                    .add(Component.translatable(stack.getDescriptionId()).withStyle(ChatFormatting.GRAY))
                    .add(CreateLang.text(" x" + stack.getCount()).style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
                empty = false;
            }
        }

        if (fluidCapability != null) {
            LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
            for (int tank = 0; tank < fluidCapability.getTanks(); tank++) {
                FluidStack fluidStack = fluidCapability.getFluidInTank(tank);
                if (fluidStack.isEmpty()) {
                    continue;
                }
                CreateLang.text("")
                    .add(CreateLang.fluidName(fluidStack)
                        .add(CreateLang.text(" "))
                        .style(ChatFormatting.GRAY)
                        .add(CreateLang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
                empty = false;
            }
        }

        if (empty) {
            tooltip.add(Component.translatable("create_gravlax.tooltip.empty")
                .withStyle(ChatFormatting.DARK_GRAY));
        }

        return true;
    }

    private void resetProgress() {
        if (processingTicks != 0) {
            processingTicks = 0;
            setChanged();
            sendData();
        }
    }
}
