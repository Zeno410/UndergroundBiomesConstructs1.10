package exterminatorjeff.undergroundbiomes.common.block;

import exterminatorjeff.undergroundbiomes.api.API;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANT_PROPERTY;
import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.values;

/**
 * @author CurtisA, LouisDB
 */
public class UBOreIgneous extends UBOre {

  public UBOreIgneous(Block baseOre, int baseOreMeta) {
    super(baseOre, baseOreMeta);
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.IGNEOUS_STONE.getBlock();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, IGNEOUS_VARIANT_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, values()[meta]);
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, values()[placer.getHeldItemMainhand().getMetadata()]);
  }

}
