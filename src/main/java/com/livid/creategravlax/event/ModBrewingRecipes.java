package com.livid.creategravlax.event;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.registry.ModItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;


@EventBusSubscriber(modid = CreateGravlax.MOD_ID)
public final class ModBrewingRecipes {
    private ModBrewingRecipes() {
    }

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(
            Ingredient.of(ModItems.MILK_BOTTLE.get()),
            Ingredient.of(Items.NETHER_WART),
            new ItemStack(ModItems.MILKY_POTION.get())
        );
        event.getBuilder().addRecipe(
            Ingredient.of(ModItems.MILKY_POTION.get()),
            Ingredient.of(ModItems.POISON_ESSENCE.get()),
            new ItemStack(ModItems.ANTIDOTE.get())
        );
        event.getBuilder().addRecipe(
            Ingredient.of(ModItems.MILKY_POTION.get()),
            Ingredient.of(ModItems.WEAKNESS_ESSENCE.get()),
            new ItemStack(ModItems.ELIXIR.get())
        );
        event.getBuilder().addRecipe(
            Ingredient.of(ModItems.MILKY_POTION.get()),
            Ingredient.of(ModItems.SLOWNESS_ESSENCE.get()),
            new ItemStack(ModItems.TONIC.get())
        );
        event.getBuilder().addRecipe(
            Ingredient.of(ModItems.MILKY_POTION.get()),
            Ingredient.of(ModItems.BLINDNESS_ESSENCE.get()),
            new ItemStack(ModItems.EYE_DROPS.get())
        );
    }
}
