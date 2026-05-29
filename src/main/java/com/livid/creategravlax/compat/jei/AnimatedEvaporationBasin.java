package com.livid.creategravlax.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

import com.livid.creategravlax.registry.ModBlocks;

import net.minecraft.client.gui.GuiGraphics;


public final class AnimatedEvaporationBasin extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, 200);
        poseStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        poseStack.mulPose(Axis.YP.rotationDegrees(22.5f));

        blockElement(ModBlocks.EVAPORATOR.get().defaultBlockState())
            .atLocal(0, 1.65, 0)
            .scale(23)
            .render(graphics);

        poseStack.popPose();
    }
}
