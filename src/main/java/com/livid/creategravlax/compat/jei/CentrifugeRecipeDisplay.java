package com.livid.creategravlax.compat.jei;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;


public record CentrifugeRecipeDisplay(
    ItemStack itemInput,
    FluidStack fluidInput,
    List<ItemStack> itemOutputs,
    FluidStack fluidOutput
) {
    public static CentrifugeRecipeDisplay fluid(
        FluidStack fluidInput,
        ItemStack itemOutput,
        FluidStack fluidOutput
    ) {
        return new CentrifugeRecipeDisplay(ItemStack.EMPTY, fluidInput, List.of(itemOutput), fluidOutput);
    }

    public static CentrifugeRecipeDisplay item(
        ItemStack itemInput,
        List<ItemStack> itemOutputs
    ) {
        return new CentrifugeRecipeDisplay(itemInput, FluidStack.EMPTY, itemOutputs, FluidStack.EMPTY);
    }
}
