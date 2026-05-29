package com.livid.creategravlax.compat.jei;

import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.registry.ModItems;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;


@ParametersAreNonnullByDefault
public final class SeasoningCategory implements IRecipeCategory<SeasoningRecipeDisplay> {
    public static final RecipeType<SeasoningRecipeDisplay> TYPE =
        RecipeType.create(CreateGravlax.MOD_ID, "seasoning", SeasoningRecipeDisplay.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SeasoningCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(132, 42);
        icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModItems.SALT.get())
        );
    }

    @Override
    public RecipeType<SeasoningRecipeDisplay> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("create_gravlax.jei.seasoning");
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
        SeasoningRecipeDisplay recipe,
        IFocusGroup focuses
    ) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 12)
            .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
            .addItemStack(recipe.seasoning());

        builder.addSlot(RecipeIngredientRole.INPUT, 40, 12)
            .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
            .addItemStack(recipe.food());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 12)
            .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
            .addItemStack(recipe.seasonedOutput());
    }

    @Override
    public void draw(
        SeasoningRecipeDisplay recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics graphics,
        double mouseX,
        double mouseY
    ) {
        graphics.drawString(Minecraft.getInstance().font, Component.literal("+"), 29, 17, 0x555555, false);
        graphics.drawString(Minecraft.getInstance().font, Component.literal("→"), 75, 17, 0x555555, false);
    }
}
