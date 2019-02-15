package exterminatorjeff.undergroundbiomes.common.itemblock;

import exterminatorjeff.undergroundbiomes.api.common.UBSlab;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBStoneSlab;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;

/**
 * @author LouisDB
 */
public final class SlabItemBlock extends ItemSlab implements UBSlab {

  // Super class fields are private ...
  protected final UBStoneSlab half, full;

  public SlabItemBlock(UBStoneSlab half, UBStoneSlab full) {
    super(half, half, full);
    this.half = half;
    this.full = full;
    half.setItemBlock(this);
    full.setItemBlock(this);
  }

  @Override
  public Item toItem() {
    return this;
  }

  @Override
  public BlockSlab getHalfSlab() {
    return half;
  }

  @Override
  public BlockSlab getFullSlab() {
    return full;
  }

  @Override
  public int getNbVariants() {
    return half.getNbVariants();
  }

  @Override
  public String getVariantName(int meta) {
    return half.getVariantName(meta);
  }

}
