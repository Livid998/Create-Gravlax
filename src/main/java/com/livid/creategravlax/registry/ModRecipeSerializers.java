package com.livid.creategravlax.registry;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.recipe.SeasoningRecipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateGravlax.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SeasoningRecipe>> SEASONING =
        SERIALIZERS.register("seasoning", () -> new SimpleCraftingRecipeSerializer<>(SeasoningRecipe::new));

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus modBus) {
        SERIALIZERS.register(modBus);
    }
}
