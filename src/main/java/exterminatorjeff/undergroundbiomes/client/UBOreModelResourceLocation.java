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

        private static String metaAddon(int meta) {
            if (meta <=0) return "";
            return ":" + meta;
        }

	public UBOreModelResourceLocation(UBOre ubOre, int meta) {
		super(UBOreModel.UBORE_MODEL_PATH + "_" + ubOre.baseOre.getRegistryName() + metaAddon(ubOre.baseOreMeta)
                        , ubOre.getVariantName(meta));
		this.ubOre = ubOre;
	}

}
