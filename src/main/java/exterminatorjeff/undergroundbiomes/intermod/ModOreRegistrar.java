package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Level;

public class ModOreRegistrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  public void registerOre(RegistryEvent.Register<Block> event, String ore, int meta, String overlay) {
    ResourceLocation location = new ResourceLocation(ore);
    IForgeRegistry<Block> registry = event.getRegistry();
    if (!registry.containsKey(location)) {
      return;
    }
    Block block = registry.getValue(location);
    OresRegistry.INSTANCE.requestOreSetup(block, meta);
    OresRegistry.INSTANCE.registerOreOverlay(block, meta, new ResourceLocation(overlay));
  }

}
