package exterminatorjeff.undergroundbiomes.core;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant;
import exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant;
import exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.common.ButtonRecipe;
import exterminatorjeff.undergroundbiomes.common.RegularStoneRecipe;
import exterminatorjeff.undergroundbiomes.common.UBFuelHandler;
import exterminatorjeff.undergroundbiomes.common.block.IgneousBrick;
import exterminatorjeff.undergroundbiomes.common.block.IgneousCobble;
import exterminatorjeff.undergroundbiomes.common.block.IgneousStone;
import exterminatorjeff.undergroundbiomes.common.block.MetamorphicBrick;
import exterminatorjeff.undergroundbiomes.common.block.MetamorphicCobble;
import exterminatorjeff.undergroundbiomes.common.block.MetamorphicStone;
import exterminatorjeff.undergroundbiomes.common.block.SedimentaryStone;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonIgneous;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonIgneousBrick;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonIgneousCobble;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonMetamorphic;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonMetamorphicBrick;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonMetamorphicCobble;
import exterminatorjeff.undergroundbiomes.common.block.button.UBButtonSedimentary;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBIgneousBrickSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBIgneousBrickSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBIgneousCobbleSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBIgneousCobbleSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBIgneousStoneSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBIgneousStoneSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBMetamorphicBrickSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBMetamorphicBrickSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBMetamorphicCobbleSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBMetamorphicCobbleSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBMetamorphicStoneSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBMetamorphicStoneSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBSedimentaryStoneSlabDouble;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBSedimentaryStoneSlabHalf;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsIgneous;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsIgneousBrick;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsIgneousCobble;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsMetamorphic;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsMetamorphicBrick;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsMetamorphicCobble;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStairsSedimentary;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallIgneous;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallIgneousBrick;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallIgneousCobble;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallMetamorphic;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallMetamorphicBrick;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallMetamorphicCobble;
import exterminatorjeff.undergroundbiomes.common.block.wall.UBWallSedimentary;
import exterminatorjeff.undergroundbiomes.common.item.ItemFossilPiece;
import exterminatorjeff.undergroundbiomes.common.item.ItemLigniteCoal;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.SlabItemBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.StairsItemBlock;
import exterminatorjeff.undergroundbiomes.config.ConfigManager;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.intermod.DropsRegistry;
import exterminatorjeff.undergroundbiomes.intermod.OresRegistry;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;
import java.io.File;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class CommonProxy {
    private ConfigManager configManager;
    private DimensionManager dimensionManager;
        
	public void preInit(FMLPreInitializationEvent event) {
		API.STONES_REGISTRY = StonesRegistry.INSTANCE;
		API.ORES_REGISTRY = OresRegistry.INSTANCE;
		API.DROPS_REGISTRY = DropsRegistry.INSTANCE;
		API.SETTINGS = UBConfig.SPECIFIC;

                configManager = new ConfigManager(event);
                dimensionManager = new DimensionManager(configManager);
                API.STRATA_COLUMN_PROVIDER = dimensionManager;

		createBlocks();
		createItems();
		createOres();

		DropsRegistry.INSTANCE.init();

		OresRegistry.INSTANCE.fulfillRequests();
	}

	public void init(FMLInitializationEvent e) {
		createRecipes();
		GameRegistry.registerFuelHandler(UBFuelHandler.INSTANCE);
	}

	public void postInit(FMLPostInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(dimensionManager);
	}
        
        public void serverLoad(FMLServerAboutToStartEvent event) {
            //logger.info("server starting");
            File worldSaveDirectory = null;
            String worldName = event.getServer().getFolderName();
            if (event.getServer().isSinglePlayer()) {
                File saveDirectory = event.getServer().getFile("saves");
                worldSaveDirectory = new File(saveDirectory,worldName);
            } else {
                PropertyManager settings = new PropertyManager(event.getServer().getFile("server.properties"));
                worldName = settings.getStringProperty("level-name", worldName);
                worldSaveDirectory = event.getServer().getFile(worldName);
            }
            try {
                WorldServer server = event.getServer().worldServerForDimension(0);
                File worldLocation = server.getChunkSaveLocation();
                //UndergroundBiomes.logger.info(world.toString() + " " +worldLocation.getAbsolutePath());
                configManager.setWorldFile(worldLocation);
            } catch (NullPointerException e) {
                throw e;
            }    
            dimensionManager.refreshManagers();
        }
        
        public void onServerStopped(FMLServerStoppedEvent event) {
            // for some reason onWorldLoad is running before any of the ServerStartxxx events
            // so I'm having to use a clunky workaround.
            dimensionManager.clearWorldManagers();
        }
        
	private final void createBlocks() {
		/*
		 * Blocks
		 */

		API.IGNEOUS_STONE.register(new IgneousStone());
		API.IGNEOUS_COBBLE.register(new IgneousCobble());
		API.IGNEOUS_BRICK.register(new IgneousBrick());
		API.METAMORPHIC_STONE.register(new MetamorphicStone());
		API.METAMORPHIC_COBBLE.register(new MetamorphicCobble());
		API.METAMORPHIC_BRICK.register(new MetamorphicBrick());
		API.SEDIMENTARY_STONE.register(new SedimentaryStone());

		/*
		 * Slabs
		 */

		API.IGNEOUS_BRICK_SLAB.register(new SlabItemBlock(new UBIgneousBrickSlabHalf(), new UBIgneousBrickSlabDouble()));
		API.METAMORPHIC_BRICK_SLAB.register(new SlabItemBlock(new UBMetamorphicBrickSlabHalf(), new UBMetamorphicBrickSlabDouble()));
		API.IGNEOUS_STONE_SLAB.register(new SlabItemBlock(new UBIgneousStoneSlabHalf(), new UBIgneousStoneSlabDouble()));
		API.METAMORPHIC_STONE_SLAB.register(new SlabItemBlock(new UBMetamorphicStoneSlabHalf(), new UBMetamorphicStoneSlabDouble()));
		API.IGNEOUS_COBBLE_SLAB.register(new SlabItemBlock(new UBIgneousCobbleSlabHalf(), new UBIgneousCobbleSlabDouble()));
		API.METAMORPHIC_COBBLE_SLAB.register(new SlabItemBlock(new UBMetamorphicCobbleSlabHalf(), new UBMetamorphicCobbleSlabDouble()));
		API.SEDIMENTARY_STONE_SLAB.register(new SlabItemBlock(new UBSedimentaryStoneSlabHalf(), new UBSedimentaryStoneSlabDouble()));

		/*
		 * Buttons
		 */

		if (UBConfig.SPECIFIC.buttonsOn()) {
			if (UBConfig.SPECIFIC.igneousButtonsOn()) {
				if (UBConfig.SPECIFIC.stoneButtonsOn())
					API.IGNEOUS_STONE_BUTTON.register(new ButtonItemBlock(API.IGNEOUS_STONE, UBButtonIgneous.class));
				if (UBConfig.SPECIFIC.cobbleButtonsOn())
					API.IGNEOUS_COBBLE_BUTTON.register(new ButtonItemBlock(API.IGNEOUS_COBBLE, UBButtonIgneousCobble.class));
				if (UBConfig.SPECIFIC.brickButtonsOn())
					API.IGNEOUS_BRICK_BUTTON.register(new ButtonItemBlock(API.IGNEOUS_BRICK, UBButtonIgneousBrick.class));
			}
			if (UBConfig.SPECIFIC.metamorphicButtonsOn()) {
				if (UBConfig.SPECIFIC.stoneButtonsOn())
					API.METAMORPHIC_STONE_BUTTON.register(new ButtonItemBlock(API.METAMORPHIC_STONE, UBButtonMetamorphic.class));
				if (UBConfig.SPECIFIC.cobbleButtonsOn())
					API.METAMORPHIC_COBBLE_BUTTON.register(new ButtonItemBlock(API.METAMORPHIC_COBBLE, UBButtonMetamorphicCobble.class));
				if (UBConfig.SPECIFIC.brickButtonsOn())
					API.METAMORPHIC_BRICK_BUTTON.register(new ButtonItemBlock(API.METAMORPHIC_BRICK, UBButtonMetamorphicBrick.class));
			}
			if (UBConfig.SPECIFIC.sedimentaryButtonsOn())
				API.SEDIMENTARY_STONE_BUTTON.register(new ButtonItemBlock(API.SEDIMENTARY_STONE, UBButtonSedimentary.class));
		}

		/*
		 * Walls
		 */

		if (UBConfig.SPECIFIC.wallsOn()) {
			if (UBConfig.SPECIFIC.igneousWallsOn()) {
				if (UBConfig.SPECIFIC.stoneWallsOn())
					API.IGNEOUS_STONE_WALL.register(new UBWallIgneous(API.IGNEOUS_STONE));
				if (UBConfig.SPECIFIC.cobbleWallsOn())
					API.IGNEOUS_COBBLE_WALL.register(new UBWallIgneousCobble(API.IGNEOUS_COBBLE));
				if (UBConfig.SPECIFIC.brickWallsOn())
					API.IGNEOUS_BRICK_WALL.register(new UBWallIgneousBrick(API.IGNEOUS_BRICK));
			}
			if (UBConfig.SPECIFIC.metamorphicWallsOn()) {
				if (UBConfig.SPECIFIC.stoneWallsOn())
					API.METAMORPHIC_STONE_WALL.register(new UBWallMetamorphic(API.METAMORPHIC_STONE));
				if (UBConfig.SPECIFIC.cobbleWallsOn())
					API.METAMORPHIC_COBBLE_WALL.register(new UBWallMetamorphicCobble(API.METAMORPHIC_COBBLE));
				if (UBConfig.SPECIFIC.brickWallsOn())
					API.METAMORPHIC_BRICK_WALL.register(new UBWallMetamorphicBrick(API.METAMORPHIC_BRICK));
			}
			if (UBConfig.SPECIFIC.sedimentaryWallsOn())
				API.SEDIMENTARY_STONE_WALL.register(new UBWallSedimentary(API.SEDIMENTARY_STONE));
		}

		/*
		 * Stairs
		 */

		if (UBConfig.SPECIFIC.stairsOn()) {
			if (UBConfig.SPECIFIC.igneousStairsOn()) {
				if (UBConfig.SPECIFIC.stoneStairsOn())
					API.IGNEOUS_STONE_STAIRS.register(new StairsItemBlock(API.IGNEOUS_STONE, UBStairsIgneous.class));
				if (UBConfig.SPECIFIC.cobbleStairsOn())
					API.IGNEOUS_COBBLE_STAIRS.register(new StairsItemBlock(API.IGNEOUS_COBBLE, UBStairsIgneousCobble.class));
				if (UBConfig.SPECIFIC.brickStairsOn())
					API.IGNEOUS_BRICK_STAIRS.register(new StairsItemBlock(API.IGNEOUS_BRICK, UBStairsIgneousBrick.class));
			}
			if (UBConfig.SPECIFIC.metamorphicStairsOn()) {
				if (UBConfig.SPECIFIC.stoneStairsOn())
					API.METAMORPHIC_STONE_STAIRS.register(new StairsItemBlock(API.METAMORPHIC_STONE, UBStairsMetamorphic.class));
				if (UBConfig.SPECIFIC.cobbleStairsOn())
					API.METAMORPHIC_COBBLE_STAIRS.register(new StairsItemBlock(API.METAMORPHIC_COBBLE, UBStairsMetamorphicCobble.class));
				if (UBConfig.SPECIFIC.brickStairsOn())
					API.METAMORPHIC_BRICK_STAIRS.register(new StairsItemBlock(API.METAMORPHIC_BRICK, UBStairsMetamorphicBrick.class));
			}
			if (UBConfig.SPECIFIC.sedimentaryStairsOn())
				API.SEDIMENTARY_STONE_STAIRS.register(new StairsItemBlock(API.SEDIMENTARY_STONE, UBStairsSedimentary.class));
		}
	}

	private final void createItems() {
		API.LIGNITE_COAL.register(new ItemLigniteCoal());
		API.FOSSIL_PIECE.register(new ItemFossilPiece());
	}

	private final void createOres() {
		OresRegistry.INSTANCE.requestOreSetup(Blocks.DIAMOND_ORE);
		OresRegistry.INSTANCE.requestOreSetup(Blocks.GOLD_ORE);
		OresRegistry.INSTANCE.requestOreSetup(Blocks.IRON_ORE);
		OresRegistry.INSTANCE.requestOreSetup(Blocks.COAL_ORE);
		OresRegistry.INSTANCE.requestOreSetup(Blocks.EMERALD_ORE);
		OresRegistry.INSTANCE.requestOreSetup(Blocks.REDSTONE_ORE);
		OresRegistry.INSTANCE.requestOreSetup(Blocks.LAPIS_ORE);
	}

	private final void createRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(Items.COAL), "XXX", "XXX", "XXX", 'X', API.LIGNITE_COAL.getItem());
		GameRegistry.addShapelessRecipe(new ItemStack(Items.DYE, 1, 15), new ItemStack(API.FOSSIL_PIECE.getItem(), 1, WILDCARD_VALUE));

		for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
			GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 4, i), "XX", "XX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
			GameRegistry.addSmelting(new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i), new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i), 0.1f);
		}
		for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
			GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 4, i), "XX", "XX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
			GameRegistry.addSmelting(new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i), new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i), 0.1f);
		}

		/*
		 * Slabs
		 */

		for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
			GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_STONE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
			GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_COBBLE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
			GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
		}
		for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
			GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_STONE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
			GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_COBBLE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
			GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
		}
		for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
			GameRegistry.addShapedRecipe(new ItemStack(API.SEDIMENTARY_STONE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));

		/*
		 * Buttons
		 */

		int n = UBConfig.SPECIFIC.buttonRecipeResult();

		if (UBConfig.SPECIFIC.buttonsOn()) {
			if (UBConfig.SPECIFIC.igneousButtonsOn())
				for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
					if (UBConfig.SPECIFIC.stoneButtonsOn())
						GameRegistry.addShapelessRecipe(new ItemStack(API.IGNEOUS_STONE_BUTTON.getItemBlock(), n, i), new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.cobbleButtonsOn())
						GameRegistry.addShapelessRecipe(new ItemStack(API.IGNEOUS_COBBLE_BUTTON.getItemBlock(), n, i), new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.brickButtonsOn())
						GameRegistry.addShapelessRecipe(new ItemStack(API.IGNEOUS_BRICK_BUTTON.getItemBlock(), n, i), new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
				}
			if (UBConfig.SPECIFIC.metamorphicButtonsOn())
				for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
					if (UBConfig.SPECIFIC.stoneButtonsOn())
						GameRegistry.addShapelessRecipe(new ItemStack(API.METAMORPHIC_STONE_BUTTON.getItemBlock(), n, i), new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.cobbleButtonsOn())
						GameRegistry.addShapelessRecipe(new ItemStack(API.METAMORPHIC_COBBLE_BUTTON.getItemBlock(), n, i), new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.brickButtonsOn())
						GameRegistry.addShapelessRecipe(new ItemStack(API.METAMORPHIC_BRICK_BUTTON.getItemBlock(), n, i), new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
				}
			if (UBConfig.SPECIFIC.sedimentaryButtonsOn())
				for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
					GameRegistry.addShapelessRecipe(new ItemStack(API.SEDIMENTARY_STONE_BUTTON.getItemBlock(), n, i), new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
		}

		/*
		 * Walls
		 */

		if (UBConfig.SPECIFIC.wallsOn()) {
			if (UBConfig.SPECIFIC.igneousWallsOn())
				for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
					if (UBConfig.SPECIFIC.stoneWallsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_STONE_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.cobbleWallsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_COBBLE_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.brickWallsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
				}
			if (UBConfig.SPECIFIC.metamorphicWallsOn())
				for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
					if (UBConfig.SPECIFIC.stoneWallsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_STONE_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.cobbleWallsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_COBBLE_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.brickWallsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
				}
			if (UBConfig.SPECIFIC.sedimentaryWallsOn())
				for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
					GameRegistry.addShapedRecipe(new ItemStack(API.SEDIMENTARY_STONE_WALL.getItemBlock(), 1, i), "XXX", "XXX", 'X', new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
		}

		/*
		 * Stairs
		 */

		if (UBConfig.SPECIFIC.stairsOn()) {
			if (UBConfig.SPECIFIC.igneousStairsOn())
				for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
					if (UBConfig.SPECIFIC.stoneStairsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_STONE_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.cobbleStairsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_COBBLE_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.brickStairsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
				}
			if (UBConfig.SPECIFIC.metamorphicStairsOn())
				for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
					if (UBConfig.SPECIFIC.stoneStairsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_STONE_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.cobbleStairsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_COBBLE_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
					if (UBConfig.SPECIFIC.brickStairsOn())
						GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
				}
			if (UBConfig.SPECIFIC.sedimentaryStairsOn())
				for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
					GameRegistry.addShapedRecipe(new ItemStack(API.SEDIMENTARY_STONE_STAIRS.getItemBlock(), 1, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
		}

		/*
		 * 
		 */

		if (UBConfig.SPECIFIC.ubifyRecipes()) {
			StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(cobbleEntry -> {
				Item cobble = cobbleEntry.getItemBlock();
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.FURNACE), "XXX", "X X", "XXX", 'X', cobble);
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.LEVER), "I", "X", 'X', cobble, 'I', Items.STICK);
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.PISTON), "WWW", "CIC", "CRC", 'W', Blocks.PLANKS, 'C', cobble, 'I', Items.IRON_INGOT, 'R', Items.REDSTONE);
				GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_AXE), "XX ", "XW ", " W ", 'X', cobble, 'W', Items.STICK);
				GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_PICKAXE), "XXX", " W ", " W ", 'X', cobble, 'W', Items.STICK);
				GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_HOE), "XX ", " W ", " W ", 'X', cobble, 'W', Items.STICK);
				GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_SHOVEL), " X ", " W ", " W ", 'X', cobble, 'W', Items.STICK);
				GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_SWORD), "X", "X", "W", 'X', cobble, 'W', Items.STICK);
				GameRegistry.addShapedRecipe(new ItemStack(Items.BREWING_STAND), " B ", "XXX", 'X', cobble, 'B', Items.BLAZE_ROD);
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.DISPENSER), "XXX", "XBX", "XRX", 'X', cobble, 'B', Items.BOW, 'R', Items.REDSTONE);
			});
			StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.STONE).forEach(stoneEntry -> {
				Item stone = stoneEntry.getItemBlock();
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE_PRESSURE_PLATE), "XX", 'X', stone);
				GameRegistry.addShapedRecipe(new ItemStack(Items.REPEATER), "TRT", "XXX", 'X', stone, 'T', Blocks.REDSTONE_TORCH, 'R', Items.REDSTONE);
			});
		}

		if (!UBConfig.SPECIFIC.buttonsOn() && UBConfig.SPECIFIC.ubifyRecipes())
			StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.STONE).forEach(stoneEntry -> {
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.STONE_BUTTON), stoneEntry.getItemBlock());
			});
		if (!UBConfig.SPECIFIC.wallsOn() && UBConfig.SPECIFIC.ubifyRecipes())
			StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(cobbleEntry -> {
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.COBBLESTONE_WALL, 6), "XXX", "XXX", 'X', cobbleEntry.getItemBlock());
			});
		if (!UBConfig.SPECIFIC.stairsOn() && UBConfig.SPECIFIC.ubifyRecipes()) {
			StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(cobbleEntry -> {
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE_STAIRS, 4), "X  ", "XX ", "XXX", 'X', cobbleEntry.getItemBlock());
			});
			StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.BRICK).forEach(brickEntry -> {
				GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE_BRICK_STAIRS, 4), "X  ", "XX ", "XXX", 'X', brickEntry.getItemBlock());
			});
		}

		((UBConfig)(UBConfig.SPECIFIC)).regularStoneCrafting.addTrackerAndUpdate(new RegularStoneRecipe());
		((UBConfig)(UBConfig.SPECIFIC)).changeButtonRecipe.addTrackerAndUpdate(new ButtonRecipe());
	}

}