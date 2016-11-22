package exterminatorjeff.undergroundbiomes.common.itemblock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import exterminatorjeff.undergroundbiomes.api.common.UBItem;
import exterminatorjeff.undergroundbiomes.api.common.Variable;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author LouisDB
 *
 */
abstract class RotatingItemBlock extends Item implements UBItem, Variable {

	public final UBStone baseStone;
	protected final Map<EnumFacing, Block> blocks = new HashMap<>();

	public RotatingItemBlock(BlockEntry baseStoneEntry) {
		baseStone = (UBStone) baseStoneEntry.getBlock();
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(UBCreativeTab.UB_BLOCKS_TAB);
	}

	@Override
	public Item toItem() {
		return this;
	}

	@Override
	public int getNbVariants() {
		return baseStone.getNbVariants();
	}

	@Override
	public String getVariantName(int meta) {
		return baseStone.getVariantName(meta);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return StringUtils.substringBeforeLast(blocks.get(EnumFacing.NORTH).getUnlocalizedName(), "_") + "." + getVariantName(stack.getMetadata());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		blocks.get(EnumFacing.NORTH).getSubBlocks(itemIn, tab, list);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState stateHere = worldIn.getBlockState(pos);
		Block blockHere = stateHere.getBlock();
		Block blockToPlace = getBlockToPlace(playerIn, side);
		if (blockHere == Blocks.SNOW_LAYER && stateHere.getValue(BlockSnow.LAYERS).intValue() < 1)
			side = EnumFacing.UP;
		else if (!blockHere.isReplaceable(worldIn, pos))
			pos = pos.offset(side);
		if (stack.stackSize == 0)
			return EnumActionResult.FAIL;
		else if (!playerIn.canPlayerEdit(pos, side, stack))
			return EnumActionResult.FAIL;
		else if (pos.getY() == 255 && stateHere.getMaterial().isSolid())
			return EnumActionResult.FAIL;
		else if (worldIn.canBlockBePlaced(blockToPlace, pos, false, side, (Entity) null, stack)) {
			int meta = this.getMetadata(stack.getMetadata());
			IBlockState stateToPlace = blockToPlace.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, meta, playerIn);
			if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, stateToPlace)) {
				SoundType soundType = blockHere.getSoundType();//blockHere.getSoundType(stateHere, worldIn, pos, playerIn);
				worldIn.playSound(playerIn, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
				--stack.stackSize;
			}
			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}

	private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState, 3))
			return false;

		IBlockState state = world.getBlockState(pos);
		Block orientedBlock = blocks.get(side);

		if (state.getBlock() == orientedBlock)
			orientedBlock.onBlockPlacedBy(world, pos, state, player, stack);

		return true;
	}

	protected abstract Block getBlockToPlace(EntityPlayer playerIn, EnumFacing facing);

}
