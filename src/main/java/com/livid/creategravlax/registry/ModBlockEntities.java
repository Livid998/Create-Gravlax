package com.livid.creategravlax.registry;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.block.entity.CentrifugeBlockEntity;
import com.livid.creategravlax.block.entity.EvaporatorBlockEntity;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateGravlax.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EvaporatorBlockEntity>> EVAPORATOR =
        BLOCK_ENTITY_TYPES.register(
            "evaporator",
            () -> BlockEntityType.Builder.of(EvaporatorBlockEntity::new, ModBlocks.EVAPORATOR.get()).build(null)
        );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE =
        BLOCK_ENTITY_TYPES.register(
            "centrifuge",
            () -> BlockEntityType.Builder.of(CentrifugeBlockEntity::new, ModBlocks.CENTRIFUGE.get()).build(null)
        );

    private ModBlockEntities() {
    }

    public static void register(IEventBus modBus) {
        BLOCK_ENTITY_TYPES.register(modBus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            EVAPORATOR.get(),
            (blockEntity, direction) -> blockEntity.getEvaporatorFluidCapability()
        );
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            EVAPORATOR.get(),
            (blockEntity, direction) -> blockEntity.getEvaporatorItemCapability()
        );

        event.registerBlockEntity(
            Capabilities.FluidHandler.BLOCK,
            CENTRIFUGE.get(),
            (blockEntity, direction) -> direction != null && direction.getAxis().isHorizontal()
                ? blockEntity.getSideFluidHandler()
                : null
        );
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            CENTRIFUGE.get(),
            (blockEntity, direction) -> {
                if (direction == Direction.UP) {
                    return blockEntity.getTopItemHandler();
                }
                if (direction != null && direction.getAxis().isHorizontal()) {
                    return blockEntity.getSideOutputHandler();
                }
                return null;
            }
        );
    }
}
