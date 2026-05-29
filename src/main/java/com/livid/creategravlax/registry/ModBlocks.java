package com.livid.creategravlax.registry;

import com.livid.creategravlax.CreateGravlax;
import com.livid.creategravlax.block.BuddingSaltBlock;
import com.livid.creategravlax.block.CentrifugeBlock;
import com.livid.creategravlax.block.EvaporatorBlock;
import com.livid.creategravlax.block.SaltBlock;
import com.livid.creategravlax.block.SugarBeetCropBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateGravlax.MOD_ID);

    public static final DeferredBlock<SugarBeetCropBlock> SUGAR_BEET_CROP = BLOCKS.register(
        "sugar_beet_crop", () -> new SugarBeetCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
            .noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<EvaporatorBlock> EVAPORATOR = BLOCKS.register(
        "evaporator", () -> new EvaporatorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
            .strength(2.5f).sound(SoundType.COPPER)));

    public static final DeferredBlock<CentrifugeBlock> CENTRIFUGE = BLOCKS.register(
        "centrifuge", () -> new CentrifugeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
            .strength(3.0f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ROCK_SALT_ORE = BLOCKS.register(
        "rock_salt_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
            .strength(3.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> DEEPSLATE_ROCK_SALT_ORE = BLOCKS.register(
        "deepslate_rock_salt_ore", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .strength(4.5f, 3.0f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));
    public static final DeferredBlock<Block> RAW_ROCK_SALT_BLOCK = BLOCKS.register(
        "raw_rock_salt_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final DeferredBlock<SaltBlock> SALT_BLOCK = BLOCKS.register(
        "salt_block", () -> new SaltBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .strength(0.6f).randomTicks().sound(SoundType.CALCITE)));
    public static final DeferredBlock<BuddingSaltBlock> BUDDING_SALT_BLOCK = BLOCKS.register(
        "budding_salt_block", () -> new BuddingSaltBlock(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .strength(1.5f).randomTicks().requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
    public static final DeferredBlock<AmethystClusterBlock> SMALL_SALT_BUD = BLOCKS.register(
        "small_salt_bud", () -> new AmethystClusterBlock(3, 4, BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .noOcclusion().instabreak().sound(SoundType.SMALL_AMETHYST_BUD).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<AmethystClusterBlock> MEDIUM_SALT_BUD = BLOCKS.register(
        "medium_salt_bud", () -> new AmethystClusterBlock(4, 3, BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .noOcclusion().instabreak().sound(SoundType.MEDIUM_AMETHYST_BUD).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<AmethystClusterBlock> LARGE_SALT_BUD = BLOCKS.register(
        "large_salt_bud", () -> new AmethystClusterBlock(5, 3, BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .noOcclusion().instabreak().sound(SoundType.LARGE_AMETHYST_BUD).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<AmethystClusterBlock> SALT_CLUSTER = BLOCKS.register(
        "salt_cluster", () -> new AmethystClusterBlock(7, 3, BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .noOcclusion().instabreak().sound(SoundType.AMETHYST_CLUSTER).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> SALT_LAMP = BLOCKS.register(
        "salt_lamp", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
            .strength(0.7f).lightLevel(state -> 13).sound(SoundType.GLASS)));

    public static final DeferredBlock<Block> SUGAR_BLOCK = BLOCKS.register(
        "sugar_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ)
            .strength(0.5f).sound(SoundType.SAND)));

    public static final DeferredBlock<Block> SUGAR_BEET_CRATE = BLOCKS.register(
        "sugar_beet_crate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD)
            .strength(2.0f, 3.0f).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> BEETROOT_CRATE = BLOCKS.register(
        "beetroot_crate", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)
            .strength(2.0f, 3.0f).sound(SoundType.WOOD)));

    private ModBlocks() {}
    public static void register(IEventBus modBus) { BLOCKS.register(modBus); }
}
