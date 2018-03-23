package exterminatorjeff.undergroundbiomes.api.names;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 *
 * @author LouisDB
 *
 */
public final class WallEntry extends BlockEntry {

	public WallEntry(StoneEntry baseStoneEntry) {
		super(baseStoneEntry.internalName + "_wall");
		baseStoneEntry.wall = this;
	}

  @Override
  protected void doRegisterItem(IForgeRegistry<Item> registry) {

  }

  @Override
  protected void doRegisterBlock(IForgeRegistry<Block> registry) {

  }
}
