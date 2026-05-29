package com.livid.creategravlax.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.fluid.FluidHelper;

import com.livid.creategravlax.block.entity.CentrifugeBlockEntity;
import com.livid.creategravlax.registry.ModFluids;
import com.livid.creategravlax.registry.ModItems;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.ItemStackHandler;


public final class CentrifugeRenderer extends SafeBlockEntityRenderer<CentrifugeBlockEntity> {
    public CentrifugeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(
        CentrifugeBlockEntity centrifuge,
        float partialTicks,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay
    ) {
        renderLowerShaft(centrifuge, poseStack, buffer, light);
        renderInputFluidWindow(centrifuge, poseStack, buffer, light, overlay);
        renderBottleIndicators(centrifuge, partialTicks, poseStack, buffer, light, overlay);
        renderRotor(centrifuge, poseStack, buffer, light);
        renderItems(centrifuge, partialTicks, poseStack, buffer, light, overlay);
        FilteringRenderer.renderOnBlockEntity(centrifuge, partialTicks, poseStack, buffer, light, overlay);
    }

    private void renderRotor(
        CentrifugeBlockEntity centrifuge,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light
    ) {
        poseStack.pushPose();
        poseStack.translate(0, .41, 0);
        poseStack.translate(.5, .5, .5);
        poseStack.scale(1.10f, .34f, 1.10f);
        poseStack.translate(-.5, -.5, -.5);

        SuperByteBuffer rotor = CachedBuffers.partial(AllPartialModels.MILLSTONE_COG, centrifuge.getBlockState());
        if (centrifuge.isActivelySeparating()) {
            KineticBlockEntityRenderer.renderRotatingBuffer(
                centrifuge,
                rotor,
                poseStack,
                buffer.getBuffer(RenderType.cutoutMipped()),
                light
            );
        } else {
            rotor.light(light).renderInto(poseStack, buffer.getBuffer(RenderType.cutoutMipped()));
        }

        poseStack.popPose();
    }


    private void renderLowerShaft(
        CentrifugeBlockEntity centrifuge,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light
    ) {
        KineticBlockEntityRenderer.renderRotatingKineticBlock(
            centrifuge,
            KineticBlockEntityRenderer.shaft(net.minecraft.core.Direction.Axis.Y),
            poseStack,
            buffer.getBuffer(RenderType.solid()),
            light
        );
    }

    private void renderInputFluidWindow(
        CentrifugeBlockEntity centrifuge,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay
    ) {
        FluidStack input = centrifuge.getRenderedInputFluid();
        if (input.isEmpty() || input.getAmount() <= 0)
            return;

        IClientFluidTypeExtensions fluidClient = IClientFluidTypeExtensions.of(input.getFluid());
        var texture = fluidClient.getStillTexture(input);
        if (texture == null)
            return;

        TextureAtlasSprite sprite = Minecraft.getInstance()
            .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(texture);

        float fill = Mth.clamp(input.getAmount() / (float) CentrifugeBlockEntity.TANK_CAPACITY, 0f, 1f);
        float minX = 4.1f / 16f;
        float maxX = 11.9f / 16f;
        float minZ = 4.1f / 16f;
        float maxZ = 11.9f / 16f;
        float minY = 5.05f / 16f;
        float maxY = minY + fill * (6.95f / 16f);
        if (maxY <= minY + 0.01f)
            maxY = minY + 0.01f;

        int tint = fluidClient.getTintColor(input);
        int a = (tint >>> 24) & 0xFF;
        int r = (tint >>> 16) & 0xFF;
        int g = (tint >>> 8) & 0xFF;
        int b = tint & 0xFF;
        if (a == 0)
            a = 224;

        VertexConsumer consumer = buffer.getBuffer(RenderType.translucent());
        PoseStack.Pose pose = poseStack.last();

        renderDoubleSidedQuad(consumer, pose, light, overlay, r, g, b, a,
            minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, sprite, 0, 0, -1);
        renderDoubleSidedQuad(consumer, pose, light, overlay, r, g, b, a,
            maxX, minY, maxZ, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, sprite, 0, 0, 1);
        renderDoubleSidedQuad(consumer, pose, light, overlay, r, g, b, a,
            minX, minY, maxZ, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, sprite, -1, 0, 0);
        renderDoubleSidedQuad(consumer, pose, light, overlay, r, g, b, a,
            maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, sprite, 1, 0, 0);
        renderDoubleSidedQuad(consumer, pose, light, overlay, r, g, b, a,
            minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, sprite, 0, 1, 0);
    }

    private void renderBottleIndicators(
        CentrifugeBlockEntity centrifuge,
        float partialTicks,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay
    ) {
        int filledOutput = Mth.clamp((int) Math.ceil(centrifuge.getRenderedOutputFluid().getAmount() / 125.0), 0, 8);
        ItemStack outputBottle = chooseOutputBottleForFluid(centrifuge.getRenderedOutputFluid());
        ItemStack emptyBottle = new ItemStack(ModItems.EMPTY_PROCESS_BOTTLE.get());

        float time = (centrifuge.getLevel() != null ? centrifuge.getLevel().getGameTime() : 0) + partialTicks;
        float rotorAngle = centrifuge.isActivelySeparating()
            ? (time * centrifuge.getSpeed() * 3f / 10f)
            : 0f;

        poseStack.pushPose();
        poseStack.translate(.5f, .995f, .5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotorAngle));

        for (int i = 0; i < 8; i++) {
            ItemStack bottle = i < filledOutput ? outputBottle : emptyBottle;
            renderRotorMountedBottle(bottle, poseStack, buffer, light, overlay, i * 45f, .620f, .015f, .015625f);
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
            bottle, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer,
            Minecraft.getInstance().level, 0
        );
        poseStack.popPose();
    }

    private ItemStack chooseOutputBottleForFluid(FluidStack stack) {
        if (stack.isEmpty())
            return new ItemStack(ModItems.EMPTY_PROCESS_BOTTLE.get());

        if (FluidHelper.convertToStill(stack.getFluid())
            == FluidHelper.convertToStill(ModFluids.MOLASSES.get())) {
            return new ItemStack(ModItems.MOLASSES_PROCESS_BOTTLE.get());
        }

        return new ItemStack(ModItems.AMBER_PROCESS_BOTTLE.get());
    }

    private void renderItems(
        CentrifugeBlockEntity centrifuge,
        float partialTicks,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay
    ) {
        renderFirstItem(centrifuge, centrifuge.getRenderedInputItems(), poseStack, buffer, light, overlay, .16f, 0f, partialTicks);
        renderFirstItem(centrifuge, centrifuge.getRenderedOutputItems(), poseStack, buffer, light, overlay, .16f, 180f, partialTicks);
    }

    private void renderFirstItem(
        CentrifugeBlockEntity centrifuge,
        ItemStackHandler items,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int light,
        int overlay,
        float orbitRadius,
        float phaseDegrees,
        float partialTicks
    ) {
        ItemStack stack = ItemStack.EMPTY;
        for (int slot = 0; slot < items.getSlots(); slot++) {
            if (!items.getStackInSlot(slot).isEmpty()) {
                stack = items.getStackInSlot(slot);
                break;
            }
        }
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        float time = (centrifuge.getLevel() != null ? centrifuge.getLevel().getGameTime() : 0) + partialTicks;
        float bob = Mth.sin(time / 8f + phaseDegrees) * 0.018f;
        float orbit = (time * 2.5f + phaseDegrees) % 360f;
        poseStack.translate(.5f, .86f + bob, .5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(orbit));
        poseStack.translate(orbitRadius, 0, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(time * 4f));
        poseStack.scale(.28f, .28f, .28f);
        Minecraft.getInstance().getItemRenderer().renderStatic(
            stack, ItemDisplayContext.GROUND, light, overlay, poseStack, buffer,
            Minecraft.getInstance().level, 0
        );
        poseStack.popPose();
    }

    private void renderDoubleSidedQuad(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        int light,
        int overlay,
        int r,
        int g,
        int b,
        int a,
        float x1,
        float y1,
        float z1,
        float x2,
        float y2,
        float z2,
        float x3,
        float y3,
        float z3,
        float x4,
        float y4,
        float z4,
        TextureAtlasSprite sprite,
        float nx,
        float ny,
        float nz
    ) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        vertex(consumer, pose, x1, y1, z1, r, g, b, a, u0, v1, light, overlay, nx, ny, nz);
        vertex(consumer, pose, x2, y2, z2, r, g, b, a, u1, v1, light, overlay, nx, ny, nz);
        vertex(consumer, pose, x3, y3, z3, r, g, b, a, u1, v0, light, overlay, nx, ny, nz);
        vertex(consumer, pose, x4, y4, z4, r, g, b, a, u0, v0, light, overlay, nx, ny, nz);

        vertex(consumer, pose, x4, y4, z4, r, g, b, a, u0, v0, light, overlay, -nx, -ny, -nz);
        vertex(consumer, pose, x3, y3, z3, r, g, b, a, u1, v0, light, overlay, -nx, -ny, -nz);
        vertex(consumer, pose, x2, y2, z2, r, g, b, a, u1, v1, light, overlay, -nx, -ny, -nz);
        vertex(consumer, pose, x1, y1, z1, r, g, b, a, u0, v1, light, overlay, -nx, -ny, -nz);
    }

    private void vertex(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float x,
        float y,
        float z,
        int r,
        int g,
        int b,
        int a,
        float u,
        float v,
        int light,
        int overlay,
        float nx,
        float ny,
        float nz
    ) {
        consumer.addVertex(pose, x, y, z)
            .setColor(r, g, b, a)
            .setUv(u, v)
            .setOverlay(overlay)
            .setLight(light)
            .setNormal(pose, nx, ny, nz);
    }
}
