package exterminatorjeff.undergroundbiomes.common;

import exterminatorjeff.undergroundbiomes.api.API;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

/**
 * Register lignite coal item as a fuel.
 *
 * @author CurtisA
 */
public enum UBFuelHandler implements IFuelHandler {
  INSTANCE;

  @Override
  public int getBurnTime(ItemStack fuel) {
    if (fuel.getItem().equals(API.LIGNITE_COAL.getItem()))
      return 200; // One item
    else
      return 0;
  }

}
