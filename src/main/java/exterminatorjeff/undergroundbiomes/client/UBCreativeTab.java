package exterminatorjeff.undergroundbiomes.client;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author CurstisA, LouisDB
 */
public class UBCreativeTab extends CreativeTabs {

  public static final UBCreativeTab UB_BLOCKS_TAB = new UBCreativeTab(CreativeTabs.getNextID(), ModInfo.MODID + "blocks");
  public static final UBCreativeTab UB_ITEMS_TAB = new UBCreativeTab(CreativeTabs.getNextID(), ModInfo.MODID + "items");
  public static final UBCreativeTab UB_ORES_TAB = new UBCreativeTab(CreativeTabs.getNextID(), ModInfo.MODID + "ores");

  private ItemStack icon;

  public UBCreativeTab(int id, String name) {
    super(id, name);
  }

  @SideOnly(Side.CLIENT)
  public void setTabIconItem(Item icon) {
    this.icon = new ItemStack(icon);
  }

  @SideOnly(Side.CLIENT)
  public void setTabIconItem(ItemStack icon) {
    this.icon = icon;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public ItemStack getTabIconItem() {
    return icon;
  }

}
