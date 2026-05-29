package com.livid.creategravlax.block;

import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import com.livid.creategravlax.registry.ModBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;


public final class EvaporatorBlock extends BasinBlock {
    public EvaporatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends BasinBlockEntity> getBlockEntityType() {
        return ModBlockEntities.EVAPORATOR.get();
    }
}
