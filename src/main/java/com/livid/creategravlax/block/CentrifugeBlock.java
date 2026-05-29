package com.livid.creategravlax.block;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;

import com.livid.creategravlax.block.entity.CentrifugeBlockEntity;
import com.livid.creategravlax.registry.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.neoforge.fluids.FluidUtil;

import org.jetbrains.annotations.Nullable;


public final class CentrifugeBlock extends KineticBlock implements IBE<CentrifugeBlockEntity> {
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(0, 0, 0, 16, 4, 16),
        Block.box(1, 4, 1, 15, 14, 15)
    );

    public CentrifugeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public Class<CentrifugeBlockEntity> getBlockEntityClass() {
        return CentrifugeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CentrifugeBlockEntity> getBlockEntityType() {
        return ModBlockEntities.CENTRIFUGE.get();
    }

    @Override
    protected ItemInteractionResult useItemOn(
        ItemStack stack,
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hitResult
    ) {
        CentrifugeBlockEntity centrifuge = getBlockEntity(level, pos);
        if (centrifuge == null) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        Direction face = hitResult.getDirection();

        
        
        if (stack.isEmpty() && centrifuge.hasSolidContents()) {
            if (!level.isClientSide) {
                centrifuge.returnAllSolidContents(player);
            }
            return ItemInteractionResult.SUCCESS;
        }

        
        if (face.getAxis().isHorizontal()
            && FluidUtil.interactWithFluidHandler(player, hand, centrifuge.getSideFluidHandler())) {
            return ItemInteractionResult.SUCCESS;
        }

        
        if (face == Direction.UP && !stack.isEmpty()) {
            ItemStack simulated = centrifuge.getTopItemHandler().insertItem(0, stack.copy(), true);
            int accepted = stack.getCount() - simulated.getCount();
            if (accepted <= 0) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (!level.isClientSide) {
                ItemStack remainder = centrifuge.getTopItemHandler().insertItem(0, stack.copy(), false);
                int inserted = stack.getCount() - remainder.getCount();
                if (!player.getAbilities().instabuild) {
                    stack.shrink(inserted);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        super.updateEntityAfterFallOn(level, entity);

        if (!(entity instanceof ItemEntity itemEntity) || entity.level().isClientSide || !entity.isAlive()) {
            return;
        }

        CentrifugeBlockEntity centrifuge = getBlockEntity(level, entity.blockPosition());
        if (centrifuge == null) {
            centrifuge = getBlockEntity(level, entity.blockPosition().below());
        }
        if (centrifuge == null) {
            return;
        }

        ItemStack remainder = centrifuge.getTopItemHandler().insertItem(0, itemEntity.getItem(), false);
        if (remainder.isEmpty()) {
            itemEntity.discard();
        } else if (remainder.getCount() < itemEntity.getItem().getCount()) {
            itemEntity.setItem(remainder);
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
