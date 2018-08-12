package exterminatorjeff.undergroundbiomes.common.block;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.common.IUBOreConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.METAMORPHIC_VARIANT_PROPERTY;
import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.values;

/**
 * @author CurtisA, LouisDB
 */
public class UBOreMetamorphic extends UBOre {

  public UBOreMetamorphic(Block baseOre, IUBOreConfig config) {
    super(baseOre, config);
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.METAMORPHIC_STONE.getBlock();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, METAMORPHIC_VARIANT_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, values()[meta]);
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, values()[placer.getHeldItemMainhand().getMetadata()]);
  }

}
