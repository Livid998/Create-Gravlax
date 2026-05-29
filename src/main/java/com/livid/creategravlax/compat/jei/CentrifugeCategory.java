package com.livid.creategravlax.compat.jei;

import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.block.entity.CentrifugeBlockEntity;
import com.livid.creategravlax.registry.ModItems;

import mezz.jei.api.constants.VanillaTypes;
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
public final class CentrifugeCategory implements IRecipeCategory<CentrifugeRecipeDisplay> {
    public static final RecipeType<CentrifugeRecipeDisplay> TYPE =
        RecipeType.create(CreateGravlax.MOD_ID, "centrifuge", CentrifugeRecipeDisplay.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final AnimatedCentrifuge centrifuge = new AnimatedCentrifuge();

    public CentrifugeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(177, 88);
        this.icon = guiHelper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            new ItemStack(ModItems.CENTRIFUGE.get())
        );
    }

    @Override
    public RecipeType<CentrifugeRecipeDisplay> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("create_gravlax.jei.centrifuge");
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
        CentrifugeRecipeDisplay recipe,
        IFocusGroup focuses
    ) {
        if (!recipe.itemInput().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 42)
                .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                .addItemStack(recipe.itemInput());
        } else {
            FluidStack input = recipe.fluidInput();
            builder.addSlot(RecipeIngredientRole.INPUT, 27, 42)
                .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK, input)
                .setFluidRenderer(input.getAmount(), false, 16, 16);
        }

        int index = 0;
        for (ItemStack output : recipe.itemOutputs()) {
            int x = index == 0 ? 132 : 151;
            int y = index == 0 ? 33 : 52;
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                .addItemStack(output);
            index++;
        }

        if (!recipe.fluidOutput().isEmpty()) {
            FluidStack fluidOutput = recipe.fluidOutput();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 151, 52)
                .setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK, fluidOutput)
                .setFluidRenderer(fluidOutput.getAmount(), false, 16, 16);
        }
    }

    @Override
    public void draw(
        CentrifugeRecipeDisplay recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics graphics,
        double mouseX,
        double mouseY
    ) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 136, 14);
        AllGuiTextures.JEI_SHADOW.render(graphics, 81, 55);
        centrifuge.draw(graphics, getBackground().getWidth() / 2 + 3, 33);
        graphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable("create_gravlax.jei.minimum_rpm",
                (int) CentrifugeBlockEntity.MINIMUM_OPERATIONAL_RPM),
            8, 73, 0x555555, false
        );
    }
}
