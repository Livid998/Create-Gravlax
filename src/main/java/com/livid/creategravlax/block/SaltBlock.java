package com.livid.creategravlax.block;

import com.simibubi.create.foundation.fluid.FluidHelper;
import com.livid.creategravlax.registry.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


public final class SaltBlock extends Block {
    public SaltBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block changedBlock, BlockPos fromPos, boolean moving) {
        super.neighborChanged(state, level, pos, changedBlock, fromPos, moving);
        if (!level.isClientSide) level.scheduleTick(pos, this, 1);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        meltNearby(level, pos, 5);
        if (!touchesWaterSource(level, pos)) {
            level.scheduleTick(pos, this, 30);
            return;
        }

        BlockState saltWater = FluidHelper.convertToStill(ModFluids.SALT_WATER.get())
            .defaultFluidState().createLegacyBlock();
        for (BlockPos target : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
            if (target.equals(pos) || isWaterSource(level, target)) {
                level.setBlockAndUpdate(target.immutable(), saltWater);
            }
        }
    }

    private boolean touchesWaterSource(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isWaterSource(level, pos.relative(direction))) return true;
        }
        return false;
    }

    public static boolean isWaterSource(Level level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos).isSource();
    }

    public static void meltNearby(ServerLevel level, BlockPos origin, int radius) {
        for (BlockPos target : BlockPos.betweenClosed(origin.offset(-radius, -1, -radius), origin.offset(radius, 2, radius))) {
            BlockState state = level.getBlockState(target);
            if (state.is(Blocks.SNOW) || state.is(Blocks.SNOW_BLOCK)) {
                level.destroyBlock(target.immutable(), false);
            } else if (state.is(Blocks.ICE) || state.is(Blocks.FROSTED_ICE)) {
                level.setBlockAndUpdate(target.immutable(), Blocks.WATER.defaultBlockState());
            }
        }
    }
}
