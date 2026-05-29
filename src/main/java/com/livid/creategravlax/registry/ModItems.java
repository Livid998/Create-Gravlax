package com.livid.creategravlax.registry;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.item.SaltItem;
import com.livid.creategravlax.item.CentrifugeBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateGravlax.MOD_ID);
    public static final DeferredItem<ItemNameBlockItem> SUGAR_BEET_SEEDS = ITEMS.registerItem("sugar_beet_seeds",
        properties -> new ItemNameBlockItem(ModBlocks.SUGAR_BEET_CROP.get(), properties), new Item.Properties());
    public static final DeferredItem<Item> DIRTY_SUGAR_BEET = ITEMS.registerSimpleItem("dirty_sugar_beet", new Item.Properties());
    public static final DeferredItem<Item> DIRTY_BEETROOT = ITEMS.registerSimpleItem("dirty_beetroot", new Item.Properties());
    public static final DeferredItem<Item> SUGAR_BEET = ITEMS.registerSimpleItem("sugar_beet", new Item.Properties());
    public static final DeferredItem<Item> SUGAR_BEET_COSSETTES = ITEMS.registerSimpleItem("sugar_beet_cossettes", new Item.Properties());
    public static final DeferredItem<Item> RED_BEET_COSSETTES = ITEMS.registerSimpleItem("red_beet_cossettes", new Item.Properties());
    public static final DeferredItem<Item> BEET_PULP = ITEMS.registerSimpleItem("beet_pulp", new Item.Properties());
    public static final DeferredItem<Item> ANIMAL_FODDER = ITEMS.registerSimpleItem("animal_fodder", new Item.Properties());
    public static final DeferredItem<Item> RAW_SUGAR = ITEMS.registerSimpleItem("raw_sugar", new Item.Properties());
    public static final DeferredItem<Item> RED_RAW_SUGAR = ITEMS.registerSimpleItem("red_raw_sugar", new Item.Properties());
    public static final DeferredItem<Item> SUGAR_CRYSTAL = ITEMS.registerSimpleItem("sugar_crystal", new Item.Properties());
    public static final DeferredItem<Item> LIME_POWDER = ITEMS.registerSimpleItem("lime_powder", new Item.Properties());
    public static final DeferredItem<Item> CALCIUM_SACCHARATE_PASTE = ITEMS.registerSimpleItem("calcium_saccharate_paste", new Item.Properties());
    public static final DeferredItem<Item> LIME_RESIDUE = ITEMS.registerSimpleItem("lime_residue", new Item.Properties());
    public static final DeferredItem<Item> MUD_CLUMP = ITEMS.registerSimpleItem("mud_clump", new Item.Properties());
    public static final DeferredItem<Item> SALT = ITEMS.register("salt", () -> new SaltItem(new Item.Properties()));
    public static final DeferredItem<Item> RAW_ROCK_SALT = ITEMS.registerSimpleItem("raw_rock_salt", new Item.Properties());
    public static final DeferredItem<Item> GRAVLAX_CURE = ITEMS.registerSimpleItem("gravlax_cure", new Item.Properties());

    
    public static final DeferredItem<Item> NIGHT_VISION_ESSENCE = ITEMS.registerSimpleItem("night_vision_essence", new Item.Properties());
    public static final DeferredItem<Item> SLOW_FALLING_ESSENCE = ITEMS.registerSimpleItem("slow_falling_essence", new Item.Properties());
    public static final DeferredItem<Item> INVISIBILITY_ESSENCE = ITEMS.registerSimpleItem("invisibility_essence", new Item.Properties());
    public static final DeferredItem<Item> LEAPING_ESSENCE = ITEMS.registerSimpleItem("leaping_essence", new Item.Properties());
    public static final DeferredItem<Item> FIRE_RESISTANCE_ESSENCE = ITEMS.registerSimpleItem("fire_resistance_essence", new Item.Properties());
    public static final DeferredItem<Item> SWIFTNESS_ESSENCE = ITEMS.registerSimpleItem("swiftness_essence", new Item.Properties());
    public static final DeferredItem<Item> WATER_BREATHING_ESSENCE = ITEMS.registerSimpleItem("water_breathing_essence", new Item.Properties());
    public static final DeferredItem<Item> STRENGTH_ESSENCE = ITEMS.registerSimpleItem("strength_essence", new Item.Properties());
    public static final DeferredItem<Item> REGENERATION_ESSENCE = ITEMS.registerSimpleItem("regeneration_essence", new Item.Properties());
    public static final DeferredItem<Item> HEALING_ESSENCE = ITEMS.registerSimpleItem("healing_essence", new Item.Properties());
    public static final DeferredItem<Item> POISON_ESSENCE = ITEMS.registerSimpleItem("poison_essence", new Item.Properties());
    public static final DeferredItem<Item> WEAKNESS_ESSENCE = ITEMS.registerSimpleItem("weakness_essence", new Item.Properties());
    public static final DeferredItem<Item> SLOWNESS_ESSENCE = ITEMS.registerSimpleItem("slowness_essence", new Item.Properties());
    public static final DeferredItem<Item> HARMING_ESSENCE = ITEMS.registerSimpleItem("harming_essence", new Item.Properties());
    public static final DeferredItem<Item> BLINDNESS_ESSENCE = ITEMS.registerSimpleItem("blindness_essence", new Item.Properties());

    
    public static final DeferredItem<Item> BITTER_SPICE = ITEMS.registerSimpleItem("bitter_spice", new Item.Properties());
    public static final DeferredItem<Item> BRIGHT_SPICE = ITEMS.registerSimpleItem("bright_spice", new Item.Properties());
    public static final DeferredItem<Item> FUNKY_SPICE = ITEMS.registerSimpleItem("funky_spice", new Item.Properties());
    public static final DeferredItem<Item> HEARTY_SPICE = ITEMS.registerSimpleItem("hearty_spice", new Item.Properties());
    public static final DeferredItem<Item> SPICY_SPICE = ITEMS.registerSimpleItem("spicy_spice", new Item.Properties());
    public static final DeferredItem<Item> ZESTY_SPICE = ITEMS.registerSimpleItem("zesty_spice", new Item.Properties());
    public static final DeferredItem<Item> MINTY_SPICE = ITEMS.registerSimpleItem("minty_spice", new Item.Properties());
    public static final DeferredItem<Item> SMOKY_SPICE = ITEMS.registerSimpleItem("smoky_spice", new Item.Properties());
    public static final DeferredItem<Item> SWEET_SPICE = ITEMS.registerSimpleItem("sweet_spice", new Item.Properties());
    public static final DeferredItem<Item> UMAMI_SPICE = ITEMS.registerSimpleItem("umami_spice", new Item.Properties());
    public static final DeferredItem<Item> FOUL_SPICE = ITEMS.registerSimpleItem("foul_spice", new Item.Properties());

    
    public static final DeferredItem<Item> MILK_BOTTLE = ITEMS.registerSimpleItem("milk_bottle",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build()));
    public static final DeferredItem<Item> MILKY_POTION = ITEMS.registerSimpleItem("milky_potion",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build()));
    public static final DeferredItem<Item> ANTIDOTE = ITEMS.registerSimpleItem("antidote",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build()));
    public static final DeferredItem<Item> ELIXIR = ITEMS.registerSimpleItem("elixir",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build()));
    public static final DeferredItem<Item> TONIC = ITEMS.registerSimpleItem("tonic",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build()));
    public static final DeferredItem<Item> EYE_DROPS = ITEMS.registerSimpleItem("eye_drops",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0).alwaysEdible().build()));
    public static final DeferredItem<Item> GRAVLAX_SALMON = ITEMS.registerSimpleItem("gravlax_salmon",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.9f).build()));
    public static final DeferredItem<Item> GRAVLAX_SALMON_SLICE = ITEMS.registerSimpleItem("gravlax_salmon_slice",
        new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.8f).build()));

    
    public static final DeferredItem<Item> EMPTY_PROCESS_BOTTLE = ITEMS.registerSimpleItem("empty_process_bottle", new Item.Properties());
    public static final DeferredItem<Item> AMBER_PROCESS_BOTTLE = ITEMS.registerSimpleItem("amber_process_bottle", new Item.Properties());
    public static final DeferredItem<Item> RED_PROCESS_BOTTLE = ITEMS.registerSimpleItem("red_process_bottle", new Item.Properties());
    public static final DeferredItem<Item> MOLASSES_PROCESS_BOTTLE = ITEMS.registerSimpleItem("molasses_process_bottle", new Item.Properties());
    public static final DeferredItem<Item> SACCHARATE_PROCESS_BOTTLE = ITEMS.registerSimpleItem("saccharate_process_bottle", new Item.Properties());

    public static final DeferredItem<BlockItem> EVAPORATOR = blockItem("evaporator", ModBlocks.EVAPORATOR);
    public static final DeferredItem<BlockItem> CENTRIFUGE = ITEMS.register("centrifuge",
        () -> new CentrifugeBlockItem(ModBlocks.CENTRIFUGE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> ROCK_SALT_ORE = blockItem("rock_salt_ore", ModBlocks.ROCK_SALT_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_ROCK_SALT_ORE = blockItem("deepslate_rock_salt_ore", ModBlocks.DEEPSLATE_ROCK_SALT_ORE);
    public static final DeferredItem<BlockItem> RAW_ROCK_SALT_BLOCK = blockItem("raw_rock_salt_block", ModBlocks.RAW_ROCK_SALT_BLOCK);
    public static final DeferredItem<BlockItem> SALT_BLOCK = blockItem("salt_block", ModBlocks.SALT_BLOCK);
    public static final DeferredItem<BlockItem> BUDDING_SALT_BLOCK = blockItem("budding_salt_block", ModBlocks.BUDDING_SALT_BLOCK);
    public static final DeferredItem<BlockItem> SMALL_SALT_BUD = blockItem("small_salt_bud", ModBlocks.SMALL_SALT_BUD);
    public static final DeferredItem<BlockItem> MEDIUM_SALT_BUD = blockItem("medium_salt_bud", ModBlocks.MEDIUM_SALT_BUD);
    public static final DeferredItem<BlockItem> LARGE_SALT_BUD = blockItem("large_salt_bud", ModBlocks.LARGE_SALT_BUD);
    public static final DeferredItem<BlockItem> SALT_CLUSTER = blockItem("salt_cluster", ModBlocks.SALT_CLUSTER);
    public static final DeferredItem<BlockItem> SALT_LAMP = blockItem("salt_lamp", ModBlocks.SALT_LAMP);
    public static final DeferredItem<BlockItem> SUGAR_BLOCK = blockItem("sugar_block", ModBlocks.SUGAR_BLOCK);
    public static final DeferredItem<BlockItem> SUGAR_BEET_CRATE = blockItem("sugar_beet_crate", ModBlocks.SUGAR_BEET_CRATE);
    public static final DeferredItem<BlockItem> BEETROOT_CRATE = blockItem("beetroot_crate", ModBlocks.BEETROOT_CRATE);

    private static DeferredItem<BlockItem> blockItem(String id, net.neoforged.neoforge.registries.DeferredBlock<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    private ModItems() {}
    public static void register(IEventBus modBus) { ITEMS.register(modBus); }
}
