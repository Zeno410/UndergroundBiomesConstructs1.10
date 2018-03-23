package exterminatorjeff.undergroundbiomes.common.block.wall;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANTS;
import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANT_PROPERTY;

/**
 * @author CurtisA, LouisDB
 */
public class UBWallIgneous extends UBStoneWall {

  public UBWallIgneous(BlockEntry baseStoneEntry) {
    super(baseStoneEntry);
    setDefaultState(getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[0]));
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.IGNEOUS_STONE.getBlock();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new UBStoneWallStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, IGNEOUS_VARIANT_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int metadata) {
    return getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[metadata & 7]);
  }

}
