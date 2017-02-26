package exterminatorjeff.undergroundbiomes.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.common.UBSubBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Base class to create UBified version of vanilla and modded ores.<br>
 * <br>
 * One instance per ore. Each instance has its metadata used for UB stone
 * variants.<br>
 * If the base ore also use metadata, then it's one instance per baseOre:meta
 * pairs.
 * 
 * @author CurtisA, LouisDB
 *
 * @see {@link UBOreIgneous}, {@link UBOreMetamorphic}, {@link UBOreSedimentary}
 */
public abstract class UBOre extends Block implements UBSubBlock {

	public static final int NO_METADATA = -1;

	public final Block baseOre;
	/** {@value #NO_METADATA} if the base ore do not have metadata */
	public final int baseOreMeta;
        public final IBlockState baseOreState;
	protected final ItemBlock itemBlock;

	public UBOre(Block baseOre, int baseOreMeta) {
		super(Material.ROCK);
		setHarvestLevel(baseOre.getHarvestTool(baseOre.getDefaultState()), baseOre.getHarvestLevel(baseOre.getDefaultState()));
		setCreativeTab(UBCreativeTab.UB_ORES_TAB);
		this.itemBlock = new UBItemOre(this);
		this.baseOre = baseOre;
		this.baseOreMeta = baseOreMeta;
                this.baseOreState = baseOre.getStateFromMeta(baseOreMeta);
	}

	@Override
	public Block toBlock() {
		return this;
	}

	@Override
	public final ItemBlock getItemBlock() {
		return itemBlock;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	protected abstract BlockStateContainer createBlockState();

	@Override
	public abstract IBlockState getStateFromMeta(int meta);

	@Override
	public int getMetaFromState(IBlockState state) {
		return baseStone().toBlock().getMetaFromState(state);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
                return baseOre.quantityDropped(state, fortune, random);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (int i = 0; i < getNbVariants(); ++i)
			list.add(new ItemStack(this, 1, i));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		Item item = Item.getItemFromBlock(baseOre);
		Item drop = baseOre.getItemDropped(state, rand, fortune);
		if (drop != item){
                    return drop;
                } else {
                    return drop;
}
	}
        
        

	@Override
	public int damageDropped(IBlockState state) {
            int test = this.baseOre.damageDropped(state);
            if (test>0) return test;
		if (baseOreMeta == NO_METADATA)
			return 0;
		else
			return baseOreMeta;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		Item itemBlock = Item.getItemFromBlock(this);
		int count = quantityDropped(state, fortune, rand);
		for (int i = 0; i < count; ++i) {
			Item itemDropped = getItemDropped(state, rand, fortune);
			if (itemDropped != null) {
				// The ore block do not drop the block item (like diamond)
				if (itemDropped != itemBlock )
					drops.add(new ItemStack(itemDropped, 1, damageDropped(state)));
				else
					drops.add(new ItemStack(itemDropped, 1, getMetaFromState(state)));
			}
		}
		return drops;
	}

	@Override
	public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
		baseOre.dropXpOnBlockBreak(worldIn, pos, amount);
	}

	@SuppressWarnings("deprecation")
	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
		return baseOre.getBlockHardness(state, worldIn, pos);
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return baseOre.getExpDrop(state, world, pos, fortune);
	}

	@Override
	public float getExplosionResistance(Entity exploder) {
		return baseOre.getExplosionResistance(exploder);
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		return baseOre.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		baseOre.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(itemBlock, 1, getMetaFromState(world.getBlockState(pos)));
	}

	@Override
	public abstract IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer);

	/**
	 * 
	 * @author CurtisA, LouisDB
	 *
	 */
	private class UBItemOre extends ItemBlock {

		public UBItemOre(UBOre ubOre) {
			super(ubOre);
			setMaxDamage(0);
			setHasSubtypes(true);
		}

		@Override
		public int getMetadata(int damage) {
			return damage;
		}

        @Override
        public int getDamage(ItemStack stack) {
            return stack.getMetadata();
        }
		@Override
		public String getItemStackDisplayName(ItemStack stack) {
			// Works for English but maybe not all languages. Some may have to
			// use the lang file to override this behavior
			// TODO Find a cleaner way to do this
                        try {
                            
                            if (baseOreMeta == NO_METADATA) {
			        return I18n.format(baseStone().getItemBlock().getUnlocalizedName(stack) + ".name") + " " + I18n.format(baseOre.getUnlocalizedName() + ".name");
                            }
                            ItemStack baseStack = new ItemStack(baseOre,1,baseOreMeta);
			    return I18n.format(baseStone().getItemBlock().getUnlocalizedName(stack) + ".name") + " " + baseStack.getDisplayName();
                        } catch (Error error) {
                            if (baseOreMeta == NO_METADATA) {
                                 return baseStone().getItemBlock().getUnlocalizedName(stack) + ".name" + " " + baseOre.getUnlocalizedName() + ".name";
                            }
                            ItemStack baseStack = new ItemStack(baseOre,1,baseOreMeta);
                                 return baseStone().getItemBlock().getUnlocalizedName(stack) + ".name" + " " + baseStack.getDisplayName();
                        }
		}

	}

}
