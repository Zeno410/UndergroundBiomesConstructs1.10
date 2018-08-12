package exterminatorjeff.undergroundbiomes.common.block;

import com.google.common.base.Predicate;
import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneType;
import exterminatorjeff.undergroundbiomes.intermod.DropsRegistry;
import exterminatorjeff.undergroundbiomes.intermod.OresRegistry;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.*;

/**
 * @author CurtisA, LouisDB
 */
public class MetamorphicStone extends UBStone {
  public static final String internal_name = "metamorphic_stone";

  @Override
  public String getInternalName() {
    return internal_name;
  }

  public MetamorphicStone() {
    setDefaultState(blockState.getBaseState().withProperty(METAMORPHIC_VARIANT_PROPERTY, GNEISS));
  }

  @Override
  public UBStoneType getStoneType() {
    return UBStoneType.METAMORPHIC;
  }

  @Override
  public UBStoneStyle getStoneStyle() {
    return UBStoneStyle.STONE;
  }

  @Override
  public BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, METAMORPHIC_VARIANT_PROPERTY);
  }

  @Override
  public int getNbVariants() {
    return NB_VARIANTS;
  }

  @Override
  public String getVariantName(int meta) {
    return METAMORPHIC_VARIANTS[meta & 7].toString();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[meta & 7]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(METAMORPHIC_VARIANT_PROPERTY).getMetadata();
  }

  @Override
  public boolean isFortuneAffected(IBlockState state) {
    return false;
  }

  @Override
  public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
    return getBaseHardness() * state.getValue(METAMORPHIC_VARIANT_PROPERTY).getHardness();
  }

  @Override
  public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
    return getBaseResistance() * world.getBlockState(pos).getValue(METAMORPHIC_VARIANT_PROPERTY).getResistance();
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    Item cobbleBlock = API.METAMORPHIC_COBBLE.getItemBlock();
    int meta = state.getBlock().getMetaFromState(state);
    ItemStack itemStack = new ItemStack(cobbleBlock, 1, meta);
    List<ItemStack> result = new ArrayList();
    result.add(itemStack);
    DropsRegistry.INSTANCE.addDrops(result, this, world, pos, state, fortune);
    return result;
  }

  @Override
  public void getDrops(NonNullList<ItemStack> stacks, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    Item cobbleBlock = API.METAMORPHIC_COBBLE.getItemBlock();
    int meta = state.getBlock().getMetaFromState(state);
    ItemStack itemStack = new ItemStack(cobbleBlock, 1, meta);
    stacks.add(itemStack);
    DropsRegistry.INSTANCE.addDrops(stacks, this, world, pos, state, fortune);
  }

  @Override
  public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target) {
    OresRegistry.INSTANCE.setRecheck(world, pos);
    return super.isReplaceableOreGen(state, world, pos, target); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public UBStone baseStone() {
    return this;
  }
}
