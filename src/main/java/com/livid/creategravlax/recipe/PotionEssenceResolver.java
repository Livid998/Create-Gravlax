package com.livid.creategravlax.recipe;

import com.livid.creategravlax.registry.ModItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredItem;


public final class PotionEssenceResolver {
    private PotionEssenceResolver() {}

    public static ItemStack outputFor(FluidStack fluid, int count) {
        DeferredItem<Item> item = essenceFor(fluid);
        return item == null ? ItemStack.EMPTY : new ItemStack(item.get(), count);
    }

    public static DeferredItem<Item> essenceFor(FluidStack fluid) {
        PotionContents contents = fluid.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        for (MobEffectInstance instance : contents.getAllEffects()) {
            if (instance.getEffect().value() == MobEffects.NIGHT_VISION.value()) return ModItems.NIGHT_VISION_ESSENCE;
            if (instance.getEffect().value() == MobEffects.SLOW_FALLING.value()) return ModItems.SLOW_FALLING_ESSENCE;
            if (instance.getEffect().value() == MobEffects.INVISIBILITY.value()) return ModItems.INVISIBILITY_ESSENCE;
            if (instance.getEffect().value() == MobEffects.JUMP.value()) return ModItems.LEAPING_ESSENCE;
            if (instance.getEffect().value() == MobEffects.FIRE_RESISTANCE.value()) return ModItems.FIRE_RESISTANCE_ESSENCE;
            if (instance.getEffect().value() == MobEffects.MOVEMENT_SPEED.value()) return ModItems.SWIFTNESS_ESSENCE;
            if (instance.getEffect().value() == MobEffects.WATER_BREATHING.value()) return ModItems.WATER_BREATHING_ESSENCE;
            if (instance.getEffect().value() == MobEffects.DAMAGE_BOOST.value()) return ModItems.STRENGTH_ESSENCE;
            if (instance.getEffect().value() == MobEffects.REGENERATION.value()) return ModItems.REGENERATION_ESSENCE;
            if (instance.getEffect().value() == MobEffects.HEAL.value()) return ModItems.HEALING_ESSENCE;
            if (instance.getEffect().value() == MobEffects.POISON.value()) return ModItems.POISON_ESSENCE;
            if (instance.getEffect().value() == MobEffects.WEAKNESS.value()) return ModItems.WEAKNESS_ESSENCE;
            if (instance.getEffect().value() == MobEffects.MOVEMENT_SLOWDOWN.value()) return ModItems.SLOWNESS_ESSENCE;
            if (instance.getEffect().value() == MobEffects.HARM.value()) return ModItems.HARMING_ESSENCE;
            if (instance.getEffect().value() == MobEffects.BLINDNESS.value()) return ModItems.BLINDNESS_ESSENCE;
        }
        return null;
    }

    public static boolean hasSupportedEssence(FluidStack fluid) {
        return essenceFor(fluid) != null;
    }
}
