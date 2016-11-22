package exterminatorjeff.undergroundbiomes.common.block.stairs;

import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.*;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.StairsItemBlock;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class UBStairsMetamorphic extends UBStoneStairs {

	public UBStairsMetamorphic(IBlockState modelState, EnumFacing facing, StairsItemBlock itemBlock) {
		super(modelState, facing, itemBlock);
		setDefaultState(getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.METAMORPHIC_STONE.getBlock();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, HALF, SHAPE, METAMORPHIC_VARIANT_PROPERTY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[meta & 7]);
	}
}
