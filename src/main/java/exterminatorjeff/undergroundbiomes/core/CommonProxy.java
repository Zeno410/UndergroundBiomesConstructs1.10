package exterminatorjeff.undergroundbiomes.core;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant;
import exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant;
import exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.names.Entry;
import exterminatorjeff.undergroundbiomes.common.ButtonRecipe;
import exterminatorjeff.undergroundbiomes.common.RegularStoneRecipe;
import exterminatorjeff.undergroundbiomes.common.UBFuelHandler;
import exterminatorjeff.undergroundbiomes.common.block.*;
import exterminatorjeff.undergroundbiomes.common.block.button.*;
import exterminatorjeff.undergroundbiomes.common.block.slab.*;
import exterminatorjeff.undergroundbiomes.common.block.stairs.*;
import exterminatorjeff.undergroundbiomes.common.block.wall.*;
import exterminatorjeff.undergroundbiomes.common.item.ItemFossilPiece;
import exterminatorjeff.undergroundbiomes.common.item.ItemLigniteCoal;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.SlabItemBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.StairsItemBlock;
import exterminatorjeff.undergroundbiomes.config.ConfigManager;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.intermod.DropsRegistry;
import exterminatorjeff.undergroundbiomes.intermod.IC2Registrar;
import exterminatorjeff.undergroundbiomes.intermod.OresRegistry;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.ArrayList;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

import exterminatorjeff.undergroundbiomes.intermod.IC2Registrar;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * @author CurtisA, LouisDB
 */
public class CommonProxy {
  private ConfigManager configManager;
  private DimensionManager dimensionManager;
  protected static final UBLogger LOGGER = new UBLogger(CommonProxy.class, Level.INFO);

  public void preInit(FMLPreInitializationEvent event) {
    LOGGER.debug("Start preInit");
    API.STONES_REGISTRY = StonesRegistry.INSTANCE;
    API.ORES_REGISTRY = OresRegistry.INSTANCE;
    API.DROPS_REGISTRY = DropsRegistry.INSTANCE;
    API.SETTINGS = UBConfig.SPECIFIC;

    configManager = new ConfigManager(event);
    dimensionManager = new DimensionManager(configManager);
    API.STRATA_COLUMN_PROVIDER = dimensionManager;

//    createBlocks();
//    createItems();
//    createOres();
  }

  public void init(FMLInitializationEvent e) {

    try {
      new IC2Registrar().register();
    } catch (java.lang.NoClassDefFoundError ignored) {
      throw new RuntimeException();
      // IC2 not installed;
    }

    DropsRegistry.INSTANCE.init();

    OresRegistry.INSTANCE.fulfillRequests();

    addOreDicts();
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
      worldSaveDirectory = new File(saveDirectory, worldName);
    } else {
      PropertyManager settings = new PropertyManager(event.getServer().getFile("server.properties"));
      worldName = settings.getStringProperty("level-name", worldName);
      worldSaveDirectory = event.getServer().getFile(worldName);
    }
    try {
      WorldServer server = event.getServer().getWorld(0);
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
    OresRegistry.INSTANCE.recheckPile();
    for (Runnable action : serverCloseActions) {
      action.run();
    }
    for (Runnable action : oneShotServerCloseActions) {
      action.run();
    }
    oneShotServerCloseActions.clear();

  }

  public void runOnServerClose(Runnable action) {
    serverCloseActions.add(action);
  }

  public void runOnNextServerCloseOnly(Runnable action) {
    serverCloseActions.add(action);
  }

  private ArrayList<Runnable> oneShotServerCloseActions = new ArrayList<Runnable>();
  private ArrayList<Runnable> serverCloseActions = new ArrayList<Runnable>();

  public void registerModels(ModelRegistryEvent event) {
  };

  public void registerBlocks(RegistryEvent.Register<Block> event) {
    /*
     * Blocks
     */
    LOGGER.debug("Start registering blocks");
    IForgeRegistry registry = event.getRegistry();
//    registry.register(new IgneousStone());
//    registry.register(new IgneousCobble());
//    registry.register(new IgneousBrick());
//    registry.register(new MetamorphicStone());
//    registry.register(new MetamorphicCobble());
//    registry.register(new MetamorphicBrick());
//    registry.register(new SedimentaryStone());

//
    API.IGNEOUS_STONE.registerBlock(event, new IgneousStone());
    API.IGNEOUS_COBBLE.registerBlock(event, new IgneousCobble());
    API.IGNEOUS_BRICK.registerBlock(event, new IgneousBrick());
    API.METAMORPHIC_STONE.registerBlock(event, new MetamorphicStone());
    API.METAMORPHIC_COBBLE.registerBlock(event, new MetamorphicCobble());
    API.METAMORPHIC_BRICK.registerBlock(event, new MetamorphicBrick());
    API.SEDIMENTARY_STONE.registerBlock(event, new SedimentaryStone());

    // TODO
//    /*
//     * Slabs
//     */
//
//    API.IGNEOUS_BRICK_SLAB.register(new SlabItemBlock(new UBIgneousBrickSlabHalf(), new UBIgneousBrickSlabDouble()));
//    API.METAMORPHIC_BRICK_SLAB.register(new SlabItemBlock(new UBMetamorphicBrickSlabHalf(), new UBMetamorphicBrickSlabDouble()));
//    API.IGNEOUS_STONE_SLAB.register(new SlabItemBlock(new UBIgneousStoneSlabHalf(), new UBIgneousStoneSlabDouble()));
//    API.METAMORPHIC_STONE_SLAB.register(new SlabItemBlock(new UBMetamorphicStoneSlabHalf(), new UBMetamorphicStoneSlabDouble()));
//    API.IGNEOUS_COBBLE_SLAB.register(new SlabItemBlock(new UBIgneousCobbleSlabHalf(), new UBIgneousCobbleSlabDouble()));
//    API.METAMORPHIC_COBBLE_SLAB.register(new SlabItemBlock(new UBMetamorphicCobbleSlabHalf(), new UBMetamorphicCobbleSlabDouble()));
//    API.SEDIMENTARY_STONE_SLAB.register(new SlabItemBlock(new UBSedimentaryStoneSlabHalf(), new UBSedimentaryStoneSlabDouble()));
//
//    /*
//     * Buttons
//     */
//
//    if (UBConfig.SPECIFIC.buttonsOn()) {
//      if (UBConfig.SPECIFIC.igneousButtonsOn()) {
//        if (UBConfig.SPECIFIC.stoneButtonsOn()) {
//          API.IGNEOUS_STONE_BUTTON.register(new ButtonItemBlock(API.IGNEOUS_STONE, UBButtonIgneous.class));
//        }
//        if (UBConfig.SPECIFIC.cobbleButtonsOn()) {
//          API.IGNEOUS_COBBLE_BUTTON.register(new ButtonItemBlock(API.IGNEOUS_COBBLE, UBButtonIgneousCobble.class));
//        }
//        if (UBConfig.SPECIFIC.brickButtonsOn())
//          API.IGNEOUS_BRICK_BUTTON.register(new ButtonItemBlock(API.IGNEOUS_BRICK, UBButtonIgneousBrick.class));
//      }
//      if (UBConfig.SPECIFIC.metamorphicButtonsOn()) {
//        if (UBConfig.SPECIFIC.stoneButtonsOn())
//          API.METAMORPHIC_STONE_BUTTON.register(new ButtonItemBlock(API.METAMORPHIC_STONE, UBButtonMetamorphic.class));
//        if (UBConfig.SPECIFIC.cobbleButtonsOn())
//          API.METAMORPHIC_COBBLE_BUTTON.register(new ButtonItemBlock(API.METAMORPHIC_COBBLE, UBButtonMetamorphicCobble.class));
//        if (UBConfig.SPECIFIC.brickButtonsOn())
//          API.METAMORPHIC_BRICK_BUTTON.register(new ButtonItemBlock(API.METAMORPHIC_BRICK, UBButtonMetamorphicBrick.class));
//      }
//      if (UBConfig.SPECIFIC.sedimentaryButtonsOn())
//        API.SEDIMENTARY_STONE_BUTTON.register(new ButtonItemBlock(API.SEDIMENTARY_STONE, UBButtonSedimentary.class));
//    }
//
//
//    /*
//     * Walls
//     */
//
//    if (UBConfig.SPECIFIC.wallsOn()) {
//      if (UBConfig.SPECIFIC.igneousWallsOn()) {
//        if (UBConfig.SPECIFIC.stoneWallsOn())
//          API.IGNEOUS_STONE_WALL.register(new UBWallIgneous(API.IGNEOUS_STONE));
//        if (UBConfig.SPECIFIC.cobbleWallsOn())
//          API.IGNEOUS_COBBLE_WALL.register(new UBWallIgneousCobble(API.IGNEOUS_COBBLE));
//        if (UBConfig.SPECIFIC.brickWallsOn())
//          API.IGNEOUS_BRICK_WALL.register(new UBWallIgneousBrick(API.IGNEOUS_BRICK));
//      }
//      if (UBConfig.SPECIFIC.metamorphicWallsOn()) {
//        if (UBConfig.SPECIFIC.stoneWallsOn())
//          API.METAMORPHIC_STONE_WALL.register(new UBWallMetamorphic(API.METAMORPHIC_STONE));
//        if (UBConfig.SPECIFIC.cobbleWallsOn())
//          API.METAMORPHIC_COBBLE_WALL.register(new UBWallMetamorphicCobble(API.METAMORPHIC_COBBLE));
//        if (UBConfig.SPECIFIC.brickWallsOn())
//          API.METAMORPHIC_BRICK_WALL.register(new UBWallMetamorphicBrick(API.METAMORPHIC_BRICK));
//      }
//      if (UBConfig.SPECIFIC.sedimentaryWallsOn())
//        API.SEDIMENTARY_STONE_WALL.register(new UBWallSedimentary(API.SEDIMENTARY_STONE));
//    }
//
//    /*
//     * Stairs
//     */
//
//    if (UBConfig.SPECIFIC.stairsOn()) {
//      if (UBConfig.SPECIFIC.igneousStairsOn()) {
//        if (UBConfig.SPECIFIC.stoneStairsOn())
//          API.IGNEOUS_STONE_STAIRS.register(new StairsItemBlock(API.IGNEOUS_STONE, UBStairsIgneous.class));
//        if (UBConfig.SPECIFIC.cobbleStairsOn())
//          API.IGNEOUS_COBBLE_STAIRS.register(new StairsItemBlock(API.IGNEOUS_COBBLE, UBStairsIgneousCobble.class));
//        if (UBConfig.SPECIFIC.brickStairsOn())
//          API.IGNEOUS_BRICK_STAIRS.register(new StairsItemBlock(API.IGNEOUS_BRICK, UBStairsIgneousBrick.class));
//      }
//      if (UBConfig.SPECIFIC.metamorphicStairsOn()) {
//        if (UBConfig.SPECIFIC.stoneStairsOn())
//          API.METAMORPHIC_STONE_STAIRS.register(new StairsItemBlock(API.METAMORPHIC_STONE, UBStairsMetamorphic.class));
//        if (UBConfig.SPECIFIC.cobbleStairsOn())
//          API.METAMORPHIC_COBBLE_STAIRS.register(new StairsItemBlock(API.METAMORPHIC_COBBLE, UBStairsMetamorphicCobble.class));
//        if (UBConfig.SPECIFIC.brickStairsOn())
//          API.METAMORPHIC_BRICK_STAIRS.register(new StairsItemBlock(API.METAMORPHIC_BRICK, UBStairsMetamorphicBrick.class));
//      }
//      if (UBConfig.SPECIFIC.sedimentaryStairsOn())
//        API.SEDIMENTARY_STONE_STAIRS.register(new StairsItemBlock(API.SEDIMENTARY_STONE, UBStairsSedimentary.class));
//
//
//    }
  }

  @SubscribeEvent
  public void registerItems(RegistryEvent.Register<Item> event) {

    API.LIGNITE_COAL.registerItem(event, new ItemLigniteCoal());
    API.FOSSIL_PIECE.registerItem(event, new ItemFossilPiece());
// TODO
    API.IGNEOUS_STONE.registerItem(event, new IgneousStone());
    API.IGNEOUS_COBBLE.registerItem(event, new IgneousCobble());
    API.IGNEOUS_BRICK.registerItem(event, new IgneousBrick());
    API.METAMORPHIC_STONE.registerItem(event, new MetamorphicStone());
    API.METAMORPHIC_COBBLE.registerItem(event, new MetamorphicCobble());
    API.METAMORPHIC_BRICK.registerItem(event, new MetamorphicBrick());
    API.SEDIMENTARY_STONE.registerItem(event, new SedimentaryStone());
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

  public void addOreDicts() {
    // wildcarding is not working
    for (int i = 0; i < 8; i++) {
//      OreDictionary.registerOre("stone", new ItemStack(new IgneousStone().getItemBlock(), 1, i));
//      OreDictionary.registerOre("stone", new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
//      OreDictionary.registerOre("stone", new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
//      OreDictionary.registerOre("cobblestone", new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
//      OreDictionary.registerOre("cobblestone", new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
//      OreDictionary.registerOre("stoneBricks", new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
//      OreDictionary.registerOre("stoneBricks", new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
    }
    OresRegistry.INSTANCE.copyOreDictionaries();
  }

  private final void createRecipes() {
    //TODO
//    GameRegistry.addShapedRecipe(new ItemStack(Items.COAL), "XXX", "XXX", "XXX", 'X', API.LIGNITE_COAL.getItem());
//    GameRegistry.addShapelessRecipe(new ItemStack(Items.DYE, 1, 15), new ItemStack(API.FOSSIL_PIECE.getItem(), 1, WILDCARD_VALUE));
//
//    for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
//      GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 4, i), "XX", "XX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
//      GameRegistry.addSmelting(new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i), new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i), 0.1f);
//    }
//    for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
//      GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 4, i), "XX", "XX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
//      GameRegistry.addSmelting(new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i), new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i), 0.1f);
//    }
//
//    /*
//     * Slabs
//     */
//
//    for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
//      GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_STONE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
//      GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_COBBLE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
//      GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
//    }
//    for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
//      GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_STONE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
//      GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_COBBLE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
//      GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
//    }
//    for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
//      GameRegistry.addShapedRecipe(new ItemStack(API.SEDIMENTARY_STONE_SLAB.getItem(), 6, i), "XXX", 'X', new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
//
//    /*
//     * Buttons
//     */
//
//    int n = UBConfig.SPECIFIC.buttonRecipeResult();
//
//    if (UBConfig.SPECIFIC.buttonsOn()) {
//      if (UBConfig.SPECIFIC.igneousButtonsOn())
//        for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
//          if (UBConfig.SPECIFIC.stoneButtonsOn())
//            GameRegistry.addShapelessRecipe(new ItemStack(API.IGNEOUS_STONE_BUTTON.getItemBlock(), n, i), new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.cobbleButtonsOn())
//            GameRegistry.addShapelessRecipe(new ItemStack(API.IGNEOUS_COBBLE_BUTTON.getItemBlock(), n, i), new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.brickButtonsOn())
//            GameRegistry.addShapelessRecipe(new ItemStack(API.IGNEOUS_BRICK_BUTTON.getItemBlock(), n, i), new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
//        }
//      if (UBConfig.SPECIFIC.metamorphicButtonsOn())
//        for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
//          if (UBConfig.SPECIFIC.stoneButtonsOn())
//            GameRegistry.addShapelessRecipe(new ItemStack(API.METAMORPHIC_STONE_BUTTON.getItemBlock(), n, i), new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.cobbleButtonsOn())
//            GameRegistry.addShapelessRecipe(new ItemStack(API.METAMORPHIC_COBBLE_BUTTON.getItemBlock(), n, i), new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.brickButtonsOn())
//            GameRegistry.addShapelessRecipe(new ItemStack(API.METAMORPHIC_BRICK_BUTTON.getItemBlock(), n, i), new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
//        }
//      if (UBConfig.SPECIFIC.sedimentaryButtonsOn())
//        for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
//          GameRegistry.addShapelessRecipe(new ItemStack(API.SEDIMENTARY_STONE_BUTTON.getItemBlock(), n, i), new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
//
//    }
//
//    /*
//     * Walls
//     */
//
//    if (UBConfig.SPECIFIC.wallsOn()) {
//      if (UBConfig.SPECIFIC.igneousWallsOn())
//        for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
//          if (UBConfig.SPECIFIC.stoneWallsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_STONE_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.cobbleWallsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_COBBLE_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.brickWallsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
//        }
//      if (UBConfig.SPECIFIC.metamorphicWallsOn())
//        for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
//          if (UBConfig.SPECIFIC.stoneWallsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_STONE_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.cobbleWallsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_COBBLE_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.brickWallsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
//        }
//      if (UBConfig.SPECIFIC.sedimentaryWallsOn())
//        for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
//          GameRegistry.addShapedRecipe(new ItemStack(API.SEDIMENTARY_STONE_WALL.getItemBlock(), 6, i), "XXX", "XXX", 'X', new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
//    }
//
//    /*
//     * Stairs
//     */
//
//    if (UBConfig.SPECIFIC.stairsOn()) {
//      if (UBConfig.SPECIFIC.igneousStairsOn())
//        for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
//          if (UBConfig.SPECIFIC.stoneStairsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_STONE_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.cobbleStairsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_COBBLE_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.brickStairsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.IGNEOUS_BRICK_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
//        }
//      if (UBConfig.SPECIFIC.metamorphicStairsOn())
//        for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
//          if (UBConfig.SPECIFIC.stoneStairsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_STONE_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.cobbleStairsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_COBBLE_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
//          if (UBConfig.SPECIFIC.brickStairsOn())
//            GameRegistry.addShapedRecipe(new ItemStack(API.METAMORPHIC_BRICK_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
//        }
//      if (UBConfig.SPECIFIC.sedimentaryStairsOn())
//        for (int i = 0; i < SedimentaryVariant.NB_VARIANTS; ++i)
//          GameRegistry.addShapedRecipe(new ItemStack(API.SEDIMENTARY_STONE_STAIRS.getItemBlock(), 4, i), "X  ", "XX ", "XXX", 'X', new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
//    }
//
//    /*
//     *
//     */
//
//    if (UBConfig.SPECIFIC.ubifyRecipes()) {
//      StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(cobbleEntry -> {
//        Item cobble = cobbleEntry.getItemBlock();
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.FURNACE), "XXX", "X X", "XXX", 'X', cobble);
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.LEVER), "I", "X", 'X', cobble, 'I', Items.STICK);
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.PISTON), "WWW", "CIC", "CRC", 'W', Blocks.PLANKS, 'C', cobble, 'I', Items.IRON_INGOT, 'R', Items.REDSTONE);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_AXE), "XX ", "XW ", " W ", 'X', cobble, 'W', Items.STICK);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_PICKAXE), "XXX", " W ", " W ", 'X', cobble, 'W', Items.STICK);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_HOE), "XX ", " W ", " W ", 'X', cobble, 'W', Items.STICK);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_SHOVEL), " X ", " W ", " W ", 'X', cobble, 'W', Items.STICK);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.STONE_SWORD), "X", "X", "W", 'X', cobble, 'W', Items.STICK);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.BREWING_STAND), " B ", "XXX", 'X', cobble, 'B', Items.BLAZE_ROD);
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.DISPENSER), "XXX", "XBX", "XRX", 'X', cobble, 'B', Items.BOW, 'R', Items.REDSTONE);
//      });
//      StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.STONE).forEach(stoneEntry -> {
//        Item stone = stoneEntry.getItemBlock();
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE_PRESSURE_PLATE), "XX", 'X', stone);
//        GameRegistry.addShapedRecipe(new ItemStack(Items.REPEATER), "TRT", "XXX", 'X', stone, 'T', Blocks.REDSTONE_TORCH, 'R', Items.REDSTONE);
//      });
//    }
//    if (!UBConfig.SPECIFIC.buttonsOn() && UBConfig.SPECIFIC.ubifyRecipes())
//      StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.STONE).forEach(stoneEntry -> {
//        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.STONE_BUTTON), stoneEntry.getItemBlock());
//      });
//    if (!UBConfig.SPECIFIC.wallsOn() && UBConfig.SPECIFIC.ubifyRecipes())
//      StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(cobbleEntry -> {
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.COBBLESTONE_WALL, 6), "XXX", "XXX", 'X', cobbleEntry.getItemBlock());
//      });
//    if (!UBConfig.SPECIFIC.stairsOn() && UBConfig.SPECIFIC.ubifyRecipes()) {
//      StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(cobbleEntry -> {
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE_STAIRS, 4), "X  ", "XX ", "XXX", 'X', cobbleEntry.getItemBlock());
//      });
//      StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.BRICK).forEach(brickEntry -> {
//        GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONE_BRICK_STAIRS, 4), "X  ", "XX ", "XXX", 'X', brickEntry.getItemBlock());
//      });
//    }
//
//    ((UBConfig) (UBConfig.SPECIFIC)).regularStoneCrafting.addTrackerAndUpdate(new RegularStoneRecipe());
//    ((UBConfig) (UBConfig.SPECIFIC)).changeButtonRecipe.addTrackerAndUpdate(new ButtonRecipe());
  }

}
