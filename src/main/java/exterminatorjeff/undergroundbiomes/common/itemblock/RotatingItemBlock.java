package exterminatorjeff.undergroundbiomes.common.itemblock;

import exterminatorjeff.undergroundbiomes.api.common.UBBlock;
import exterminatorjeff.undergroundbiomes.api.common.UBItem;
import exterminatorjeff.undergroundbiomes.api.common.Variable;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.block.button.UBStoneButton;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LouisDB
 */
abstract class RotatingItemBlock extends ItemBlock implements UBItem, Variable {

  public final UBStone baseStone;
  protected Map<EnumFacing, Block> blocks = new HashMap<>();

  public RotatingItemBlock(BlockEntry baseStoneEntry) {
    super(((UBBlock) baseStoneEntry.getBlock()).baseStone());
    baseStone = (UBStone) baseStoneEntry.getBlock();
    setMaxDamage(0);
    setHasSubtypes(true);
    setCreativeTab(UBCreativeTab.UB_BLOCKS_TAB);
  }

  public RotatingItemBlock(BlockEntry baseStoneEntry, Block block) {
    super(block);
    baseStone = (UBStone) baseStoneEntry.getBlock();
    setMaxDamage(0);
    setHasSubtypes(true);
    setCreativeTab(UBCreativeTab.UB_BLOCKS_TAB);
  }

  public RotatingItemBlock(UBStoneButton button) {
    super(button);
    baseStone = button.baseStone();
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
  public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
    blocks.get(EnumFacing.NORTH).getSubBlocks(itemIn, tab, list);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState stateHere = worldIn.getBlockState(pos);
    ItemStack stack = player.getHeldItem(hand);
    Block blockHere = stateHere.getBlock();
    Block blockToPlace = getBlockToPlace(player, facing);
    if (blockHere == Blocks.SNOW_LAYER && stateHere.getValue(BlockSnow.LAYERS).intValue() < 1)
      facing = EnumFacing.UP;
    else if (!blockHere.isReplaceable(worldIn, pos))
      pos = pos.offset(facing);
    if (stack.getCount() == 0)
      return EnumActionResult.FAIL;
    else if (!player.canPlayerEdit(pos, facing, stack))
      return EnumActionResult.FAIL;
    else if (pos.getY() == 255 && stateHere.getMaterial().isSolid())
      return EnumActionResult.FAIL;
    else if (worldIn.mayPlace(blockToPlace, pos, false, facing, (Entity) null)) {
      int meta = this.getMetadata(stack.getMetadata());
      IBlockState stateToPlace = blockToPlace.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, player);
      if (placeBlockAt(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, stateToPlace)) {
        SoundType soundType = blockHere.getSoundType();//blockHere.getSoundType(stateHere, worldIn, pos, player);
        worldIn.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        stack.setCount(stack.getCount() - 1);
      }
      return EnumActionResult.SUCCESS;
    } else
      return EnumActionResult.FAIL;
  }

  @Override
  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState newState) {
    if (!world.setBlockState(pos, newState, 3))
      return false;

    IBlockState state = world.getBlockState(pos);
    Block orientedBlock = blocks.get(facing);

    if (state.getBlock() == orientedBlock)
      orientedBlock.onBlockPlacedBy(world, pos, state, player, stack);

    return true;
  }

  protected abstract Block getBlockToPlace(EntityPlayer player, EnumFacing facing);


}
