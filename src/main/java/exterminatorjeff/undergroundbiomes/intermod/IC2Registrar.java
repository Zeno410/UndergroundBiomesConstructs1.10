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
public class IC2Registrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  public static String copperOreName = "oreCopper";
  public static String leadOreName = "oreLead";
  public static String tinOreName = "oreTin";
  public static String uraniumOreName = "oreUranium";

  public static String ic2OreName = "ic2:resource";
  public static ResourceLocation ic2OreLocation = new ResourceLocation(ic2OreName);

  public static int copperMeta = 1;
  public static int leadMeta = 2;
  public static int tinMeta = 3;
  public static int uraniumMeta = 4;

  public void register(RegistryEvent.Register<Block> event) {
    //if (IC2.MODID == null) treturn; that doesn't work?
    ModOreRegistrar registrar = new ModOreRegistrar();
    registrar.register(event, ic2OreName, copperMeta, ModInfo.MODID + ":blocks/overlays/copper");
    registrar.register(event, ic2OreName, leadMeta, ModInfo.MODID + ":blocks/overlays/lead");
    registrar.register(event, ic2OreName, tinMeta, ModInfo.MODID + ":blocks/overlays/tin");
    registrar.register(event, ic2OreName, uraniumMeta, ModInfo.MODID + ":blocks/overlays/uranium");
  }
}
