/*
 */

package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import org.apache.logging.log4j.Level;

/**
 * @author Zeno410
 */
public class ActuallyAdditionsRegistrar extends ModOreRegistrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  public static String oreName = "actuallyadditions:block_misc";

  public void register(RegistryEvent.Register<Block> event) {
    registerOre(event, oreName, 3, ModInfo.MODID + ":blocks/overlays/black_quarz");
  }
}
