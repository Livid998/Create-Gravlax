package com.livid.creategravlax.block;

import com.livid.creategravlax.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


public final class BuddingSaltBlock extends Block {
    public BuddingSaltBlock(Properties properties) { super(properties); }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(5) != 0) return;
        Direction direction = Direction.getRandom(random);
        BlockPos growthPos = pos.relative(direction);
        BlockState current = level.getBlockState(growthPos);
        Block next = null;
        if (current.isAir() || current.canBeReplaced()) next = ModBlocks.SMALL_SALT_BUD.get();
        else if (current.is(ModBlocks.SMALL_SALT_BUD.get())) next = ModBlocks.MEDIUM_SALT_BUD.get();
        else if (current.is(ModBlocks.MEDIUM_SALT_BUD.get())) next = ModBlocks.LARGE_SALT_BUD.get();
        else if (current.is(ModBlocks.LARGE_SALT_BUD.get())) next = ModBlocks.SALT_CLUSTER.get();
        if (next != null) {
            level.setBlockAndUpdate(growthPos, next.defaultBlockState()
                .setValue(AmethystClusterBlock.FACING, direction)
                .setValue(AmethystClusterBlock.WATERLOGGED, false));
        }
    }
}
