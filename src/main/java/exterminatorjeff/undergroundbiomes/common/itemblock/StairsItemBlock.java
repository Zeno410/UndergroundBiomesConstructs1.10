package exterminatorjeff.undergroundbiomes.common.itemblock;

import exterminatorjeff.undergroundbiomes.api.common.UBStairs;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStoneStairs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author LouisDB
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

  public StairsItemBlock(BlockEntry baseStoneEntry, HashMap<EnumFacing, Block> blocks) {
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

  /**
   * Called when a Block is right-clicked with this Item
   *
   * @param player
   * @param worldIn
   * @param pos
   * @param hand
   * @param facing
   * @param hitX
   * @param hitY
   * @param hitZ
   */
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();

    if (!block.isReplaceable(worldIn, pos)) {
      pos = pos.offset(facing);
    }

    ItemStack stack = player.getHeldItem(hand);

    Block placedBlock = blocks.get(player.getAdjustedHorizontalFacing());
    if (stack.getCount() != 0 && player.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(placedBlock, pos, false, facing, (Entity) null)) {
      int i = this.getMetadata(stack.getMetadata());
      IBlockState iblockstate1 = placedBlock.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player);

      if (placeBlockAt(stack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
        SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType();
        worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        stack.setCount(stack.getCount());
      }
      return EnumActionResult.SUCCESS;
    } else {
      return EnumActionResult.FAIL;
    }
  }

  public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
    if (!world.setBlockState(pos, newState, 3)) return false;

    IBlockState state = world.getBlockState(pos);
    for (Block block : this.blocks.values()) {
      if (state.getBlock() == block) {
        setTileEntityNBT(world, player, pos, stack);
        block.onBlockPlacedBy(world, pos, state, player, stack);
        return true;
      }
    }
    throw new RuntimeException(state.getBlock().getUnlocalizedName());
    //return true;
  }
}
