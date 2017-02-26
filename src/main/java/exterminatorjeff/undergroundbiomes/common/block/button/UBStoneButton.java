package exterminatorjeff.undergroundbiomes.common.block.button;

import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import java.util.List;
import java.util.Random;

import exterminatorjeff.undergroundbiomes.common.UBSubBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.ButtonItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1 instance = 8 variants + powered -> fill 16 metadatas<br>
 * 6 orientations (1 per instance)<br>
 * 3 stone types (1 per instance)<br>
 * 3 stone styles (only stone for sedimentary)<br>
 * Total : 42 instances (7 subclasses)
 *
 * @author CurtisA, LouisDB
 *
 */
public abstract class UBStoneButton extends BlockButtonStone implements UBSubBlock {

	public final EnumFacing facing;
	private ButtonItemBlock itemBlock;

	public UBStoneButton(EnumFacing facing, ButtonItemBlock itemBlock) {
		this.facing = facing;
		this.itemBlock = itemBlock;
		setTickRandomly(true);
	}

    @Override
    public String getLocalizedName() {
        return "UB Button";
    }
    
    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return super.createStackedBlock(state); //To change body of generated methods, choose Tools | Templates.
    }
    
	@Override
	public Block toBlock() {
		return this;
	}

        public void setItemBlock(ButtonItemBlock itemBlock) {
            this.itemBlock = itemBlock;
        }
        
	@Override
	public ButtonItemBlock getItemBlock() {
		return itemBlock;
	}

    @Override
    public ItemStack getItem(World world, BlockPos bp, IBlockState ibs) {
        int meta = getMetaFromState(ibs);
        return new ItemStack(itemBlock,1,meta);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn() {
        return UBCreativeTab.UB_BLOCKS_TAB; //To change body of generated methods, choose Tools | Templates.
    }
	@Override
	protected abstract BlockStateContainer createBlockState();

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState().withProperty(FACING, facing);
		if (meta >= 8)
			return state.withProperty(POWERED, true);
		else
			return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = baseStone().getMetaFromState(state);
		if (state.getValue(POWERED).booleanValue())
			meta |= 8;
		return meta;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (int i = 0; i < getNbVariants(); ++i)
			list.add(new ItemStack(itemBlock, 1, i));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return itemBlock;
	}

	@Override
	public int damageDropped(IBlockState state) {
		// There is no powered button item
		return getMetaFromState(state) & 7;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
            return new ItemStack(itemBlock, 1, state.getBlock().getMetaFromState(state));
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		// The item choose the correct orientation
		assert side == facing;
		return true;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = getStateFromMeta(meta);
		return canPlaceBlock(worldIn, pos, facing.getOpposite()) ? state : state.withProperty(FACING, EnumFacing.DOWN);
	}

}
