package com.livid.creategravlax.compat.jei;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.foundation.fluid.FluidHelper;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.fluid.ConcentratedPotionFluid;
import com.livid.creategravlax.recipe.PotionEssenceResolver;
import com.livid.creategravlax.recipe.SeasoningRecipe;
import com.livid.creategravlax.registry.ModFluids;
import com.livid.creategravlax.registry.ModItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;

import net.neoforged.neoforge.fluids.FluidStack;

@JeiPlugin
public final class CreateGravlaxJEI implements IModPlugin {
    private static final ResourceLocation ID =
        ResourceLocation.fromNamespaceAndPath(CreateGravlax.MOD_ID, "jei_plugin");

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new EvaporationBasinCategory(registration.getJeiHelpers().getGuiHelper()),
            new CentrifugeCategory(registration.getJeiHelpers().getGuiHelper()),
            new SeasoningCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<EvaporationRecipeDisplay> evaporation = new ArrayList<>(List.of(
            new EvaporationRecipeDisplay(new FluidStack(FluidHelper.convertToStill(ModFluids.SUGAR_BEET_JUICE.get()), 1000),
                new FluidStack(FluidHelper.convertToStill(ModFluids.THICK_SUGAR_BEET_JUICE.get()), 500), ItemStack.EMPTY),
            new EvaporationRecipeDisplay(new FluidStack(FluidHelper.convertToStill(ModFluids.RED_BEET_JUICE.get()), 1000),
                new FluidStack(FluidHelper.convertToStill(ModFluids.THICK_RED_BEET_JUICE.get()), 500), ItemStack.EMPTY),
            new EvaporationRecipeDisplay(new FluidStack(Fluids.WATER, 1000), FluidStack.EMPTY,
                new ItemStack(ModItems.SALT.get())),
            new EvaporationRecipeDisplay(new FluidStack(FluidHelper.convertToStill(ModFluids.SALT_WATER.get()), 1000),
                FluidStack.EMPTY, new ItemStack(ModItems.SALT.get()))
        ));

        List<CentrifugeRecipeDisplay> centrifuge = new ArrayList<>(List.of(
            CentrifugeRecipeDisplay.fluid(
                new FluidStack(FluidHelper.convertToStill(ModFluids.THICK_SUGAR_BEET_JUICE.get()), 1000),
                new ItemStack(ModItems.RAW_SUGAR.get(), 10),
                new FluidStack(FluidHelper.convertToStill(ModFluids.MOLASSES.get()), 400)),
            CentrifugeRecipeDisplay.fluid(
                new FluidStack(FluidHelper.convertToStill(ModFluids.THICK_RED_BEET_JUICE.get()), 1000),
                new ItemStack(ModItems.RED_RAW_SUGAR.get(), 10),
                new FluidStack(FluidHelper.convertToStill(ModFluids.MOLASSES.get()), 400)),
            new CentrifugeRecipeDisplay(ItemStack.EMPTY,
                new FluidStack(FluidHelper.convertToStill(ModFluids.CALCIUM_SACCHARATE.get()), 1000),
                List.of(new ItemStack(ModItems.CALCIUM_SACCHARATE_PASTE.get(), 10)),
                new FluidStack(FluidHelper.convertToStill(ModFluids.WASTE_WATER.get()), 50))
        ));

        for (Holder<Potion> potion : shownPotions()) {
            FluidStack ordinary = potionFluid(potion, 1000);
            FluidStack concentrate = ConcentratedPotionFluid.concentrate(ordinary, 500);
            evaporation.add(new EvaporationRecipeDisplay(ordinary, concentrate, ItemStack.EMPTY));

            FluidStack fullConcentrate = ConcentratedPotionFluid.concentrate(ordinary, 1000);
            ItemStack essence = PotionEssenceResolver.outputFor(fullConcentrate, 6);
            if (!essence.isEmpty()) {
                centrifuge.add(new CentrifugeRecipeDisplay(ItemStack.EMPTY, fullConcentrate,
                    List.of(essence), new FluidStack(FluidHelper.convertToStill(ModFluids.WASTE_WATER.get()), 500)));
            }
        }

        registration.addRecipes(EvaporationBasinCategory.TYPE, evaporation);
        registration.addRecipes(CentrifugeCategory.TYPE, centrifuge);

        List<SeasoningRecipeDisplay> seasoning = new ArrayList<>();
        addSeasoningDisplays(seasoning, SeasoningRecipe.CAN_BE_SEASONED_SAVORY, List.of(
            new ItemStack(ModItems.SALT.get()), new ItemStack(ModItems.BITTER_SPICE.get()),
            new ItemStack(ModItems.BRIGHT_SPICE.get()), new ItemStack(ModItems.HEARTY_SPICE.get()),
            new ItemStack(ModItems.SPICY_SPICE.get()), new ItemStack(ModItems.SMOKY_SPICE.get()),
            new ItemStack(ModItems.UMAMI_SPICE.get()), new ItemStack(ModItems.FUNKY_SPICE.get()),
            new ItemStack(ModItems.FOUL_SPICE.get())
        ));
        addSeasoningDisplays(seasoning, SeasoningRecipe.CAN_BE_SEASONED_SWEET, List.of(
            new ItemStack(ModItems.ZESTY_SPICE.get()), new ItemStack(ModItems.MINTY_SPICE.get()),
            new ItemStack(ModItems.SWEET_SPICE.get()), new ItemStack(ModItems.FUNKY_SPICE.get()),
            new ItemStack(ModItems.FOUL_SPICE.get())
        ));
        registration.addRecipes(SeasoningCategory.TYPE, seasoning);
    }

    private static void addSeasoningDisplays(List<SeasoningRecipeDisplay> displays, TagKey<Item> tag,
                                              List<ItemStack> seasonings) {
        BuiltInRegistries.ITEM.getTag(tag).ifPresent(items -> items.forEach(holder -> {
            ItemStack food = new ItemStack(holder.value());
            for (ItemStack seasoning : seasonings) {
                String profile = SeasoningRecipe.profileFor(seasoning);
                displays.add(new SeasoningRecipeDisplay(seasoning, food,
                    SeasoningRecipe.applySeasoning(food, profile)));
            }
        }));
    }

    private static FluidStack potionFluid(Holder<Potion> potion, int amount) {
        FluidStack stack = PotionFluidHandler.getFluidFromPotionItem(
            PotionContents.createItemStack(Items.POTION, potion));
        return FluidHelper.copyStackWithAmount(stack, amount);
    }

    private static List<Holder<Potion>> shownPotions() {
        return List.of(
            Potions.NIGHT_VISION, Potions.SLOW_FALLING, Potions.INVISIBILITY, Potions.LEAPING,
            Potions.FIRE_RESISTANCE, Potions.SWIFTNESS, Potions.WATER_BREATHING, Potions.STRENGTH,
            Potions.REGENERATION, Potions.HEALING, Potions.POISON, Potions.WEAKNESS,
            Potions.SLOWNESS, Potions.HARMING
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.EVAPORATOR.get()), EvaporationBasinCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.CENTRIFUGE.get()), CentrifugeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.SALT.get()), SeasoningCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.BITTER_SPICE.get()), SeasoningCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.ZESTY_SPICE.get()), SeasoningCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.FUNKY_SPICE.get()), SeasoningCategory.TYPE);
    }
}
