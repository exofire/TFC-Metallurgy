package tfc_metallurgy.common.blocks;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.TFCBellBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tfc_metallurgy.TFCMetallurgy;
import tfc_metallurgy.common.block_entities.MetallurgyBlockEntities;
import tfc_metallurgy.common.blocks.rock.MetallurgyOre;
import tfc_metallurgy.common.fluids.MetallurgyFluids;
import tfc_metallurgy.common.items.MetallurgyItems;
import tfc_metallurgy.util.MetallurgyMetal;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class MetallurgyBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TFCMetallurgy.mod_id);

    public static final Map<Rock, Map<MetallurgyOre, RegistryObject<Block>>> ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(MetallurgyOre.class, ore -> !ore.isGraded(), ore ->
                    register(("ore/" + ore.name().toLowerCase() + "/" + rock.name().toLowerCase()), () -> ore.create(rock))
            )
    );

    public static final Map<Rock, Map<MetallurgyOre, Map<Ore.Grade, RegistryObject<Block>>>> GRADED_ORES = Helpers.mapOfKeys(Rock.class, rock ->
            Helpers.mapOfKeys(MetallurgyOre.class, MetallurgyOre::isGraded, ore ->
                    Helpers.mapOfKeys(Ore.Grade.class, grade ->
                            register(("ore/" + grade.name().toLowerCase() + "_" + ore.name().toLowerCase() + "/" + rock.name().toLowerCase()), () -> ore.create(rock))
                    )
            )
    );

    public static final Map<MetallurgyOre, RegistryObject<Block>> SMALL_ORES = Helpers.mapOfKeys(MetallurgyOre.class, MetallurgyOre::isGraded, type ->
            register(("ore/small_" + type.name().toLowerCase()), () -> GroundcoverBlock.looseOre(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).strength(0.05F, 0.0F).sound(SoundType.NETHER_ORE).noCollission()))
    );

    public static final Map<MetallurgyMetal, Map<MetallurgyMetal.BlockType, RegistryObject<Block>>> METALS = Helpers.mapOfKeys(MetallurgyMetal.class, metal ->
            Helpers.mapOfKeys(MetallurgyMetal.BlockType.class, type -> type.has(metal), type ->
                    register(("metal/" + type.name() + "/" + metal.name()), type.create(metal), type.createBlockItem(new Item.Properties()))
            )
    );

    public static final RegistryObject<Block> BERYLLIUM_COPPER_BELL = register("beryllium_copper_bell", () -> new TFCBellBlock(ExtendedProperties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.ANVIL).blockEntity(MetallurgyBlockEntities.BELL).ticks(BellBlockEntity::serverTick, BellBlockEntity::clientTick), 0.8f, "beryllium_copper"));
    public static final RegistryObject<Block> FLORENTINE_BRONZE_BELL = register("florentine_bronze_bell", () -> new TFCBellBlock(ExtendedProperties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.ANVIL).blockEntity(MetallurgyBlockEntities.BELL).ticks(BellBlockEntity::serverTick, BellBlockEntity::clientTick), 0.8f, "florentine_bronze"));
    public static final RegistryObject<Block> ENDERIUM_BARS = register("enderium_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6.0F, 7.0F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> TITANIUM_BARS = register("titanium_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6.0F, 7.0F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> TUNGSTEN_BARS = register("tungsten_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6.0F, 7.0F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> TUNGSTEN_STEEL_BARS = register("tungsten_steel_bars", () -> new IronBarsBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6.0F, 7.0F).sound(SoundType.METAL).noOcclusion()));

    public static final Map<MetallurgyMetal, RegistryObject<LiquidBlock>> METAL_FLUIDS = Helpers.mapOfKeys(MetallurgyMetal.class, metal ->
            register("metal/fluid/" + metal.name(), () -> new LiquidBlock(MetallurgyFluids.METALS.get(metal).source(), BlockBehaviour.Properties.of().noCollission().strength(100f).lightLevel(state -> 15)))//todo .noDrops()))
    );

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(BLOCKS, MetallurgyItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}

