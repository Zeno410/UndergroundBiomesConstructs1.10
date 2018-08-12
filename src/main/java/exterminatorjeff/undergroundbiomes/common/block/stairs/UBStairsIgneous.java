package exterminatorjeff.undergroundbiomes.common.block.stairs;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.StairsItemBlock;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANTS;
import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANT_PROPERTY;

/**
 * @author CurtisA, LouisDB
 */
public class UBStairsIgneous extends UBStoneStairs {

  public UBStairsIgneous(IBlockState modelState, EnumFacing facing, StairsItemBlock itemBlock) {
    super(modelState, facing, itemBlock);
    setDefaultState(getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[0]));
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.IGNEOUS_STONE.getBlock();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, HALF, SHAPE, IGNEOUS_VARIANT_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return super.getStateFromMeta(meta).withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[meta & 7]);
  }

}
