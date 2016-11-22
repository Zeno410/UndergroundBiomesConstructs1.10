package exterminatorjeff.undergroundbiomes.common.block.wall;

import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.*;

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
public class UBWallMetamorphic extends UBStoneWall {

	public UBWallMetamorphic(BlockEntry baseStoneEntry) {
		super(baseStoneEntry);
		setDefaultState(getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.METAMORPHIC_STONE.getBlock();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new UBStoneWallStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, METAMORPHIC_VARIANT_PROPERTY);
	}

	@Override
	public IBlockState getStateFromMeta(int metadata) {
		return getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[metadata & 7]);
	}

}
