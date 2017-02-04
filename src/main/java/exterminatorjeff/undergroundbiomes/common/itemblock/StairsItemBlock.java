package exterminatorjeff.undergroundbiomes.common.itemblock;

import java.lang.reflect.InvocationTargetException;

import exterminatorjeff.undergroundbiomes.api.common.UBStairs;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStoneStairs;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
                ItemBlock test;
	}
        
        public StairsItemBlock(BlockEntry baseStoneEntry,HashMap<EnumFacing,Block> blocks) {
            super(baseStoneEntry);
            this.blocks = blocks;
        }

	@Override
	public Block getBlock(EnumFacing facing) {
		assert facing != EnumFacing.UP && facing != EnumFacing.DOWN;
		return blocks.get(facing);
	}
        
        @Override
        public Block getBlock() {
            return getBlock(EnumFacing.NORTH);
        }

	@Override
	protected Block getBlockToPlace(EntityPlayer playerIn, EnumFacing side) {
		return blocks.get(playerIn.getHorizontalFacing());//p;
	}

    @Override
    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        Block placedBlock = blocks.get(playerIn.getAdjustedHorizontalFacing());
        if (stack.stackSize != 0 && playerIn.canPlayerEdit(pos, facing, stack) && worldIn.canBlockBePlaced(placedBlock, pos, false, facing, (Entity)null, stack))
        {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = placedBlock.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType();
                worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                --stack.stackSize;
            }
            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        for (Block block: this.blocks.values()) {
            if (state.getBlock() == block)
            {
                setTileEntityNBT(world, player, pos, stack);
                block.onBlockPlacedBy(world, pos, state, player, stack);
                return true;
            }
        }
        throw new RuntimeException(state.getBlock().getUnlocalizedName());
        //return true;
    }
}
