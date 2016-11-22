package exterminatorjeff.undergroundbiomes.common.block.slab;

import static exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant.*;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public abstract class UBMetamorphicStoneSlab extends UBStoneSlab {

	public UBMetamorphicStoneSlab() {
		setDefaultState(getDefaultState().withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[0]));
	}

	@Override
	public UBStone baseStone() {
		return (UBStone) API.METAMORPHIC_STONE.getBlock();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return METAMORPHIC_VARIANT_PROPERTY;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return METAMORPHIC_VARIANTS[stack.getMetadata() & 7];
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(METAMORPHIC_VARIANT_PROPERTY, METAMORPHIC_VARIANTS[meta & 7]);
	}
}
