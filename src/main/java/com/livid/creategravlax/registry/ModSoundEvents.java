package com.livid.creategravlax.registry;

import com.livid.creategravlax.CreateGravlax;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
        DeferredRegister.create(Registries.SOUND_EVENT, CreateGravlax.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CENTRIFUGE_START =
        register("centrifuge_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> CENTRIFUGE_RUNNING =
        register("centrifuge_running");
    public static final DeferredHolder<SoundEvent, SoundEvent> CENTRIFUGE_STOP =
        register("centrifuge_stop");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
        ResourceLocation location =
            ResourceLocation.fromNamespaceAndPath(CreateGravlax.MOD_ID, id);
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(location));
    }

    private ModSoundEvents() {
    }

    public static void register(IEventBus modBus) {
        SOUND_EVENTS.register(modBus);
    }
}
