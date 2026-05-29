package com.livid.creategravlax.client;

import com.simibubi.create.foundation.item.ItemDescription;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.registry.ModItems;
import com.livid.creategravlax.recipe.SeasoningRecipe;

import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;


@EventBusSubscriber(modid = CreateGravlax.MOD_ID, value = Dist.CLIENT)
public final class ModItemTooltips {
    private ModItemTooltips() {
    }

    @SubscribeEvent
    public static void addMachineSummaries(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item == ModItems.EVAPORATOR.get() || item == ModItems.CENTRIFUGE.get()) {
            new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE).modify(event);
        }

        if (item == ModItems.GRAVLAX_SALMON.get() || item == ModItems.GRAVLAX_SALMON_SLICE.get()) {
            event.getToolTip().add(Component.translatable(
                "tooltip.create_gravlax.seasoned",
                Component.translatable("tooltip.create_gravlax.seasoning.sweet_sour")
            ).withStyle(ChatFormatting.AQUA));
        }

        if (item == ModItems.MILK_BOTTLE.get()) {
            event.getToolTip().add(Component.translatable(
                "tooltip.create_gravlax.milk_bottle.effect"
            ).withStyle(ChatFormatting.GRAY));
        }

        if (item == ModItems.MILKY_POTION.get()) {
            event.getToolTip().add(Component.translatable(
                "tooltip.create_gravlax.milky_potion.effect"
            ).withStyle(ChatFormatting.GRAY));
        }

        CustomData customData = event.getItemStack().get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }

        CompoundTag tag = customData.copyTag();
        String profile = tag.getString(SeasoningRecipe.SEASONING_KEY);
        if (profile.isEmpty()) {
            return;
        }

        event.getToolTip().add(Component.translatable(
            "tooltip.create_gravlax.seasoned",
            Component.translatable("tooltip.create_gravlax.seasoning." + profile)
        ).withStyle(ChatFormatting.AQUA));
    }
}
