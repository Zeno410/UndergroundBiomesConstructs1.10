package exterminatorjeff.undergroundbiomes.core;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant;
import exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant;
import exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.names.Entry;
import exterminatorjeff.undergroundbiomes.api.names.StoneEntry;
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
import exterminatorjeff.undergroundbiomes.intermod.*;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;
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

    createOres();
  }

  public void init(FMLInitializationEvent e) {
    DropsRegistry.INSTANCE.init();

    addOreDicts();
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
    // Only used in Client, overwritten there
  }

  public void registerBlocks(RegistryEvent.Register<Block> event) {
    /*
     * Blocks
     */
    new ActuallyAdditionsRegistrar().register(event);
    new ForestryRegistrar().register(event);
    new IC2Registrar().register(event);
    new ImmersiveEngineeringRegistrar().register(event);

    LOGGER.debug("Start registering blocks");

    API.IGNEOUS_STONE.registerBlock(event, new IgneousStone());
    API.IGNEOUS_COBBLE.registerBlock(event, new IgneousCobble());
    API.IGNEOUS_BRICK.registerBlock(event, new IgneousBrick());
    API.METAMORPHIC_STONE.registerBlock(event, new MetamorphicStone());
    API.METAMORPHIC_COBBLE.registerBlock(event, new MetamorphicCobble());
    API.METAMORPHIC_BRICK.registerBlock(event, new MetamorphicBrick());
    API.SEDIMENTARY_STONE.registerBlock(event, new SedimentaryStone());

    /*
     * Slabs
     */

    API.IGNEOUS_BRICK_SLAB.registerBlock(event, new SlabItemBlock(new UBIgneousBrickSlabHalf(), new UBIgneousBrickSlabDouble()));
    API.METAMORPHIC_BRICK_SLAB.registerBlock(event, new SlabItemBlock(new UBMetamorphicBrickSlabHalf(), new UBMetamorphicBrickSlabDouble()));
    API.IGNEOUS_STONE_SLAB.registerBlock(event, new SlabItemBlock(new UBIgneousStoneSlabHalf(), new UBIgneousStoneSlabDouble()));
    API.METAMORPHIC_STONE_SLAB.registerBlock(event, new SlabItemBlock(new UBMetamorphicStoneSlabHalf(), new UBMetamorphicStoneSlabDouble()));
    API.IGNEOUS_COBBLE_SLAB.registerBlock(event, new SlabItemBlock(new UBIgneousCobbleSlabHalf(), new UBIgneousCobbleSlabDouble()));
    API.METAMORPHIC_COBBLE_SLAB.registerBlock(event, new SlabItemBlock(new UBMetamorphicCobbleSlabHalf(), new UBMetamorphicCobbleSlabDouble()));
    API.SEDIMENTARY_STONE_SLAB.registerBlock(event, new SlabItemBlock(new UBSedimentaryStoneSlabHalf(), new UBSedimentaryStoneSlabDouble()));

    /*
     * Buttons
     */

    if (UBConfig.SPECIFIC.buttonsOn()) {
      if (UBConfig.SPECIFIC.igneousButtonsOn()) {
        if (UBConfig.SPECIFIC.stoneButtonsOn()) {
          API.IGNEOUS_STONE_BUTTON.registerBlock(event, new ButtonItemBlock(API.IGNEOUS_STONE, UBButtonIgneous.class));
        }
        if (UBConfig.SPECIFIC.cobbleButtonsOn()) {
          API.IGNEOUS_COBBLE_BUTTON.registerBlock(event, new ButtonItemBlock(API.IGNEOUS_COBBLE, UBButtonIgneousCobble.class));
        }
        if (UBConfig.SPECIFIC.brickButtonsOn())
          API.IGNEOUS_BRICK_BUTTON.registerBlock(event, new ButtonItemBlock(API.IGNEOUS_BRICK, UBButtonIgneousBrick.class));
      }
      if (UBConfig.SPECIFIC.metamorphicButtonsOn()) {
        if (UBConfig.SPECIFIC.stoneButtonsOn())
          API.METAMORPHIC_STONE_BUTTON.registerBlock(event, new ButtonItemBlock(API.METAMORPHIC_STONE, UBButtonMetamorphic.class));
        if (UBConfig.SPECIFIC.cobbleButtonsOn())
          API.METAMORPHIC_COBBLE_BUTTON.registerBlock(event, new ButtonItemBlock(API.METAMORPHIC_COBBLE, UBButtonMetamorphicCobble.class));
        if (UBConfig.SPECIFIC.brickButtonsOn())
          API.METAMORPHIC_BRICK_BUTTON.registerBlock(event, new ButtonItemBlock(API.METAMORPHIC_BRICK, UBButtonMetamorphicBrick.class));
      }
      if (UBConfig.SPECIFIC.sedimentaryButtonsOn())
        API.SEDIMENTARY_STONE_BUTTON.registerBlock(event, new ButtonItemBlock(API.SEDIMENTARY_STONE, UBButtonSedimentary.class));
    }


    /*
     * Walls
     */

    if (UBConfig.SPECIFIC.wallsOn()) {
      if (UBConfig.SPECIFIC.igneousWallsOn()) {
        if (UBConfig.SPECIFIC.stoneWallsOn())
          API.IGNEOUS_STONE_WALL.registerBlock(event, new UBWallIgneous(API.IGNEOUS_STONE));
        if (UBConfig.SPECIFIC.cobbleWallsOn())
          API.IGNEOUS_COBBLE_WALL.registerBlock(event, new UBWallIgneousCobble(API.IGNEOUS_COBBLE));
        if (UBConfig.SPECIFIC.brickWallsOn())
          API.IGNEOUS_BRICK_WALL.registerBlock(event, new UBWallIgneousBrick(API.IGNEOUS_BRICK));
      }
      if (UBConfig.SPECIFIC.metamorphicWallsOn()) {
        if (UBConfig.SPECIFIC.stoneWallsOn())
          API.METAMORPHIC_STONE_WALL.registerBlock(event, new UBWallMetamorphic(API.METAMORPHIC_STONE));
        if (UBConfig.SPECIFIC.cobbleWallsOn())
          API.METAMORPHIC_COBBLE_WALL.registerBlock(event, new UBWallMetamorphicCobble(API.METAMORPHIC_COBBLE));
        if (UBConfig.SPECIFIC.brickWallsOn())
          API.METAMORPHIC_BRICK_WALL.registerBlock(event, new UBWallMetamorphicBrick(API.METAMORPHIC_BRICK));
      }
      if (UBConfig.SPECIFIC.sedimentaryWallsOn())
        API.SEDIMENTARY_STONE_WALL.registerBlock(event, new UBWallSedimentary(API.SEDIMENTARY_STONE));
    }

    /*
     * Stairs
     */

    if (UBConfig.SPECIFIC.stairsOn()) {
      if (UBConfig.SPECIFIC.igneousStairsOn()) {
        if (UBConfig.SPECIFIC.stoneStairsOn())
          API.IGNEOUS_STONE_STAIRS.registerBlock(event, new StairsItemBlock(API.IGNEOUS_STONE, UBStairsIgneous.class));
        if (UBConfig.SPECIFIC.cobbleStairsOn())
          API.IGNEOUS_COBBLE_STAIRS.registerBlock(event, new StairsItemBlock(API.IGNEOUS_COBBLE, UBStairsIgneousCobble.class));
        if (UBConfig.SPECIFIC.brickStairsOn())
          API.IGNEOUS_BRICK_STAIRS.registerBlock(event, new StairsItemBlock(API.IGNEOUS_BRICK, UBStairsIgneousBrick.class));
      }
      if (UBConfig.SPECIFIC.metamorphicStairsOn()) {
        if (UBConfig.SPECIFIC.stoneStairsOn())
          API.METAMORPHIC_STONE_STAIRS.registerBlock(event, new StairsItemBlock(API.METAMORPHIC_STONE, UBStairsMetamorphic.class));
        if (UBConfig.SPECIFIC.cobbleStairsOn())
          API.METAMORPHIC_COBBLE_STAIRS.registerBlock(event, new StairsItemBlock(API.METAMORPHIC_COBBLE, UBStairsMetamorphicCobble.class));
        if (UBConfig.SPECIFIC.brickStairsOn())
          API.METAMORPHIC_BRICK_STAIRS.registerBlock(event, new StairsItemBlock(API.METAMORPHIC_BRICK, UBStairsMetamorphicBrick.class));
      }
      if (UBConfig.SPECIFIC.sedimentaryStairsOn())
        API.SEDIMENTARY_STONE_STAIRS.registerBlock(event, new StairsItemBlock(API.SEDIMENTARY_STONE, UBStairsSedimentary.class));
    }
    OresRegistry.INSTANCE.registerBlocks(event);
  }

  @SubscribeEvent
  public void registerItems(RegistryEvent.Register<Item> event) {


    LOGGER.debug("Start registering items");
    API.LIGNITE_COAL.registerItem(event, new ItemLigniteCoal());
    API.FOSSIL_PIECE.registerItem(event, new ItemFossilPiece());

    /* Blocks */

    IForgeRegistry<Item> registry = event.getRegistry();
    registry.register(API.IGNEOUS_STONE.getItemBlock());
    registry.register(API.METAMORPHIC_STONE.getItemBlock());
    registry.register(API.SEDIMENTARY_STONE.getItemBlock());

    registry.register(API.IGNEOUS_COBBLE.getItemBlock());
    registry.register(API.METAMORPHIC_COBBLE.getItemBlock());

    registry.register(API.IGNEOUS_BRICK.getItemBlock());
    registry.register(API.METAMORPHIC_BRICK.getItemBlock());

    /* Slabs */

    API.IGNEOUS_STONE_SLAB.registerItem(event, null);
    API.METAMORPHIC_STONE_SLAB.registerItem(event, null);
    API.IGNEOUS_COBBLE_SLAB.registerItem(event, null);
    API.METAMORPHIC_COBBLE_SLAB.registerItem(event, null);
    API.SEDIMENTARY_STONE_SLAB.registerItem(event, null);
    API.IGNEOUS_BRICK_SLAB.registerItem(event, null);
    API.METAMORPHIC_BRICK_SLAB.registerItem(event, null);

    /*
     * Buttons
     */

    if (UBConfig.SPECIFIC.buttonsOn()) {
      if (UBConfig.SPECIFIC.igneousButtonsOn()) {
        if (UBConfig.SPECIFIC.stoneButtonsOn()) {
          registry.register(API.IGNEOUS_STONE_BUTTON.getItemBlock());
        }
        if (UBConfig.SPECIFIC.cobbleButtonsOn()) {
          registry.register(API.IGNEOUS_COBBLE_BUTTON.getItemBlock());
        }
        if (UBConfig.SPECIFIC.brickButtonsOn())
          registry.register(API.IGNEOUS_BRICK_BUTTON.getItemBlock());
      }
      if (UBConfig.SPECIFIC.metamorphicButtonsOn()) {
        if (UBConfig.SPECIFIC.stoneButtonsOn())
          registry.register(API.METAMORPHIC_STONE_BUTTON.getItemBlock());
        if (UBConfig.SPECIFIC.cobbleButtonsOn())
          registry.register(API.METAMORPHIC_COBBLE_BUTTON.getItemBlock());
        if (UBConfig.SPECIFIC.brickButtonsOn())
          registry.register(API.METAMORPHIC_BRICK_BUTTON.getItemBlock());
      }
      if (UBConfig.SPECIFIC.sedimentaryButtonsOn())
        registry.register(API.SEDIMENTARY_STONE_BUTTON.getItemBlock());
    }


    /*
     * Walls
     */

    if (UBConfig.SPECIFIC.wallsOn()) {
      if (UBConfig.SPECIFIC.igneousWallsOn()) {
        if (UBConfig.SPECIFIC.stoneWallsOn())
          API.IGNEOUS_STONE_WALL.registerItem(event, new UBWallIgneous(API.IGNEOUS_STONE));
        if (UBConfig.SPECIFIC.cobbleWallsOn())
          API.IGNEOUS_COBBLE_WALL.registerItem(event, new UBWallIgneousCobble(API.IGNEOUS_COBBLE));
        if (UBConfig.SPECIFIC.brickWallsOn())
          API.IGNEOUS_BRICK_WALL.registerItem(event, new UBWallIgneousBrick(API.IGNEOUS_BRICK));
      }
      if (UBConfig.SPECIFIC.metamorphicWallsOn()) {
        if (UBConfig.SPECIFIC.stoneWallsOn())
          API.METAMORPHIC_STONE_WALL.registerItem(event, new UBWallMetamorphic(API.METAMORPHIC_STONE));
        if (UBConfig.SPECIFIC.cobbleWallsOn())
          API.METAMORPHIC_COBBLE_WALL.registerItem(event, new UBWallMetamorphicCobble(API.METAMORPHIC_COBBLE));
        if (UBConfig.SPECIFIC.brickWallsOn())
          API.METAMORPHIC_BRICK_WALL.registerItem(event, new UBWallMetamorphicBrick(API.METAMORPHIC_BRICK));
      }
      if (UBConfig.SPECIFIC.sedimentaryWallsOn())
        API.SEDIMENTARY_STONE_WALL.registerItem(event, new UBWallSedimentary(API.SEDIMENTARY_STONE));
    }

    /*
     * Stairs
     */

    if (UBConfig.SPECIFIC.stairsOn()) {
      if (UBConfig.SPECIFIC.igneousStairsOn()) {
        if (UBConfig.SPECIFIC.stoneStairsOn())
          API.IGNEOUS_STONE_STAIRS.registerItem(event, new StairsItemBlock(API.IGNEOUS_STONE, UBStairsIgneous.class));
        if (UBConfig.SPECIFIC.cobbleStairsOn())
          API.IGNEOUS_COBBLE_STAIRS.registerItem(event, new StairsItemBlock(API.IGNEOUS_COBBLE, UBStairsIgneousCobble.class));
        if (UBConfig.SPECIFIC.brickStairsOn())
          API.IGNEOUS_BRICK_STAIRS.registerItem(event, new StairsItemBlock(API.IGNEOUS_BRICK, UBStairsIgneousBrick.class));
      }
      if (UBConfig.SPECIFIC.metamorphicStairsOn()) {
        if (UBConfig.SPECIFIC.stoneStairsOn())
          API.METAMORPHIC_STONE_STAIRS.registerItem(event, new StairsItemBlock(API.METAMORPHIC_STONE, UBStairsMetamorphic.class));
        if (UBConfig.SPECIFIC.cobbleStairsOn())
          API.METAMORPHIC_COBBLE_STAIRS.registerItem(event, new StairsItemBlock(API.METAMORPHIC_COBBLE, UBStairsMetamorphicCobble.class));
        if (UBConfig.SPECIFIC.brickStairsOn())
          API.METAMORPHIC_BRICK_STAIRS.registerItem(event, new StairsItemBlock(API.METAMORPHIC_BRICK, UBStairsMetamorphicBrick.class));
      }
      if (UBConfig.SPECIFIC.sedimentaryStairsOn())
        API.SEDIMENTARY_STONE_STAIRS.registerItem(event, new StairsItemBlock(API.SEDIMENTARY_STONE, UBStairsSedimentary.class));
    }
    OresRegistry.INSTANCE.registerItems(event);
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
      OreDictionary.registerOre("stone", new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i));
      OreDictionary.registerOre("stone", new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i));
      OreDictionary.registerOre("stone", new ItemStack(API.SEDIMENTARY_STONE.getItemBlock(), 1, i));
      OreDictionary.registerOre("cobblestone", new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i));
      OreDictionary.registerOre("cobblestone", new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i));
      OreDictionary.registerOre("stoneBricks", new ItemStack(API.IGNEOUS_BRICK.getItemBlock(), 1, i));
      OreDictionary.registerOre("stoneBricks", new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
      OreDictionary.registerOre("fossilPiece", new ItemStack(API.METAMORPHIC_BRICK.getItemBlock(), 1, i));
    }
    OresRegistry.INSTANCE.copyOreDictionaries();
  }

  public void createRecipes(RegistryEvent.Register<IRecipe> event) {
    IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) event.getRegistry();

    for (int i = 0; i < IgneousVariant.NB_VARIANTS; ++i) {
      GameRegistry.addSmelting(new ItemStack(API.IGNEOUS_COBBLE.getItemBlock(), 1, i), new ItemStack(API.IGNEOUS_STONE.getItemBlock(), 1, i), 0.1f);
    }
    for (int i = 0; i < MetamorphicVariant.NB_VARIANTS; ++i) {
      GameRegistry.addSmelting(new ItemStack(API.METAMORPHIC_COBBLE.getItemBlock(), 1, i), new ItemStack(API.METAMORPHIC_STONE.getItemBlock(), 1, i), 0.1f);
    }

    if (UBConfig.SPECIFIC.buttonsOn()) {
      modRegistry.remove(new ResourceLocation("minecraft:stone_button"));
    }

    // TODO: Figure out how to update the cobblestone recipe
    RegularStoneRecipe regularCobblestoneRecipe = new RegularStoneRecipe();
    modRegistry.register(regularCobblestoneRecipe);
    ((UBConfig) (UBConfig.SPECIFIC)).regularStoneCrafting.addTrackerAndUpdate(regularCobblestoneRecipe);
    ((UBConfig) (UBConfig.SPECIFIC)).changeButtonRecipe.addTrackerAndUpdate(new ButtonRecipe());

  }

}
