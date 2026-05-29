package com.livid.creategravlax.registry;

import java.util.function.Consumer;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.fluid.ConcentratedPotionFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;


public final class ModFluids {
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateGravlax.MOD_ID);

    private static final ResourceLocation WATER_STILL =
        ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
    private static final ResourceLocation WATER_FLOW =
        ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");

    
    public static final FluidEntry<ConcentratedPotionFluid> CONCENTRATED_POTION =
        REGISTRATE.virtualFluid("concentrated_potion", ConcentratedPotionFluid.Type::new,
                ConcentratedPotionFluid::createSource, ConcentratedPotionFluid::createFlowing)
            .lang("Concentrated Potion")
            .register();

    
    public static final FluidEntry<BaseFlowingFluid.Flowing> WASTE_WATER =
        REGISTRATE.standardFluid("waste_water", waterTintedFluid(0xB8465362))
            .lang("Waste Water")
            .properties(p -> p.density(1040).viscosity(1150))
            .fluidProperties(p -> p.tickRate(8).slopeFindDistance(4).levelDecreasePerBlock(1))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    
    public static final FluidEntry<BaseFlowingFluid.Flowing> LIME_WATER =
        REGISTRATE.standardFluid("lime_water", waterTintedFluid(0xB8D6DEC4))
            .lang("Lime Water")
            .properties(p -> p.density(1060).viscosity(1120))
            .fluidProperties(p -> p.tickRate(7).slopeFindDistance(4).levelDecreasePerBlock(1))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    
    public static final FluidEntry<BaseFlowingFluid.Flowing> SALT_WATER =
        REGISTRATE.standardFluid("salt_water", waterTintedFluid(0xA84E9FC4))
            .lang("Salt Water")
            .properties(p -> p.density(1030).viscosity(1050))
            .fluidProperties(p -> p.tickRate(5).slopeFindDistance(4).levelDecreasePerBlock(1))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> SUGAR_BEET_JUICE =
        REGISTRATE.standardFluid("sugar_beet_juice", texturedFluid("sugar_beet_juice", 0xDCFFFFFF))
            .lang("Sugar Beet Juice")
            .properties(p -> p.density(1050).viscosity(1100))
            .fluidProperties(p -> p.tickRate(15).slopeFindDistance(4).levelDecreasePerBlock(1))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> THICK_SUGAR_BEET_JUICE =
        REGISTRATE.standardFluid("thick_sugar_beet_juice", texturedFluid("thick_sugar_beet_juice", 0xEEFFFFFF))
            .lang("Thick Sugar Beet Juice")
            .properties(p -> p.density(1200).viscosity(2200))
            .fluidProperties(p -> p.tickRate(28).slopeFindDistance(2).levelDecreasePerBlock(2))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> RED_BEET_JUICE =
        REGISTRATE.standardFluid("red_beet_juice", texturedFluid("red_beet_juice", 0xDCFFFFFF))
            .lang("Red Beet Juice")
            .properties(p -> p.density(1050).viscosity(1100))
            .fluidProperties(p -> p.tickRate(15).slopeFindDistance(4).levelDecreasePerBlock(1))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> THICK_RED_BEET_JUICE =
        REGISTRATE.standardFluid("thick_red_beet_juice", texturedFluid("thick_red_beet_juice", 0xEEFFFFFF))
            .lang("Thick Red Beet Juice")
            .properties(p -> p.density(1200).viscosity(2200))
            .fluidProperties(p -> p.tickRate(28).slopeFindDistance(2).levelDecreasePerBlock(2))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> MOLASSES =
        REGISTRATE.standardFluid("molasses", texturedFluid("molasses", 0xF5FFFFFF))
            .lang("Molasses")
            .properties(p -> p.density(1400).viscosity(3200))
            .fluidProperties(p -> p.tickRate(35).slopeFindDistance(2).levelDecreasePerBlock(2))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> CALCIUM_SACCHARATE =
        REGISTRATE.standardFluid("calcium_saccharate", texturedFluid("calcium_saccharate", 0xDFFFFFFF))
            .lang("Calcium Saccharate")
            .properties(p -> p.density(1250).viscosity(2400))
            .fluidProperties(p -> p.tickRate(30).slopeFindDistance(2).levelDecreasePerBlock(2))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> ETHANOL =
        REGISTRATE.standardFluid("ethanol", texturedFluid("ethanol", 0xA8FFFFFF))
            .lang("Ethanol")
            .properties(p -> p.density(790).viscosity(850))
            .fluidProperties(p -> p.tickRate(8).slopeFindDistance(5).levelDecreasePerBlock(1))
            .source(BaseFlowingFluid.Source::new)
            .block().build()
            .bucket().build()
            .register();

    private ModFluids() {
    }

    @SuppressWarnings("removal")
    private static FluidBuilder.FluidTypeFactory texturedFluid(final String textureName, final int tintColor) {
        final ResourceLocation customStill =
            ResourceLocation.fromNamespaceAndPath(CreateGravlax.MOD_ID, "block/fluid/" + textureName + "_still");
        final ResourceLocation customFlow =
            ResourceLocation.fromNamespaceAndPath(CreateGravlax.MOD_ID, "block/fluid/" + textureName + "_flow");

        return (properties, stillTexture, flowingTexture) -> new FluidType(properties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return customStill;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return customFlow;
                    }

                    @Override
                    public int getTintColor(FluidStack stack) {
                        return tintColor;
                    }

                    @Override
                    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                        return tintColor;
                    }
                });
            }
        };
    }

    @SuppressWarnings("removal")
    private static FluidBuilder.FluidTypeFactory waterTintedFluid(final int tintColor) {
        return (properties, stillTexture, flowingTexture) -> new FluidType(properties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return WATER_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return WATER_FLOW;
                    }

                    @Override
                    public int getTintColor(FluidStack stack) {
                        return tintColor;
                    }

                    @Override
                    public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                        return tintColor;
                    }
                });
            }
        };
    }

    public static void register(IEventBus modBus) {
        REGISTRATE.registerEventListeners(modBus);
    }
}
