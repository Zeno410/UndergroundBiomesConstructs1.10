package exterminatorjeff.undergroundbiomes.common.block.button;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.*;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class UBButtonIgneous extends UBStoneButton {

	public UBButtonIgneous(EnumFacing facing, ButtonItemBlock itemBlock) {
		super(facing, itemBlock);
		setDefaultState(getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.IGNEOUS_STONE.getBlock();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, POWERED, IGNEOUS_VARIANT_PROPERTY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[meta & 7]);
	}

}
