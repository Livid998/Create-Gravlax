package com.livid.creategravlax.item;

import java.util.function.Consumer;

import com.livid.creategravlax.client.renderer.CentrifugeItemRenderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;


public final class CentrifugeBlockItem extends BlockItem {
    public CentrifugeBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CentrifugeItemRenderer.getInstance();
            }
        });
    }
}
