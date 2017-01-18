package exterminatorjeff.undergroundbiomes.common.block.wall;

import java.util.List;

import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.common.UBSubBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public abstract class UBStoneWall extends BlockWall implements UBSubBlock {

	private final ItemBlock itemBlock;

	public UBStoneWall(BlockEntry baseStoneEntry) {
		super(baseStoneEntry.getBlock());
		this.itemBlock = new UBItemWall(this);
		setCreativeTab(UBCreativeTab.UB_BLOCKS_TAB);
		setDefaultState(blockState.getBaseState()//
				.withProperty(UP, false)//
				.withProperty(NORTH, false)//
				.withProperty(EAST, false)//
				.withProperty(SOUTH, false)//
				.withProperty(WEST, false));
	}

	@Override
	public Block toBlock() {
		return this;
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock;
	}

	@Override
	protected abstract BlockStateContainer createBlockState();

	@Override
	public int getMetaFromState(IBlockState state) {
		return baseStone().getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (int i = 0; i < getNbVariants(); ++i)
			list.add(new ItemStack(itemIn, 1, i));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public String getLocalizedName() {
		return I18n.format(this.getUnlocalizedName() + ".name");
	}

	private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof UBStoneWall)
			return true;
		if (block == Blocks.BARRIER)
			return false;
		if (block != this && !(block instanceof BlockFenceGate)) {
			if (state.getMaterial().isOpaque() && state.isFullCube())
				return state.getMaterial() != Material.GOURD;
			else
				return false;
		}
		return true;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean north = canConnectTo(worldIn, pos.north());
		boolean east = canConnectTo(worldIn, pos.east());
		boolean south = canConnectTo(worldIn, pos.south());
		boolean west = canConnectTo(worldIn, pos.west());
                
		boolean straight = north && !east && south && !west || !north && east && !south && west;
		return state.withProperty(UP, !straight || !worldIn.isAirBlock(pos.up())) //
				.withProperty(NORTH, north) //
				.withProperty(EAST, east) //
				.withProperty(SOUTH, south) //
				.withProperty(WEST, west);
	}


    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true; //To change body of generated methods, choose Tools | Templates.
    }
	/**
	 * 
	 * @author CurtisA, LouisDB
	 *
	 */
	private class UBItemWall extends ItemBlock {

		public UBItemWall(UBStoneWall block) {
			super(block);
			setMaxDamage(0);
			setHasSubtypes(true);
		}

		@Override
		public int getMetadata(int damage) {
			return damage;
		}

		@Override
		public String getUnlocalizedName(ItemStack stack) {
			return super.getUnlocalizedName(stack) + "." + getVariantName(stack.getMetadata());
		}

	}

}
