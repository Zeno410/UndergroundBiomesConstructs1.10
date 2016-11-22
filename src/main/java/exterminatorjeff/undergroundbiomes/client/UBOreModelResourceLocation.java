package exterminatorjeff.undergroundbiomes.client;

import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

/**
 * 
 * @author LouisDB
 *
 */
public class UBOreModelResourceLocation extends ModelResourceLocation {

	public final UBOre ubOre;

	public UBOreModelResourceLocation(UBOre ubOre, int meta) {
		super(UBOreModel.UBORE_MODEL_PATH + "_" + ubOre.baseOre.getRegistryName(), ubOre.getVariantName(meta));
		this.ubOre = ubOre;
	}

}
