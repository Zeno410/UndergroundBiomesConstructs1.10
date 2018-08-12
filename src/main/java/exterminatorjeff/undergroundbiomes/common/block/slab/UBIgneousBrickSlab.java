package exterminatorjeff.undergroundbiomes.common.block.slab;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;

/**
 * @author CurtisA, LouisDB
 */
public abstract class UBIgneousBrickSlab extends UBIgneousStoneSlab {

  @Override
  public UBStone baseStone() {
    return (UBStone) API.IGNEOUS_BRICK.getBlock();
  }

}
