package exterminatorjeff.undergroundbiomes.world;

import exterminatorjeff.undergroundbiomes.api.UBBiome;
import java.util.ArrayList;
import java.util.List;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.StrataLayer;
import exterminatorjeff.undergroundbiomes.api.UndergroundBiomeSet;
import exterminatorjeff.undergroundbiomes.api.common.UBSettings;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * All underground biomes
 * 
 * @author CurtisA, LouisDB
 */
public final class UBBiomesSet extends UndergroundBiomeSet {
    
    private final UBSettings settings;
	private final UBBiome[] biomes;
        private final List<UBBiome> biomesBuilder = new ArrayList<>();
        private int ID = 0;
    private UBBiome[] allowedBiomes;

	public UBBiomesSet( UBSettings settings) {
		super(new StrataLayers(settings).layers);

		add(new UBBiome(ID,API.IGNEOUS_STONE, 0),strataLayers[0]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 1),strataLayers[1]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 2),strataLayers[2]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 3),strataLayers[3]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 4),strataLayers[4]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 5),strataLayers[5]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 6),strataLayers[6]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 7),strataLayers[7]);

		add(new UBBiome(ID,API.IGNEOUS_STONE, 0),strataLayers[8]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 1),strataLayers[9]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 2),strataLayers[0]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 3),strataLayers[1]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 4),strataLayers[2]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 5),strataLayers[3]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 6),strataLayers[4]);
		add(new UBBiome(ID,API.IGNEOUS_STONE, 7),strataLayers[5]);

		add(new UBBiome(ID,API.METAMORPHIC_STONE, 0),strataLayers[6]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 1),strataLayers[7]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 2),strataLayers[8]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 3),strataLayers[9]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 4),strataLayers[0]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 5),strataLayers[1]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 6),strataLayers[2]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 7),strataLayers[3]);

		add(new UBBiome(ID,API.METAMORPHIC_STONE, 0),strataLayers[4]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 1),strataLayers[5]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 2),strataLayers[6]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 3),strataLayers[7]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 4),strataLayers[8]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 5),strataLayers[9]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 6),strataLayers[0]);
		add(new UBBiome(ID,API.METAMORPHIC_STONE, 7),strataLayers[1]);

		if (settings.regularStoneBiomes()&&UBConfig.SPECIFIC.generationAllowed(Blocks.STONE.getDefaultState())) {
			add(new UBBiome(ID,Blocks.STONE, 0),strataLayers[0]);
			add(new UBBiome(ID,Blocks.STONE, 0),strataLayers[1]);
			add(new UBBiome(ID,Blocks.STONE, 0),strataLayers[2]);
			add(new UBBiome(ID,Blocks.STONE, 0),strataLayers[3]);
		}

		this.biomes = new UBBiome[biomesBuilder.size()];
		biomesBuilder.toArray(this.biomes);
                if (biomes[20].ID == 0) throw new RuntimeException();
                this.settings = settings;
        allowedBiomes = generatable(biomes);
	}

        private void add(UBBiome biome, StrataLayer [] layers) {
            if (UBConfig.SPECIFIC.generationAllowed(biome.filler)) {
                biome.addStrataLayers(layers);
                biomesBuilder.add(biome);
                ID++;
            }
            
        }
        
    public UBBiome [] generatable(UBBiome[] possible) {
        ArrayList<UBBiome> accepted = new ArrayList<UBBiome>();
        for (int i = 0; i < possible.length; i++) {
            IBlockState block = possible[i].filler;
            if (settings.generationAllowed(block)) accepted.add(possible[i]);
        }
        UBBiome[] result = new UBBiome[accepted.size()];
        return accepted.toArray(result);
    }
    @Override
    public UBBiome[] allowedBiomes() {
        return allowedBiomes;
    }

}
