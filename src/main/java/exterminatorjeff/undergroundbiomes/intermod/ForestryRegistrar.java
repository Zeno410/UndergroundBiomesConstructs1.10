/*
 */

package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import org.apache.logging.log4j.Level;

/**
 * @author Zeno410
 */
public class ForestryRegistrar extends ModOreRegistrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  public static String oreName = "forestry:resources";

  public static int tinMeta = 2;
  public static int copperMeta = 1;
  public static int apatiteMeta = 0;

  public void register(RegistryEvent.Register<Block> event) {
    registerOre(event, oreName, copperMeta, ModInfo.MODID + ":blocks/overlays/copper");
    registerOre(event, oreName, apatiteMeta, ModInfo.MODID + ":blocks/overlays/apatite");
    registerOre(event, oreName, tinMeta, ModInfo.MODID + ":blocks/overlays/tin");
  }
}
