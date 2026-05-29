package com.livid.creategravlax.mixin;

import java.util.Optional;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;

import com.livid.creategravlax.block.entity.EvaporatorBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public abstract class BasinOperatingBlockEntityMixin {
    @Inject(method = "getBasin", at = @At("RETURN"), cancellable = true, remap = false)
    private void createGravlax$rejectEvaporationBasin(
        CallbackInfoReturnable<Optional<BasinBlockEntity>> cir
    ) {
        Optional<BasinBlockEntity> found = cir.getReturnValue();
        if (found.isPresent() && found.get() instanceof EvaporatorBlockEntity) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
