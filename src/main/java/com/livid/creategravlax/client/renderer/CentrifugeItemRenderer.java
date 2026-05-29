package com.livid.creategravlax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;

import com.livid.creategravlax.registry.ModBlocks;
import com.livid.creategravlax.registry.ModItems;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


public final class CentrifugeItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static CentrifugeItemRenderer instance;

    private CentrifugeItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    public static CentrifugeItemRenderer getInstance() {
        if (instance == null) {
            Minecraft minecraft = Minecraft.getInstance();
            instance = new CentrifugeItemRenderer(
                minecraft.getBlockEntityRenderDispatcher(),
                minecraft.getEntityModels()
            );
        }
        return instance;
    }

    @Override
    public void renderByItem(
        ItemStack stack,
        ItemDisplayContext displayContext,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay
    ) {
        poseStack.pushPose();
        applyHandScale(displayContext, poseStack);

        
        
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
            ModBlocks.CENTRIFUGE.get().defaultBlockState(),
            poseStack,
            buffer,
            light,
            overlay
        );

        renderStaticRotor(poseStack, buffer, light);
        renderStaticBottles(poseStack, buffer, light, overlay);
        poseStack.popPose();
    }

    


    private void applyHandScale(ItemDisplayContext displayContext, PoseStack poseStack) {
        float scale;
        switch (displayContext) {
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> scale = .60f;
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> scale = .72f;
            default -> {
                return;
            }
        }

        poseStack.translate(.5f, .5f, .5f);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(-.5f, -.5f, -.5f);
    }

    private void renderStaticRotor(PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        
        poseStack.translate(0, .41, 0);
        poseStack.translate(.5, .5, .5);
        poseStack.scale(1.10f, .34f, 1.10f);
        poseStack.translate(-.5, -.5, -.5);

        SuperByteBuffer rotor = CachedBuffers.partial(
            AllPartialModels.MILLSTONE_COG,
            ModBlocks.CENTRIFUGE.get().defaultBlockState()
        );
        rotor.light(light).renderInto(poseStack, buffer.getBuffer(RenderType.cutoutMipped()));
        poseStack.popPose();
    }

    private void renderStaticBottles(
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay
    ) {
        ItemStack bottle = new ItemStack(ModItems.EMPTY_PROCESS_BOTTLE.get());

        poseStack.pushPose();
        
        poseStack.translate(.5f, .995f, .5f);
        for (int i = 0; i < 8; i++) {
            renderRotorMountedBottle(
                bottle,
                poseStack,
                buffer,
                light,
                overlay,
                i * 45f,
                .620f,
                .015f,
                .015625f
            );
        }
        poseStack.popPose();
    }

    private void renderRotorMountedBottle(
        ItemStack bottle,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay,
        float armAngle,
        float radius,
        float yOffset,
        float zOffset
    ) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(armAngle));
        poseStack.translate(radius, yOffset, zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
        poseStack.scale(.42f, .42f, .42f);
        Minecraft.getInstance().getItemRenderer().renderStatic(
            bottle,
            ItemDisplayContext.FIXED,
            light,
            overlay,
            poseStack,
            buffer,
            Minecraft.getInstance().level,
            0
        );
        poseStack.popPose();
    }
}
