package com.livid.creategravlax.registry;

import com.livid.creategravlax.CreateGravlax;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateGravlax.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.create_gravlax"))
        .icon(() -> ModItems.CENTRIFUGE.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(ModItems.EVAPORATOR.get());
            output.accept(ModItems.CENTRIFUGE.get());
            output.accept(ModItems.SUGAR_BEET_SEEDS.get());
            output.accept(ModItems.DIRTY_SUGAR_BEET.get());
            output.accept(ModItems.DIRTY_BEETROOT.get());
            output.accept(ModItems.SUGAR_BEET.get());
            output.accept(ModItems.SUGAR_BEET_CRATE.get());
            output.accept(ModItems.BEETROOT_CRATE.get());
            output.accept(ModItems.SUGAR_BEET_COSSETTES.get());
            output.accept(ModItems.RED_BEET_COSSETTES.get());
            output.accept(ModItems.BEET_PULP.get());
            output.accept(ModItems.ANIMAL_FODDER.get());
            output.accept(ModItems.RAW_SUGAR.get());
            output.accept(ModItems.RED_RAW_SUGAR.get());
            output.accept(ModItems.SUGAR_CRYSTAL.get());
            output.accept(ModItems.LIME_POWDER.get());
            output.accept(ModItems.CALCIUM_SACCHARATE_PASTE.get());
            output.accept(ModItems.LIME_RESIDUE.get());
            output.accept(ModItems.MUD_CLUMP.get());
            output.accept(ModItems.SALT.get());
            output.accept(ModItems.RAW_ROCK_SALT.get());
            output.accept(ModItems.GRAVLAX_CURE.get());
            output.accept(ModItems.GRAVLAX_SALMON.get());
            output.accept(ModItems.GRAVLAX_SALMON_SLICE.get());
            output.accept(ModItems.BITTER_SPICE.get());
            output.accept(ModItems.BRIGHT_SPICE.get());
            output.accept(ModItems.FUNKY_SPICE.get());
            output.accept(ModItems.HEARTY_SPICE.get());
            output.accept(ModItems.SPICY_SPICE.get());
            output.accept(ModItems.ZESTY_SPICE.get());
            output.accept(ModItems.MINTY_SPICE.get());
            output.accept(ModItems.SMOKY_SPICE.get());
            output.accept(ModItems.SWEET_SPICE.get());
            output.accept(ModItems.UMAMI_SPICE.get());
            output.accept(ModItems.FOUL_SPICE.get());
            output.accept(ModItems.NIGHT_VISION_ESSENCE.get());
            output.accept(ModItems.SLOW_FALLING_ESSENCE.get());
            output.accept(ModItems.INVISIBILITY_ESSENCE.get());
            output.accept(ModItems.LEAPING_ESSENCE.get());
            output.accept(ModItems.FIRE_RESISTANCE_ESSENCE.get());
            output.accept(ModItems.SWIFTNESS_ESSENCE.get());
            output.accept(ModItems.WATER_BREATHING_ESSENCE.get());
            output.accept(ModItems.STRENGTH_ESSENCE.get());
            output.accept(ModItems.REGENERATION_ESSENCE.get());
            output.accept(ModItems.HEALING_ESSENCE.get());
            output.accept(ModItems.POISON_ESSENCE.get());
            output.accept(ModItems.WEAKNESS_ESSENCE.get());
            output.accept(ModItems.SLOWNESS_ESSENCE.get());
            output.accept(ModItems.HARMING_ESSENCE.get());
            output.accept(ModItems.BLINDNESS_ESSENCE.get());
            output.accept(ModItems.MILK_BOTTLE.get());
            output.accept(ModItems.MILKY_POTION.get());
            output.accept(ModItems.ANTIDOTE.get());
            output.accept(ModItems.ELIXIR.get());
            output.accept(ModItems.TONIC.get());
            output.accept(ModItems.EYE_DROPS.get());
            output.accept(ModItems.ROCK_SALT_ORE.get());
            output.accept(ModItems.DEEPSLATE_ROCK_SALT_ORE.get());
            output.accept(ModItems.RAW_ROCK_SALT_BLOCK.get());
            output.accept(ModItems.SALT_BLOCK.get());
            output.accept(ModItems.BUDDING_SALT_BLOCK.get());
            output.accept(ModItems.SMALL_SALT_BUD.get());
            output.accept(ModItems.MEDIUM_SALT_BUD.get());
            output.accept(ModItems.LARGE_SALT_BUD.get());
            output.accept(ModItems.SALT_CLUSTER.get());
            output.accept(ModItems.SALT_LAMP.get());
            output.accept(ModItems.SUGAR_BLOCK.get());
            output.accept(ModFluids.SUGAR_BEET_JUICE.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.THICK_SUGAR_BEET_JUICE.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.RED_BEET_JUICE.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.THICK_RED_BEET_JUICE.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.MOLASSES.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.CALCIUM_SACCHARATE.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.SALT_WATER.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.WASTE_WATER.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            output.accept(ModFluids.LIME_WATER.get().getBucket(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        }).build());
    private ModCreativeTabs() {}
    public static void register(IEventBus modBus) { TABS.register(modBus); }
}
