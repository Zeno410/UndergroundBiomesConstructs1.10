package exterminatorjeff.undergroundbiomes.common.block.slab;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import static exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant.SEDIMENTARY_VARIANTS;
import static exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant.SEDIMENTARY_VARIANT_PROPERTY;

/**
 *
 * @author CurtisA, LouisDB
 *
 */
public abstract class UBSedimentaryStoneSlab extends UBStoneSlab {

	public UBSedimentaryStoneSlab() {
		setDefaultState(getDefaultState().withProperty(SEDIMENTARY_VARIANT_PROPERTY, SEDIMENTARY_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.SEDIMENTARY_STONE.getBlock();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return SEDIMENTARY_VARIANT_PROPERTY;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return SEDIMENTARY_VARIANTS[stack.getMetadata() & 7];
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(SEDIMENTARY_VARIANT_PROPERTY, SEDIMENTARY_VARIANTS[meta & 7]);
	}

}
