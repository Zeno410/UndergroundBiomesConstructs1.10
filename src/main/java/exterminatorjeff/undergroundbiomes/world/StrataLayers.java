package exterminatorjeff.undergroundbiomes.world;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.StrataLayer;
import exterminatorjeff.undergroundbiomes.api.common.UBSettings;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import net.minecraft.init.Blocks;

import java.util.ArrayList;

/**
 *
 * @author CurtisA, LouisDB
 *
 */
final class StrataLayers {

	StrataLayer[][] layers;

	public StrataLayers(UBSettings config) {
		layers = new StrataLayer[30][];
		if (config.harmoniousStrata())
			createHarmoniousLayers();
		else
			createLayers();
                cleanup();
	}

	private void createLayers() {
		layers[0] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 0, 30, 32), new StrataLayer(API.SEDIMENTARY_STONE, 1, 38, 41), new StrataLayer(API.SEDIMENTARY_STONE, 6, 65, 70) };
		layers[1] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 2, 33, 36), new StrataLayer(API.SEDIMENTARY_STONE, 1, 38, 41), new StrataLayer(API.SEDIMENTARY_STONE, 5, 60, 62), new StrataLayer(API.SEDIMENTARY_STONE, 7, 75, 80) };
		layers[2] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 3, 30, 35), new StrataLayer(API.SEDIMENTARY_STONE, 4, 26, 29), new StrataLayer(API.METAMORPHIC_STONE, 2, 70, 74) };
		layers[3] = new StrataLayer[] { new StrataLayer(Blocks.SANDSTONE, 0, 40, 43), new StrataLayer(Blocks.SAND, 0, 44, 47), new StrataLayer(API.SEDIMENTARY_STONE, 1, 55, 57) };
		layers[4] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 7, 29, 33), new StrataLayer(API.SEDIMENTARY_STONE, 5, 42, 44), new StrataLayer(API.SEDIMENTARY_STONE, 4, 90, 105) };
		layers[5] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 2, 33, 35), new StrataLayer(API.SEDIMENTARY_STONE, 3, 38, 41), new StrataLayer(API.SEDIMENTARY_STONE, 6, 72, 79) };
		layers[6] = new StrataLayer[] { new StrataLayer(API.METAMORPHIC_STONE, 2, 30, 35), new StrataLayer(API.SEDIMENTARY_STONE, 0, 39, 44), new StrataLayer(Blocks.SANDSTONE, 0, 52, 54), new StrataLayer(Blocks.SAND, 0, 55, 60) };
		layers[7] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 0, 33, 35), new StrataLayer(API.SEDIMENTARY_STONE, 3, 45, 49), new StrataLayer(API.SEDIMENTARY_STONE, 6, 80, 85) };
		layers[8] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 1, 30, 32), new StrataLayer(API.METAMORPHIC_STONE, 2, 70, 74), new StrataLayer(API.SEDIMENTARY_STONE, 4, 75, 79) };
		layers[9] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 2, 31, 35), new StrataLayer(API.SEDIMENTARY_STONE, 5, 39, 42), new StrataLayer(API.SEDIMENTARY_STONE, 7, 62, 65) };
		layers[10] = new StrataLayer[] { new StrataLayer(API.IGNEOUS_STONE, 2, 12, 18), new StrataLayer(API.IGNEOUS_STONE, 6, 24, 29), new StrataLayer(API.METAMORPHIC_STONE, 2, 80, 85) };
		layers[11] = new StrataLayer[] { new StrataLayer(API.IGNEOUS_STONE, 5, 13, 22), new StrataLayer(API.METAMORPHIC_STONE, 6, 29, 36), new StrataLayer(API.METAMORPHIC_STONE, 3, 80, 128) };

	}

	private void createHarmoniousLayers() {
		layers[0] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 0, 30, 32), new StrataLayer(API.SEDIMENTARY_STONE, 1, 38, 41), new StrataLayer(API.SEDIMENTARY_STONE, 6, 65, 70) };
		layers[5] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 2, 33, 36), new StrataLayer(API.SEDIMENTARY_STONE, 1, 38, 41), new StrataLayer(API.SEDIMENTARY_STONE, 5, 60, 62), new StrataLayer(API.SEDIMENTARY_STONE, 7, 75, 80) };
		layers[2] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 3, 30, 35), new StrataLayer(API.SEDIMENTARY_STONE, 4, 26, 29), new StrataLayer(API.METAMORPHIC_STONE, 2, 70, 74) };
		layers[3] = new StrataLayer[] { new StrataLayer(Blocks.SANDSTONE, 0, 40, 43), new StrataLayer(Blocks.SAND, 0, 44, 47), new StrataLayer(API.SEDIMENTARY_STONE, 1, 55, 57) };
		layers[4] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 7, 29, 33), new StrataLayer(API.SEDIMENTARY_STONE, 5, 42, 44), new StrataLayer(API.SEDIMENTARY_STONE, 4, 90, 105) };
		layers[1] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 2, 33, 35), new StrataLayer(API.SEDIMENTARY_STONE, 3, 38, 41), new StrataLayer(API.SEDIMENTARY_STONE, 6, 72, 79) };
		layers[6] = new StrataLayer[] { new StrataLayer(API.METAMORPHIC_STONE, 2, 30, 35), new StrataLayer(API.SEDIMENTARY_STONE, 0, 39, 44), new StrataLayer(Blocks.SANDSTONE, 0, 52, 54), new StrataLayer(Blocks.SAND, 0, 55, 60) };
		layers[7] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 0, 33, 35), new StrataLayer(API.SEDIMENTARY_STONE, 3, 45, 49), new StrataLayer(API.SEDIMENTARY_STONE, 4, 80, 85) };
		layers[8] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 1, 30, 32), new StrataLayer(API.METAMORPHIC_STONE, 2, 70, 74), new StrataLayer(API.SEDIMENTARY_STONE, 6, 75, 79) };
		layers[9] = new StrataLayer[] { new StrataLayer(API.SEDIMENTARY_STONE, 2, 31, 35), new StrataLayer(API.SEDIMENTARY_STONE, 5, 39, 42), new StrataLayer(API.SEDIMENTARY_STONE, 7, 62, 65) };
		layers[10] = new StrataLayer[] { new StrataLayer(API.IGNEOUS_STONE, 2, 12, 18), new StrataLayer(API.IGNEOUS_STONE, 6, 24, 29), new StrataLayer(API.METAMORPHIC_STONE, 2, 80, 85) };
		layers[11] = new StrataLayer[] { new StrataLayer(API.IGNEOUS_STONE, 5, 13, 22), new StrataLayer(API.METAMORPHIC_STONE, 6, 29, 36), new StrataLayer(API.METAMORPHIC_STONE, 3, 80, 128) };

	}

        private void cleanup() {
            for (int i = 0 ; i < 12; i ++) {
                layers [i] = cleanedLayers(layers[i]);
            }
        }

        private StrataLayer [] cleanedLayers(StrataLayer [] toClean) {
            // removes disallowed layers
            ArrayList<StrataLayer> kept = new ArrayList();
            for (int i = 0; i < toClean.length;i++) {
                if (UBConfig.SPECIFIC.generationAllowed(toClean[i].filler)) {
                    kept.add(toClean[i]);
                }
            }
            StrataLayer [] result = new StrataLayer[kept.size()];
            int i = 0;
            for (StrataLayer layer: kept) {
                result[i++] = layer;
            }
            return result;
        }

}
