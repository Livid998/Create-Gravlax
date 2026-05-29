package com.livid.creategravlax.block;

import com.livid.creategravlax.registry.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class SugarBeetCropBlock extends CropBlock {
    public SugarBeetCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.SUGAR_BEET_SEEDS.get();
    }
}
