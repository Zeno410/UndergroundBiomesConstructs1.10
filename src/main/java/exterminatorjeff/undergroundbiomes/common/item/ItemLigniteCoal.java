package exterminatorjeff.undergroundbiomes.common.item;

import exterminatorjeff.undergroundbiomes.api.common.UBItem;
import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import net.minecraft.item.Item;

/**
 * @author CurtisA, LouisDB
 */
public class ItemLigniteCoal extends Item implements UBItem {

  public ItemLigniteCoal() {
    setCreativeTab(UBCreativeTab.UB_ITEMS_TAB);
  }

  @Override
  public Item toItem() {
    return this;
  }

}
