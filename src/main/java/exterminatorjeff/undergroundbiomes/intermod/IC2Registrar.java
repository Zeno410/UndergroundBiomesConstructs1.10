/*
 */

package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Level;

/**
 * @author Zeno410
 */
public class IC2Registrar extends ModOreRegistrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  public static String ic2OreName = "ic2:resource";

  public static int copperMeta = 1;
  public static int leadMeta = 2;
  public static int tinMeta = 3;
  public static int uraniumMeta = 4;

  public void register(RegistryEvent.Register<Block> event) {
    registerOre(event, ic2OreName, copperMeta, ModInfo.MODID + ":blocks/overlays/copper");
    registerOre(event, ic2OreName, leadMeta, ModInfo.MODID + ":blocks/overlays/lead");
    registerOre(event, ic2OreName, tinMeta, ModInfo.MODID + ":blocks/overlays/tin");
    registerOre(event, ic2OreName, uraniumMeta, ModInfo.MODID + ":blocks/overlays/uranium");
  }
}
