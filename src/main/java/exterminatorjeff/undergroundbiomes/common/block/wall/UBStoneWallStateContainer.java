package exterminatorjeff.undergroundbiomes.common.block.wall;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * To not add the VARIANT property from BlockWall
 *
 * @author LouisDB
 *
 */
class UBStoneWallStateContainer extends BlockStateContainer {

	public UBStoneWallStateContainer(Block blockIn, IProperty<?>... properties) {
		super(blockIn, properties);
	}

	@Override
	protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
		return new CustomStateImplementation(block, properties);
	}

	private class CustomStateImplementation extends StateImplementation {

		protected CustomStateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
			super(blockIn, propertiesIn);
		}

		@Override
		public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
			if (property == BlockWall.VARIANT)
				return this; // Do not apply property
			return super.withProperty(property, value);
		}

	}

}
