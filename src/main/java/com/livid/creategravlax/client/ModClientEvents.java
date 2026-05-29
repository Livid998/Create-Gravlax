package com.livid.creategravlax.client;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.client.renderer.EvaporatorRenderer;
import com.livid.creategravlax.client.renderer.CentrifugeRenderer;
import com.livid.creategravlax.registry.ModBlockEntities;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = CreateGravlax.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClientEvents {
    private ModClientEvents() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.EVAPORATOR.get(), EvaporatorRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CENTRIFUGE.get(), CentrifugeRenderer::new);
    }
}
