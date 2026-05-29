package com.livid.creategravlax.item;

import com.livid.creategravlax.block.SaltBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;


public final class SaltItem extends Item {
    public SaltItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level instanceof ServerLevel serverLevel && (level.getBlockState(pos).is(Blocks.SNOW)
            || level.getBlockState(pos).is(Blocks.SNOW_BLOCK)
            || level.getBlockState(pos).is(Blocks.ICE)
            || level.getBlockState(pos).is(Blocks.FROSTED_ICE))) {
            SaltBlock.meltNearby(serverLevel, pos, 3);
            consume(context);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private static void consume(UseOnContext context) {
        if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
    }
}
