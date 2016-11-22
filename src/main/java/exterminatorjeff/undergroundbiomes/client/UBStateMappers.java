package exterminatorjeff.undergroundbiomes.client;

import org.apache.commons.lang3.StringUtils;

import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import exterminatorjeff.undergroundbiomes.common.block.button.UBStoneButton;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBStoneSlab;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStoneStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

/**
 * 
 * @author LouisDB
 *
 */
public final class UBStateMappers {

	public static final IStateMapper UBSLAB_STATE_MAPPER = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			UBStoneSlab slab = (UBStoneSlab) state.getBlock();
			String path = slab.getRegistryName() + "_alt";
			String variants;
			if (slab.isDouble())
				variants = makeVariantsString("type", slab.getVariantName(slab.getMetaFromState(state)));
			else
				variants = makeVariantsString( //
						"half", state.getValue(UBStoneSlab.HALF), //
						"type", slab.getVariantName(slab.getMetaFromState(state)));
			return new ModelResourceLocation(path, variants);
		}
	};

	public static final IStateMapper UBBUTTON_STATE_MAPPER = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			UBStoneButton button = (UBStoneButton) state.getBlock();
			String variants = makeVariantsString( //
					"facing", button.facing, //
					"powered", state.getValue(UBStoneButton.POWERED), //
					"type", button.getVariantName(button.getMetaFromState(state)));
			String path = StringUtils.substringBeforeLast(button.getRegistryName().toString(), "_");
			return new ModelResourceLocation(path, variants);
		}
	};

	public static final IStateMapper UBSTAIRS_STATE_MAPPER = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			UBStoneStairs stairs = (UBStoneStairs) state.getBlock();
			String variants = makeVariantsString( //
					"facing", stairs.facing, //
					"half", state.getValue(UBStoneStairs.HALF), //
					"shape", state.getValue(UBStoneStairs.SHAPE), //
					"type", stairs.getVariantName(stairs.getMetaFromState(state)));
			String path = StringUtils.substringBeforeLast(stairs.getRegistryName().toString(), "_");
			return new ModelResourceLocation(path, variants);
		}
	};

	public static final IStateMapper UBORE_STATE_MAPPER = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			UBOre ubOre = (UBOre) state.getBlock();
			return new UBOreModelResourceLocation(ubOre, ubOre.getMetaFromState(state));
		}
	};

	private static String makeVariantsString(Object... args) {
		assert args.length % 2 == 0;
		assert args.length >= 2;
		String result = args[0] + "=" + args[1];
		for (int i = 2; i < args.length; i += 2)
			result += "," + args[i] + "=" + args[i + 1];
		return result;
	}

	private UBStateMappers() {
	}

}
