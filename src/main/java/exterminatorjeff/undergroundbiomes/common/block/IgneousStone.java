package exterminatorjeff.undergroundbiomes.common.block;

import exterminatorjeff.undergroundbiomes.api.API;
import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.*;

import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneType;
import exterminatorjeff.undergroundbiomes.intermod.DropsRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class IgneousStone extends UBStone {

	public IgneousStone() {
		setDefaultState(blockState.getBaseState().withProperty(IGNEOUS_VARIANT_PROPERTY, RED_GRANITE));
	}

	@Override
	public UBStoneType getStoneType() {
		return UBStoneType.IGNEOUS;
	}

	@Override
	public UBStoneStyle getStoneStyle() {
		return UBStoneStyle.STONE;
	}

	@Override
	public int getNbVariants() {
		return NB_VARIANTS;
	}

	@Override
	public String getVariantName(int meta) {
		return IGNEOUS_VARIANTS[meta & 7].toString();
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, IGNEOUS_VARIANT_PROPERTY);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[meta & 7]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(IGNEOUS_VARIANT_PROPERTY).getMetadata();
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Block cobbleBlock = API.IGNEOUS_COBBLE.getBlock();
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack itemStack = new ItemStack(cobbleBlock,1,meta);
        List<ItemStack> result = new ArrayList();
        result.add(itemStack);
		DropsRegistry.INSTANCE.addDrops(result, this, world, pos, state, fortune);
        return result;
        //return super.getDrops(world, pos, state, fortune); //To change body of generated methods, choose Tools | Templates.
    }
	@Override
	public boolean isFortuneAffected(IBlockState state) {
		return false;
	}

	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
		return getBaseHardness() * state.getValue(IGNEOUS_VARIANT_PROPERTY).getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		return getBaseResistance() * world.getBlockState(pos).getValue(IGNEOUS_VARIANT_PROPERTY).getResistance();
	}

}
