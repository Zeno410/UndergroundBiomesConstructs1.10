package exterminatorjeff.undergroundbiomes.common.block.slab;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;

/**
 * @author CurtisA, LouisDB
 */
public abstract class UBMetamorphicBrickSlab extends UBMetamorphicStoneSlab {

  @Override
  public UBStone baseStone() {
    return (UBStone) API.METAMORPHIC_BRICK.getBlock();
  }

}
