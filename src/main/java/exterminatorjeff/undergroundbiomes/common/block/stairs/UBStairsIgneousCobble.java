package exterminatorjeff.undergroundbiomes.common.block.stairs;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.StairsItemBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * @author CurtisA, LouisDB
 */
public class UBStairsIgneousCobble extends UBStairsIgneous {

  public UBStairsIgneousCobble(IBlockState modelState, EnumFacing facing, StairsItemBlock itemBlock) {
    super(modelState, facing, itemBlock);
  }

  @Override
  public UBStone baseStone() {
    return (UBStone) API.IGNEOUS_COBBLE.getBlock();
  }

}
