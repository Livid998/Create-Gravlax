package com.livid.creategravlax;

import com.livid.creategravlax.registry.ModBlocks;
import com.livid.creategravlax.registry.ModBlockEntities;
import com.livid.creategravlax.registry.ModCreativeTabs;
import com.livid.creategravlax.registry.ModFluids;
import com.livid.creategravlax.registry.ModItems;
import com.livid.creategravlax.registry.ModRecipeSerializers;
import com.livid.creategravlax.registry.ModSoundEvents;
import com.simibubi.create.api.stress.BlockStressValues;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraft.world.level.block.ComposterBlock;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(CreateGravlax.MOD_ID)
public final class CreateGravlax {
    public static final String MOD_ID = "create_gravlax";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateGravlax(IEventBus modBus, ModContainer modContainer) {
        ModBlocks.register(modBus);
        ModItems.register(modBus);
        ModRecipeSerializers.register(modBus);
        ModBlockEntities.register(modBus);
        modBus.addListener(ModBlockEntities::registerCapabilities);
        ModCreativeTabs.register(modBus);
        ModFluids.register(modBus);
        ModSoundEvents.register(modBus);
        modBus.addListener(this::commonSetup);

        LOGGER.info("Loading Create: Gravlax 1.0");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockStressValues.IMPACTS.register(ModBlocks.CENTRIFUGE.get(), () -> 4.0);
            ComposterBlock.COMPOSTABLES.put(ModItems.SUGAR_BEET.get(), 0.65f);
        });
    }
}
