package exterminatorjeff.undergroundbiomes.common.block.button;

import net.minecraft.util.EnumFacing;
import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class UBButtonIgneousCobble extends UBButtonIgneous {

	public UBButtonIgneousCobble(EnumFacing facing, ButtonItemBlock itemBlock) {
		super(facing, itemBlock);
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.IGNEOUS_COBBLE.getBlock();
	}

}
