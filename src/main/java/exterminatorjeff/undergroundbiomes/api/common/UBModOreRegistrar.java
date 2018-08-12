package exterminatorjeff.undergroundbiomes.api.common;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;

public interface UBModOreRegistrar {

  public void requestOreSetups(RegistryEvent.Register<Block> event);
}
