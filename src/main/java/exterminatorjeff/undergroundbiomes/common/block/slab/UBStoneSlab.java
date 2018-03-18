package exterminatorjeff.undergroundbiomes.common.block.slab;

import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.common.UBSubBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.SlabItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author CurtisA, LouisDB
 *
 */
public abstract class UBStoneSlab extends BlockSlab implements UBSubBlock {

	private SlabItemBlock itemBlock;

	public UBStoneSlab() {
		super(Material.ROCK);
		Block baseStone = baseStone().toBlock();
		setHarvestLevel(baseStone.getHarvestTool(baseStone.getDefaultState()), baseStone.getHarvestLevel(baseStone.getDefaultState()));
		if (!isDouble()) {
			setDefaultState(blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM));
			setCreativeTab(UBCreativeTab.UB_BLOCKS_TAB);
		}
		useNeighborBrightness = !isDouble();
	}

	public final void setItemBlock(SlabItemBlock itemBlock) {
		this.itemBlock = itemBlock;
	}

	@Override
	public Block toBlock() {
		return this;
	}



    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int numberDropped = isDouble() ? 2 : 1;
        ItemStack dropped = new ItemStack(itemBlock,numberDropped,damageDropped(state));
        List<ItemStack> result = new ArrayList(1);
        result.add(dropped);
        return result;
    }
	@Override
	public final ItemBlock getItemBlock() {
		return itemBlock;
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName() + "." + getVariantName(meta);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		if (isDouble())
			return new BlockStateContainer(this, getVariantProperty());
		else
			return new BlockStateContainer(this, getVariantProperty(), HALF);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		if (!isDouble())
			return state.withProperty(HALF, (meta < 8 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP));
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = baseStone().getMetaFromState(state);

		if (!isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP)
			meta |= 8;

		return meta;
	}

	@Override
	public int damageDropped(IBlockState state) {
		int result = getMetaFromState(state);
                if (result >8 ) result -=8;
                return result;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < getNbVariants(); ++i)
			list.add(new ItemStack(itemIn, 1, i));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(itemBlock, 1, state.getBlock().getMetaFromState(state));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return itemBlock;
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return baseStone().getBlockHardness(blockState, worldIn, pos);
	}

	@Override
	public float getExplosionResistance(Entity exploder) {
		return baseStone().getExplosionResistance(exploder);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		return baseStone().getExplosionResistance(world, pos, exploder, explosion);
	}

}
