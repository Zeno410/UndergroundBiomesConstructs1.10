package exterminatorjeff.undergroundbiomes.common.block.stairs;

import java.util.List;
import java.util.Random;

import exterminatorjeff.undergroundbiomes.common.UBSubBlock;
import exterminatorjeff.undergroundbiomes.common.itemblock.StairsItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 1 instance -> 8 variants + half -> 16 metadatas</br>
 * 4 facings</br>
 * 3 stone types</br>
 * 3 stone style (only stone for sedimentary)</br>
 * Total : 28 instances (7 subclasses)
 *
 * @author CurtisA, LouisDB
 */
public abstract class UBStoneStairs extends BlockStairs implements UBSubBlock {

  public final EnumFacing facing;
  private final StairsItemBlock itemBlock;

  public UBStoneStairs(IBlockState modelState, EnumFacing facing, StairsItemBlock itemBlock) {
    super(modelState);
    this.facing = facing;
    this.itemBlock = itemBlock;
    setHardness(baseStone().getBaseHardness());
    setResistance(baseStone().getBaseResistance() / 3.0F);
    useNeighborBrightness = true;
  }

  @Override
  public Block toBlock() {
    return this;
  }

  @Override
  public StairsItemBlock getItemBlock() {
    return itemBlock;
  }

  @Override
  protected abstract BlockStateContainer createBlockState();

  @Override
  public IBlockState getStateFromMeta(int meta) {
    IBlockState state = getDefaultState().withProperty(FACING, facing);
    if (meta >= 8)
      state = state.withProperty(HALF, EnumHalf.TOP);
    return state;
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    IBlockState result = super.getActualState(state, worldIn, pos);
    return result;
  }


  @Override
  public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
    IBlockState result = super.getExtendedState(state, world, pos);
    return result;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = baseStone().getMetaFromState(state);
    if (state.getValue(HALF) == EnumHalf.TOP)
      i |= 8;
    return i;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
    for (int i = 0; i < getNbVariants(); ++i)
      list.add(new ItemStack(itemIn, 1, i));
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return itemBlock;
  }

  @Override
  public int damageDropped(IBlockState state) {
    // There is no reverse stairs item
    return getMetaFromState(state) & 7;
  }

  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    return new ItemStack(itemBlock, 1, getMetaFromState(state));
  }

  /**
   * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
   * IBlockstate
   *
   * @param worldIn
   * @param pos
   * @param facing
   * @param hitX
   * @param hitY
   * @param hitZ
   * @param meta
   * @param placer
   * @return
   */
  @Override
  public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing());//.withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
    //iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
    //if ((double) hitY <= 0.5D) throw new RuntimeException();
    //if (1>0) throw new RuntimeException(facing.getName() + " " + iblockstate.getValue(FACING).getName() + " " + this.facing.getName());
    iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, BlockStairs.EnumShape.STRAIGHT);
    return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM) : iblockstate.withProperty(HALF, BlockStairs.EnumHalf.TOP);
    //return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? iblockstate.withProperty(HALF, BlockStairs.EnumHalf.BOTTOM) : iblockstate.withProperty(HALF, BlockStairs.EnumHalf.TOP);
  }
}
