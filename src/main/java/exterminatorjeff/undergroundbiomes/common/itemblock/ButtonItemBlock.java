package exterminatorjeff.undergroundbiomes.common.itemblock;

import java.lang.reflect.InvocationTargetException;

import exterminatorjeff.undergroundbiomes.api.common.UBButton;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.common.block.button.UBStoneButton;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author LouisDB
 *
 */
public class ButtonItemBlock extends RotatingItemBlock implements UBButton {

	public ButtonItemBlock(BlockEntry baseStoneEntry, Class<? extends UBStoneButton> blockClass) {
		super(baseStoneEntry);
		for (EnumFacing facing : EnumFacing.VALUES) {
			Block block = null;
			try {
				block = (Block) blockClass.getConstructor(EnumFacing.class, ButtonItemBlock.class) //
						.newInstance(facing, this);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			blocks.put(facing, block);
		}
	}

	@Override
	public Block getBlock(EnumFacing facing) {
		return blocks.get(facing);
	}

	@Override
	protected Block getBlockToPlace(EntityPlayer playerIn, EnumFacing side) {
		return blocks.get(side);
	}

}
