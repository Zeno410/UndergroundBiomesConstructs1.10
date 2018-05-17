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
public class ThermalFoundationRegistrar extends ModOreRegistrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  public static String oreName = "thermalfoundation:ore";

  public void register(RegistryEvent.Register<Block> event) {
    registerOre(event, oreName, 0, ModInfo.MODID + ":blocks/overlays/copper");
    registerOre(event, oreName, 1, ModInfo.MODID + ":blocks/overlays/tin");
    registerOre(event, oreName, 4, ModInfo.MODID + ":blocks/overlays/aluminum");
    registerOre(event, oreName, 3, ModInfo.MODID + ":blocks/overlays/lead");
    registerOre(event, oreName, 2, ModInfo.MODID + ":blocks/overlays/silver");
    registerOre(event, oreName, 5, ModInfo.MODID + ":blocks/overlays/nickel");
    registerOre(event, oreName, 6, ModInfo.MODID + ":blocks/overlays/platinum");
    registerOre(event, oreName, 7, ModInfo.MODID + ":blocks/overlays/iridium");
    registerOre(event, oreName, 8, ModInfo.MODID + ":blocks/overlays/mana_infused");
    registerOre(event, "thermalfoundation:ore_fluid", 2, ModInfo.MODID + ":blocks/overlays/destabilized_redstone");
  }
}
