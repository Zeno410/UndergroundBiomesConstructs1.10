package exterminatorjeff.undergroundbiomes.common.block.wall;

import static exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant.*;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class UBWallSedimentary extends UBStoneWall {

	public UBWallSedimentary(BlockEntry baseStoneEntry) {
		super(baseStoneEntry);
		setDefaultState(getDefaultState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, SEDIMENTARY_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.SEDIMENTARY_STONE.getBlock();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new UBStoneWallStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, SEDIMENTARY_VARIANT_PROPERTY);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata) {
		return getDefaultState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, SEDIMENTARY_VARIANTS[metadata & 7]);
	}

}
