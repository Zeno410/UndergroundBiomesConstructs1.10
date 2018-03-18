package exterminatorjeff.undergroundbiomes.common.block.slab;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANTS;
import static exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant.IGNEOUS_VARIANT_PROPERTY;

/**
 *
 * @author CurtisA, LouisDB
 *
 */
public abstract class UBIgneousStoneSlab extends UBStoneSlab {

	public UBIgneousStoneSlab() {
		setDefaultState(getDefaultState().withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.IGNEOUS_STONE.getBlock();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return IGNEOUS_VARIANT_PROPERTY;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return IGNEOUS_VARIANTS[stack.getMetadata() & 7];
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(IGNEOUS_VARIANT_PROPERTY, IGNEOUS_VARIANTS[meta & 7]);
	}

}
