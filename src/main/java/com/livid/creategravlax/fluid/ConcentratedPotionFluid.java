package com.livid.creategravlax.fluid;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.potion.PotionFluid.BottleType;

import com.livid.creategravlax.registry.ModFluids;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;


public final class ConcentratedPotionFluid extends PotionFluid {
    private static final net.minecraft.resources.ResourceLocation CREATE_POTION_STILL =
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("create", "fluid/potion_still");
    private static final net.minecraft.resources.ResourceLocation CREATE_POTION_FLOWING =
        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("create", "fluid/potion_flow");
    public static ConcentratedPotionFluid createSource(BaseFlowingFluid.Properties properties) {
        return new ConcentratedPotionFluid(properties, true);
    }

    public static ConcentratedPotionFluid createFlowing(BaseFlowingFluid.Properties properties) {
        return new ConcentratedPotionFluid(properties, false);
    }

    public ConcentratedPotionFluid(BaseFlowingFluid.Properties properties, boolean source) {
        super(properties, source);
    }

    public static FluidStack concentrate(FluidStack input, int amount) {
        FluidStack output = new FluidStack(ModFluids.CONCENTRATED_POTION.get().getSource(), amount);
        output.set(DataComponents.POTION_CONTENTS,
            input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
        output.set(AllDataComponents.POTION_FLUID_BOTTLE_TYPE,
            input.getOrDefault(AllDataComponents.POTION_FLUID_BOTTLE_TYPE, BottleType.REGULAR));
        return output;
    }

    public static final class Type extends PotionFluid.PotionFluidType {
        public Type(net.neoforged.neoforge.fluids.FluidType.Properties properties,
                    net.minecraft.resources.ResourceLocation ignoredStillTexture,
                    net.minecraft.resources.ResourceLocation ignoredFlowingTexture) {
            
            
            super(properties, CREATE_POTION_STILL, CREATE_POTION_FLOWING);
        }

        @Override
        public Component getDescription(FluidStack stack) {
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            if (contents == PotionContents.EMPTY) {
                return Component.translatable("fluid.create_gravlax.concentrated_potion");
            }
            ItemLike bottle = PotionFluidHandler.itemFromBottleType(
                stack.getOrDefault(AllDataComponents.POTION_FLUID_BOTTLE_TYPE, BottleType.REGULAR)
            );
            Component originalPotionName = Component.translatable(
                Potion.getName(contents.potion(), bottle.asItem().getDescriptionId() + ".effect.")
            );
            return Component.translatable(
                "fluid.create_gravlax.concentrated_potion_with_effect",
                originalPotionName
            );
        }

        @Override
        public int getTintColor(FluidStack stack) {
            int source = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor();
            int red = Math.round(((source >> 16) & 255) * 0.62f);
            int green = Math.round(((source >> 8) & 255) * 0.62f);
            int blue = Math.round((source & 255) * 0.62f);
            return 0xff000000 | (red << 16) | (green << 8) | blue;
        }
    }
}
