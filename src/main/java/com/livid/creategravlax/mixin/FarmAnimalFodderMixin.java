package com.livid.creategravlax.mixin;

import com.livid.creategravlax.registry.ModItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin({Cow.class, Sheep.class, Pig.class, Goat.class, Chicken.class})
public abstract class FarmAnimalFodderMixin extends Animal {
    protected FarmAnimalFodderMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true, require = 0)
    private void createGravlax$acceptAnimalFodder(
        ItemStack stack,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (stack.is(ModItems.ANIMAL_FODDER.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "registerGoals", at = @At("TAIL"), require = 0)
    private void createGravlax$followAnimalFodder(CallbackInfo ci) {
        this.goalSelector.addGoal(
            3,
            new TemptGoal(this, 1.25D, Ingredient.of(ModItems.ANIMAL_FODDER.get()), false)
        );
    }
}
