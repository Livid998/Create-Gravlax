package com.livid.creategravlax.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;

import com.livid.creategravlax.registry.ModItems;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;


public final class AnimatedCentrifuge {
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        
        
        poseStack.translate(xOffset, yOffset - 12, 0);
        poseStack.scale(2.05f, 2.05f, 2.05f);
        graphics.renderItem(new ItemStack(ModItems.CENTRIFUGE.get()), 0, 0);
        poseStack.popPose();
    }
}
