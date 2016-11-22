package exterminatorjeff.undergroundbiomes.common.block;

import static exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant.*;

import java.util.Random;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class SedimentaryStone extends UBStone {

	public SedimentaryStone() {
		setDefaultState(blockState.getBaseState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, LIMESTONE));
	}

	@Override
	public UBStoneType getStoneType() {
		return UBStoneType.SEDIMENTARY;
	}

	@Override
	public UBStoneStyle getStoneStyle() {
		return UBStoneStyle.STONE;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SEDIMENTARY_VARIANT_PROPERTY);
	}

	@Override
	public int getNbVariants() {
		return NB_VARIANTS;
	}

	@Override
	public String getVariantName(int meta) {
		return SEDIMENTARY_VARIANTS[meta & 7].toString();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, SEDIMENTARY_VARIANTS[meta & 7]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SEDIMENTARY_VARIANT_PROPERTY).getMetadata();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if (state.getValue(SEDIMENTARY_VARIANT_PROPERTY) == LIGNITE)
			return API.LIGNITE_COAL.getItem();
		else
			return super.getItemDropped(state, rand, fortune);
	}

	@Override
	public boolean isFortuneAffected(IBlockState state) {
		return state.getValue(SEDIMENTARY_VARIANT_PROPERTY) == LIGNITE;
	}

	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
		return getBaseHardness() * state.getValue(SEDIMENTARY_VARIANT_PROPERTY).getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		return getBaseResistance() * world.getBlockState(pos).getValue(SEDIMENTARY_VARIANT_PROPERTY).getResistance();
	}

}
