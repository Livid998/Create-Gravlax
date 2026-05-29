package com.livid.creategravlax.compat.jei;

import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.CreateLang;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.registry.ModItems;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.fluids.FluidStack;


@ParametersAreNonnullByDefault
public final class EvaporationBasinCategory implements IRecipeCategory<EvaporationRecipeDisplay> {
    public static final RecipeType<EvaporationRecipeDisplay> TYPE =
        RecipeType.create(CreateGravlax.MOD_ID, "evaporation_basin", EvaporationRecipeDisplay.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final AnimatedEvaporationBasin basin = new AnimatedEvaporationBasin();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    public EvaporationBasinCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(177, 103);
        this.icon = guiHelper.createDrawableIngredient(
            mezz.jei.api.constants.VanillaTypes.ITEM_STACK,
            new ItemStack(ModItems.EVAPORATOR.get())
        );
    }

    @Override
    public RecipeType<EvaporationRecipeDisplay> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("create_gravlax.jei.evaporation_basin");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    @SuppressWarnings("removal")
    public void setRecipe(
        IRecipeLayoutBuilder builder,
        EvaporationRecipeDisplay recipe,
        IFocusGroup focuses
    ) {
        FluidStack input = recipe.fluidInput();
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 51)
            .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
            .addIngredient(NeoForgeTypes.FLUID_STACK, input)
            .setFluidRenderer(input.getAmount(), false, 16, 16);

        if (!recipe.fluidOutput().isEmpty()) {
            FluidStack output = recipe.fluidOutput();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 51)
                .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK, output)
                .setFluidRenderer(output.getAmount(), false, 16, 16);
        }

        if (!recipe.itemOutput().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 51)
                .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                .addItemStack(recipe.itemOutput());
        }

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 81)
            .addItemStack(AllBlocks.BLAZE_BURNER.asStack());
    }

    @Override
    public void draw(
        EvaporationRecipeDisplay recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics graphics,
        double mouseX,
        double mouseY
    ) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 136, 32);
        AllGuiTextures.JEI_LIGHT.render(graphics, 81, 88);
        AllGuiTextures.JEI_HEAT_BAR.render(graphics, 4, 80);

        graphics.drawString(
            Minecraft.getInstance().font,
            CreateLang.translateDirect(HeatCondition.HEATED.getTranslationKey()),
            9,
            86,
            HeatCondition.HEATED.getColor(),
            false
        );

        heater.withHeat(HeatLevel.KINDLED).draw(graphics, getBackground().getWidth() / 2 + 3, 55);
        basin.draw(graphics, getBackground().getWidth() / 2 + 3, 34);
    }
}
