package exterminatorjeff.undergroundbiomes.api.names;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author LouisDB
 */
public final class StoneEntry extends BlockEntry {

  protected SlabEntry slab;
  protected ButtonEntry button;
  protected WallEntry wall;
  protected StairsEntry stairs;

  public StoneEntry(String internalName) {
    super(internalName);
  }

  public SlabEntry getSlab() {
    return slab;
  }

  public ButtonEntry getButton() {
    return button;
  }

  public WallEntry getWall() {
    return wall;
  }

  public StairsEntry getStairs() {
    return stairs;
  }

  @Override
  protected void doRegisterItem(IForgeRegistry<Item> registry) {

  }

  @Override
  protected void doRegisterBlock(IForgeRegistry<Block> registry) {

  }
}
