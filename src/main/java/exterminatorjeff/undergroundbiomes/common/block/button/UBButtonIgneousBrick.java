package exterminatorjeff.undergroundbiomes.common.block.button;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class UBButtonIgneousBrick extends UBButtonIgneous {

	public UBButtonIgneousBrick(EnumFacing facing, ButtonItemBlock itemBlock) {
		super(facing, itemBlock);
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.IGNEOUS_BRICK.getBlock();
	}

}
