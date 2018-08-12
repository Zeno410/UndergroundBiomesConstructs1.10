package exterminatorjeff.undergroundbiomes.core;

import exterminatorjeff.undergroundbiomes.network.DedicatedServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author CurtisA, LouisDB
 */
public final class ServerProxy extends CommonProxy {

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);

    MinecraftForge.EVENT_BUS.register(DedicatedServer.INSTANCE);
  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);

  }

  @Override
  public void postInit(FMLPostInitializationEvent e) {
    super.postInit(e);

  }

}
