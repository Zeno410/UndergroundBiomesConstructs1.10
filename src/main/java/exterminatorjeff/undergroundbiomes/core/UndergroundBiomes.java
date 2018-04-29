package exterminatorjeff.undergroundbiomes.core;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

/**
 * The main class
 *
 * @author CurtisA, LouisDB
 */
@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
@Mod.EventBusSubscriber
public class UndergroundBiomes {

  @Instance
  public static UndergroundBiomes INSTANCE;

  @SidedProxy(serverSide = "exterminatorjeff.undergroundbiomes.core.ServerProxy", clientSide = "exterminatorjeff.undergroundbiomes.core.ClientProxy")
  public static CommonProxy PROXY;

  public UndergroundBiomes() {
  }

  /*
   *
   */

  private static final UBLogger LOGGER = new UBLogger(UndergroundBiomes.class, Level.INFO);

  public static boolean isPreInitDone = false;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    LOGGER.info("Start Pre-init!");
    PROXY.preInit(event);

    if (API.VERSION != "1.0.0")
      throw new RuntimeException("Another mod has included an obsolete version of the Underground Biomes API.");

    isPreInitDone = true;
    LOGGER.info("Pre-init done!");
  }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {
    PROXY.registerBlocks(event);
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event) {
    PROXY.registerItems(event);
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    PROXY.registerModels(event);
  }

  @SubscribeEvent
  public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
      PROXY.createRecipes(event);
  }

  @EventHandler
  public  void init(FMLInitializationEvent event) {
    PROXY.init(event);
    LOGGER.info("Init done!");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) throws Exception {
    PROXY.postInit(event);
    LOGGER.info("Post-init done!");
  }

  @EventHandler
  public void serverStopped(FMLServerStoppedEvent event) {
    PROXY.onServerStopped(event);
    LOGGER.info("Server Stopped");
  }

}
