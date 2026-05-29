package com.livid.creategravlax.event;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.recipe.SeasoningRecipe;
import com.livid.creategravlax.registry.ModItems;

import java.util.ArrayList;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = CreateGravlax.MOD_ID)
public final class SeasonedFoodEvents {
    private SeasonedFoodEvents() {}

    @SubscribeEvent
    public static void onFoodFinished(LivingEntityUseItemEvent.Finish event) {
        ItemStack eaten = event.getItem();
        LivingEntity entity = event.getEntity();

        if (eaten.is(ModItems.MILK_BOTTLE.get())) {
            entity.removeAllEffects();
            return;
        }
        if (eaten.is(ModItems.MILKY_POTION.get())) {
            for (MobEffectInstance active : new ArrayList<>(entity.getActiveEffects())) {
                if (active.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                    entity.removeEffect(active.getEffect());
                }
            }
            return;
        }
        if (eaten.is(ModItems.ANTIDOTE.get())) {
            entity.removeEffect(MobEffects.POISON);
            return;
        }
        if (eaten.is(ModItems.ELIXIR.get())) {
            entity.removeEffect(MobEffects.WEAKNESS);
            return;
        }
        if (eaten.is(ModItems.TONIC.get())) {
            entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            return;
        }
        if (eaten.is(ModItems.EYE_DROPS.get())) {
            entity.removeEffect(MobEffects.BLINDNESS);
            return;
        }
        if (eaten.is(ModItems.GRAVLAX_SALMON.get()) || eaten.is(ModItems.GRAVLAX_SALMON_SLICE.get())) {
            entity.addEffect(new MobEffectInstance(MobEffects.LUCK, 1800, 0));
            if (entity instanceof Player player) player.getFoodData().eat(1, 0.2f);
            return;
        }

        String profile = SeasoningRecipe.seasoningOn(eaten);
        if (profile == null) return;

        switch (profile) {
            case "salty" -> {
                entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0));
                if (entity instanceof Player player) player.getFoodData().eat(1, 0.2f);
            }
            case "bitter" -> entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1800, 0));
            case "bright" -> entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 0));
            case "funky" -> entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 900, 0));
            case "hearty" -> entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 1200, 0));
            case "spicy" -> entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0));
            case "zesty" -> entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0));
            case "minty" -> entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1800, 0));
            case "smoky" -> entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 900, 0));
            case "sweet" -> entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 0));
            case "umami" -> entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0));
            case "foul" -> entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 0));
            default -> { }
        }
    }
}
