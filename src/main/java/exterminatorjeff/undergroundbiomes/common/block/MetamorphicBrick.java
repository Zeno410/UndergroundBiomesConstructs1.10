package exterminatorjeff.undergroundbiomes.common.block;

import com.google.common.base.Predicate;
import exterminatorjeff.undergroundbiomes.api.API;

import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.intermod.DropsRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class MetamorphicBrick extends MetamorphicStone {
	@Override
	public UBStoneStyle getStoneStyle() {
		return UBStoneStyle.BRICK;
	}

	@Override
	public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target) {
		return false;
	}
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Block cobbleBlock = API.METAMORPHIC_BRICK.getBlock();
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack itemStack = new ItemStack(cobbleBlock,1,meta);
        List<ItemStack> result = new ArrayList();
        result.add(itemStack);
        return result;
        //return super.getDrops(world, pos, state, fortune); //To change body of generated methods, choose Tools | Templates.
    }
}
