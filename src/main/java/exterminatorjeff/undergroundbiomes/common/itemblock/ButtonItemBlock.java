package exterminatorjeff.undergroundbiomes.common.itemblock;

import java.lang.reflect.InvocationTargetException;

import exterminatorjeff.undergroundbiomes.api.common.UBButton;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.common.block.button.UBStoneButton;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LouisDB
 */
public class ButtonItemBlock extends RotatingItemBlock implements UBButton {


  public ButtonItemBlock(BlockEntry baseStoneEntry, Class<? extends UBStoneButton> blockClass) {
    // this is a weird hack because this system is trying to make a final entry in ItemBlock
    // refering to the block and a final in block referring to the ItemBlock. And that's impossible.
    // I'm forced to send a fake block because I don't want to completely redo the system.
    // Earlier it was ultimately sending the base stone and that was creating a total mess
    super(new ButtonMaker().buttonFrom(EnumFacing.NORTH, blockClass));
    ((UBStoneButton) (this.block)).setItemBlock(this);
    for (EnumFacing facing : EnumFacing.VALUES) {
      Block block = null;
      try {
        block = (Block) blockClass.getConstructor(EnumFacing.class, ButtonItemBlock.class) //
          .newInstance(facing, this);
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
        e.printStackTrace();
      }
      blocks.put(facing, block);
    }
  }

  private ButtonItemBlock(Map<EnumFacing, Block> blocks) {
    super((UBStoneButton) (blocks.get(EnumFacing.NORTH)));
    this.blocks = blocks;
  }

  @Override
  public Block getBlock(EnumFacing facing) {
    return blocks.get(facing);
  }

  @Override
  protected Block getBlockToPlace(EntityPlayer playerIn, EnumFacing side) {
    return blocks.get(side);
  }

  private static class ButtonMaker {
    UBStoneButton buttonFrom(EnumFacing facing, Class<? extends UBStoneButton> blockClass) {
      Block block = null;
      ButtonItemBlock itemBlock = null;
      try {
        block = (Block) blockClass.getConstructor(EnumFacing.class, ButtonItemBlock.class) //
          .newInstance(facing, itemBlock);
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
      return (UBStoneButton) block;
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public CreativeTabs getCreativeTab() {
    return UBCreativeTab.UB_BLOCKS_TAB; //To change body of generated methods, choose Tools | Templates.
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
    block.getSubBlocks(itemIn, tab, list);
  }
}
