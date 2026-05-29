package com.livid.creategravlax.compat.jei;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;


public record EvaporationRecipeDisplay(
    FluidStack fluidInput,
    FluidStack fluidOutput,
    ItemStack itemOutput
) {
}
