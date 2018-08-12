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

import static exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant.SEDIMENTARY_VARIANT_PROPERTY;
import static exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant.values;

/**
 * @author CurtisA, LouisDB
 */
public class UBOreSedimentary extends UBOre {

  public UBOreSedimentary(Block baseOre, IUBOreConfig config) {
    super(baseOre, config);
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.SEDIMENTARY_STONE.getBlock();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, SEDIMENTARY_VARIANT_PROPERTY);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, values()[meta]);
  }

  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, values()[placer.getHeldItemMainhand().getMetadata()]);
  }

}
