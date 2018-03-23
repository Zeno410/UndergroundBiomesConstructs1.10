package exterminatorjeff.undergroundbiomes.common.block.button;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.METAMORPHIC_VARIANTS;
import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.METAMORPHIC_VARIANT_PROPERTY;

/**
 * @author CurtisA, LouisDB
 */
public class UBButtonMetamorphic extends UBStoneButton {

  public UBButtonMetamorphic(EnumFacing facing, ButtonItemBlock itemBlock) {
    super(facing, itemBlock);
    setDefaultState(getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[0]));
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.METAMORPHIC_STONE.getBlock();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, POWERED, METAMORPHIC_VARIANT_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return super.getStateFromMeta(meta).withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[meta & 7]);
  }

}
