package exterminatorjeff.undergroundbiomes.common;

import exterminatorjeff.undergroundbiomes.api.common.UBBlock;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneType;

/**
 * Common interface for UB blocks.
 *
 * @author LouisDB
 */
public interface UBSubBlock extends UBBlock {

  default UBStoneType getStoneType() {
    return baseStone().getStoneType();
  }

  @Override
  default UBStoneStyle getStoneStyle() {
    return baseStone().getStoneStyle();
  }

  @Override
  default int getNbVariants() {
    return baseStone().getNbVariants();
  }

  @Override
  default String getVariantName(int meta) {
    return baseStone().getVariantName(meta);
  }

}
