/*
 */

package exterminatorjeff.undergroundbiomes.world;

import exterminatorjeff.undergroundbiomes.api.UBBiome;
import exterminatorjeff.undergroundbiomes.world.noise.SimplexNoiseGenerator;
import exterminatorjeff.undergroundbiomes.world.noise.Voronoi;
import net.minecraft.world.chunk.Chunk;

public class SimpleStoneReplacer extends UBStoneReplacer {

  final int NB_BIOMES;
  private SimplexNoiseGenerator simplex;
  private Voronoi voronoi;

  public SimpleStoneReplacer(UBBiome[] biomeList, long seed, int biomeSize) {
    super(biomeList, new SimplexNoiseGenerator(seed));
    NB_BIOMES = biomeList.length;
    simplex = new SimplexNoiseGenerator(seed + 1);
    voronoi = new Voronoi();
    voronoi.setSeed((int) seed);
    voronoi.setFrequency(0.05D / biomeSize);
  }

  /**
   * enManager mana
   *
   * @param value
   * @param n     : the number of parts
   * @param min
   * @param max
   * @return : Return 0 <-> (n-1)
   */
  int getRoundedValueInNParts(final double value, final int n, final double min, final double max) {
    int ret = -1;
    double range = max - min;
    double gap = range / n;
    double[] bounds = new double[n - 1];
    // {b0,b1...b(n-1)}
    for (int i = 1; i <= bounds.length; i++) {
      bounds[i - 1] = min + i * gap;
    }
    //
    if (value >= min && value < bounds[0])
      ret = 0;
    for (int i = 0; i <= n - 3; i++) {
      if (value >= bounds[i] && value < bounds[i + 1])
        ret = i + 1;
    }
    if (value >= bounds[bounds.length - 1] && value <= max)
      ret = n - 1;
    // Error
    if (ret == -1) {
      String msg = String.format("ret == -1 for value : %f | min: %f ; max: %f | %d parts | bounds: {", value, min, max, n);
      for (int i = 0; i < bounds.length - 1; i++) {
        msg += bounds[i] + ", ";
      }
      msg += bounds[bounds.length - 1] + "}";
      throw new RuntimeException(msg);
    }
    return ret;
  }

  @Override
  public int[] getBiomeValues(Chunk chunk) {    /*
   * Voronoi biomes map for this chunk
   */
    int[] biomeValues = new int[256];
    int xPos = chunk.xPosition * 16;
    int zPos = chunk.zPosition * 16;
    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        double v = simplex.noise(xPos + x, zPos + z, 2, 0.05D, 10000.0D, true);
        int variation = (int) (v * 8);
        double value = voronoi.getValue(xPos + x + variation, zPos + z + variation, 0);
        biomeValues[x * 6 + z] = getRoundedValueInNParts(value, NB_BIOMES, -1.0D, 1.0D);
      }
    }
    return biomeValues;
  }

  @Override
  public UBBiome UBBiomeAt(int x, int z) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
