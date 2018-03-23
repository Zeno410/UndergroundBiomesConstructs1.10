/*
 */

package exterminatorjeff.undergroundbiomes.world;


/**
 * @author curtisadams
 */

import exterminatorjeff.undergroundbiomes.api.UBBiome;
import exterminatorjeff.undergroundbiomes.api.UndergroundBiomeSet;
import exterminatorjeff.undergroundbiomes.world.noise.SimplexNoiseGenerator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class TraditionalStoneReplacer extends UBStoneReplacer {

  private GenLayer undergroundBiomeIndexLayer;

  public TraditionalStoneReplacer(long seed, int size, UndergroundBiomeSet biomeSet) {
    super(biomeSet.allowedBiomes(), new SimplexNoiseGenerator(seed));
    if (biomeSet.allowedBiomes()[20].ID < 1) {
      throw new RuntimeException(biomeSet.toString() + biomeSet.allowedBiomes()[20].filler.getBlock().getLocalizedName());
    }
    undergroundBiomeIndexLayer = biomeGenerators(seed, size, biomeSet);
  }


  @Override
  public int[] getBiomeValues(Chunk chunk) {

    int[] var7 = this.undergroundBiomeIndexLayer.getInts(chunk.xPosition * 16, chunk.zPosition * 16, 16, 16);
    return var7;
  }

  public static GenLayer biomeGenerators(long par0, int size, UndergroundBiomeSet biomeSet) {

    GenLayerUndergroundBiomes var17 = new GenLayerUndergroundBiomes(200L, biomeSet);

    GenLayerSmooth var15 = new GenLayerSmooth(1000L, var17);
    GenLayer var6 = GenLayerZoom.magnify(1000L, var15, size);
    var6 = GenLayerZoom.magnify(1000L, var6, 2);


    GenLayerSmooth var19 = new GenLayerSmooth(1000L, var6);

    GenLayerVoronoiZoom var8 = new GenLayerVoronoiZoom(10L, var19);
    var8.initWorldGenSeed(par0);
    //testGenerator(var8, size);
    //testBiomeSize(var8);
    GenLayer reliable = new GenLayerReliable(var8);
    return reliable;
  }

  public UBBiome UBBiomeAt(int x, int z) {

    int[] var7 = this.undergroundBiomeIndexLayer.getInts(x, z, 1, 1);

    // Get the underground biome for the position
    UBBiome currentBiome = biomeList[var7[0]];
    return currentBiome;
  }
}

