package exterminatorjeff.undergroundbiomes.common.block;

import com.google.common.base.Predicate;

import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class MetamorphicCobble extends MetamorphicStone {
	@Override
	public UBStoneStyle getStoneStyle() {
		return UBStoneStyle.COBBLE;
	}

	@Override
	public Block setHardness(float hardness) {
		return super.setHardness(hardness * COBBLE_HARDNESS_MODIFIER);
	}

	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target) {
		return false;
	}

}
