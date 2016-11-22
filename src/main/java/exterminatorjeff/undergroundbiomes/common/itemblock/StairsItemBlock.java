package exterminatorjeff.undergroundbiomes.common.itemblock;

import java.lang.reflect.InvocationTargetException;

import exterminatorjeff.undergroundbiomes.api.common.UBStairs;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStoneStairs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author LouisDB
 *
 */
public class StairsItemBlock extends RotatingItemBlock implements UBStairs {

	public StairsItemBlock(BlockEntry baseStoneEntry, Class<? extends UBStoneStairs> blockClass) {
		super(baseStoneEntry);
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			Block block = null;
			try {
				block = (Block) blockClass.getConstructor(IBlockState.class, EnumFacing.class, StairsItemBlock.class) //
						.newInstance(baseStoneEntry.getBlock().getDefaultState(), facing, this);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			blocks.put(facing, block);
		}
	}

	@Override
	public Block getBlock(EnumFacing facing) {
		assert facing != EnumFacing.UP && facing != EnumFacing.DOWN;
		return blocks.get(facing);
	}

	@Override
	protected Block getBlockToPlace(EntityPlayer playerIn, EnumFacing side) {
		return blocks.get(playerIn.getHorizontalFacing());
	}

}
