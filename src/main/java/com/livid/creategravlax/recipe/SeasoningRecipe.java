package com.livid.creategravlax.recipe;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.registry.ModItems;
import com.livid.creategravlax.registry.ModRecipeSerializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public final class SeasoningRecipe extends CustomRecipe {
    public static final String SEASONING_KEY = "create_gravlax_seasoning";
    public static final TagKey<Item> CAN_BE_SEASONED_SAVORY = tag("can_be_seasoned_savory");
    public static final TagKey<Item> CAN_BE_SEASONED_SWEET = tag("can_be_seasoned_sweet");

    private static TagKey<Item> tag(String id) {
        return TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(CreateGravlax.MOD_ID, id));
    }

    public SeasoningRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack seasoning = ItemStack.EMPTY;
        ItemStack food = ItemStack.EMPTY;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (profileFor(stack) != null) {
                if (!seasoning.isEmpty()) return false;
                seasoning = stack;
            } else if (stack.is(CAN_BE_SEASONED_SAVORY) || stack.is(CAN_BE_SEASONED_SWEET)) {
                if (!food.isEmpty() || hasSeasoning(stack)) return false;
                food = stack;
            } else return false;
        }
        return !seasoning.isEmpty() && !food.isEmpty() && accepts(profileFor(seasoning), food);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        String profile = null;
        ItemStack food = ItemStack.EMPTY;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            String candidate = profileFor(stack);
            if (candidate != null) profile = candidate;
            else if (stack.is(CAN_BE_SEASONED_SAVORY) || stack.is(CAN_BE_SEASONED_SWEET)) food = stack;
        }
        return profile != null && !food.isEmpty() && accepts(profile, food)
            ? applySeasoning(food, profile) : ItemStack.EMPTY;
    }

    public static ItemStack applySeasoning(ItemStack food, String profile) {
        ItemStack seasoned = food.copyWithCount(1);
        CustomData.update(DataComponents.CUSTOM_DATA, seasoned,
            tag -> tag.putString(SEASONING_KEY, profile));
        return seasoned;
    }

    public static String profileFor(ItemStack stack) {
        if (stack.is(ModItems.SALT.get())) return "salty";
        if (stack.is(ModItems.BITTER_SPICE.get())) return "bitter";
        if (stack.is(ModItems.BRIGHT_SPICE.get())) return "bright";
        if (stack.is(ModItems.FUNKY_SPICE.get())) return "funky";
        if (stack.is(ModItems.HEARTY_SPICE.get())) return "hearty";
        if (stack.is(ModItems.SPICY_SPICE.get())) return "spicy";
        if (stack.is(ModItems.ZESTY_SPICE.get())) return "zesty";
        if (stack.is(ModItems.MINTY_SPICE.get())) return "minty";
        if (stack.is(ModItems.SMOKY_SPICE.get())) return "smoky";
        if (stack.is(ModItems.SWEET_SPICE.get())) return "sweet";
        if (stack.is(ModItems.UMAMI_SPICE.get())) return "umami";
        if (stack.is(ModItems.FOUL_SPICE.get())) return "foul";
        return null;
    }

    public static boolean accepts(String profile, ItemStack food) {
        if (profile == null) return false;
        if ("funky".equals(profile) || "foul".equals(profile)) {
            return food.is(CAN_BE_SEASONED_SAVORY) || food.is(CAN_BE_SEASONED_SWEET);
        }
        if ("zesty".equals(profile) || "minty".equals(profile) || "sweet".equals(profile)) {
            return food.is(CAN_BE_SEASONED_SWEET);
        }
        return food.is(CAN_BE_SEASONED_SAVORY);
    }

    public static String seasoningOn(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return null;
        CompoundTag tag = data.copyTag();
        return tag.contains(SEASONING_KEY) ? tag.getString(SEASONING_KEY) : null;
    }

    private static boolean hasSeasoning(ItemStack stack) {
        return seasoningOn(stack) != null;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
    @Override public boolean isSpecial() { return true; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipeSerializers.SEASONING.get(); }
}
