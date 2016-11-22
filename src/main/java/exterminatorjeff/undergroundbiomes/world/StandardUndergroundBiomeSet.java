/*
 */

package exterminatorjeff.undergroundbiomes.world;
import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.UBBiome;
import exterminatorjeff.undergroundbiomes.api.UndergroundBiomeSet;
import exterminatorjeff.undergroundbiomes.api.common.UBSettings;
import exterminatorjeff.undergroundbiomes.api.names.StoneEntry;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 *
 * @author Zeno410
 */
public class StandardUndergroundBiomeSet extends UndergroundBiomeSet {

    private final UBSettings settings;
    private UBBiome[] allowedBiomes;
    public StandardUndergroundBiomeSet(UBSettings settings) {
        super(new StrataLayers(settings).layers);
        this.settings = settings;
        choseBiomes();
    }

    static StoneEntry  igneousID = API.IGNEOUS_STONE;

    public final UBBiome igneous1 = (new UBBiome(0, igneousID, 0))
            .addStrataLayers(strataLayers[0]);
    public final UBBiome igneous2 = (new UBBiome(1, igneousID, 1))
            .addStrataLayers(strataLayers[1]);
    public final UBBiome igneous3 = (new UBBiome(2, igneousID, 2))
            .addStrataLayers(strataLayers[2]);
    public final UBBiome igneous4 = (new UBBiome(3, igneousID, 3))
            .addStrataLayers(strataLayers[3]);
    public final UBBiome igneous5 = (new UBBiome(4, igneousID, 4))
            .addStrataLayers(strataLayers[4]);
    public final UBBiome igneous6 = (new UBBiome(5, igneousID, 5))
            .addStrataLayers(strataLayers[5]);
    public final UBBiome igneous7 = (new UBBiome(6, igneousID, 6))
            .addStrataLayers(strataLayers[6]);
    public final UBBiome igneous8 = (new UBBiome(7, igneousID, 7))
            .addStrataLayers(strataLayers[7]);

    public final UBBiome igneous9 = (new UBBiome(8, igneousID, 0))
            .addStrataLayers(strataLayers[8]);
    public final UBBiome igneous10 = (new UBBiome(9, igneousID, 1))
            .addStrataLayers(strataLayers[9]);
    public final UBBiome igneous11 = (new UBBiome(10, igneousID, 2))
            .addStrataLayers(strataLayers[0]);
    public final UBBiome igneous12 = (new UBBiome(11, igneousID, 3))
            .addStrataLayers(strataLayers[1]);
    public final UBBiome igneous13 = (new UBBiome(12, igneousID, 4))
            .addStrataLayers(strataLayers[2]);
    public final UBBiome igneous14 = (new UBBiome(13, igneousID, 5))
            .addStrataLayers(strataLayers[3]);
    public final UBBiome igneous15 = (new UBBiome(14, igneousID, 6))
            .addStrataLayers(strataLayers[4]);
    public final UBBiome igneous16 = (new UBBiome(15, igneousID, 7))
            .addStrataLayers(strataLayers[5]);

    static StoneEntry metamorphicID = API.METAMORPHIC_STONE;

    public final UBBiome metamorphic1 = (new UBBiome(16, metamorphicID, 0))
            .addStrataLayers(strataLayers[6]);
    public final UBBiome metamorphic2 = (new UBBiome(17, metamorphicID, 1))
            .addStrataLayers(strataLayers[7]);
    public final UBBiome metamorphic3 = (new UBBiome(18, metamorphicID, 1))//to stop marble from being a base rock
            .addStrataLayers(strataLayers[8]);
    public final UBBiome metamorphic4 = (new UBBiome(19, metamorphicID, 3))
            .addStrataLayers(strataLayers[9]);
    public final UBBiome metamorphic5 = (new UBBiome(20, metamorphicID, 4))
            .addStrataLayers(strataLayers[0]);
    public final UBBiome metamorphic6 = (new UBBiome(21, metamorphicID, 5))
            .addStrataLayers(strataLayers[1]);
    public final UBBiome metamorphic7 = (new UBBiome(22, metamorphicID, 6))
            .addStrataLayers(strataLayers[2]);
    public final UBBiome metamorphic8 = (new UBBiome(23, metamorphicID, 7))
            .addStrataLayers(strataLayers[3]);

    public final UBBiome metamorphic9 = (new UBBiome(24, metamorphicID, 0))
            .addStrataLayers(strataLayers[4]);
    public final UBBiome metamorphic10 = (new UBBiome(25, metamorphicID, 1))
            .addStrataLayers(strataLayers[5]);
    public final UBBiome metamorphic11 = (new UBBiome(26, metamorphicID, 1))//to stop marble from being a base rock
            .addStrataLayers(strataLayers[6]);
    public final UBBiome metamorphic12 = (new UBBiome(27, metamorphicID, 3))
            .addStrataLayers(strataLayers[7]);
    public final UBBiome metamorphic13 = (new UBBiome(28, metamorphicID, 4))
            .addStrataLayers(strataLayers[8]);
    public final UBBiome metamorphic14 = (new UBBiome(29, metamorphicID, 5))
            .addStrataLayers(strataLayers[9]);
    public final UBBiome metamorphic15 = (new UBBiome(30, metamorphicID, 6))
            .addStrataLayers(strataLayers[0]);
    public final UBBiome metamorphic16 = (new UBBiome(31, metamorphicID, 7))
            .addStrataLayers(strataLayers[1]);

    public final UBBiome vanillaStone1 = (new UBBiome(32, Blocks.STONE, 0))
            .addStrataLayers(strataLayers[0]);
    public final UBBiome vanillaStone2 = (new UBBiome(33, Blocks.STONE, 0))
            .addStrataLayers(strataLayers[1]);
    public final UBBiome vanillaStone3 = (new UBBiome(34, Blocks.STONE, 0))
            .addStrataLayers(strataLayers[2]);
    public final UBBiome vanillaStone4 = (new UBBiome(35, Blocks.STONE, 0))
            .addStrataLayers(strataLayers[3]);

    private void choseBiomes() {
         if (settings.regularStoneBiomes()) {
            allowedBiomes = new UBBiome[] {igneous1, igneous2,
                    igneous3, igneous4,
                    igneous5, igneous6, igneous7,
                    igneous8, igneous9, igneous10,
                    igneous11, igneous12, igneous13,
                    igneous14, igneous15, igneous16,
                    metamorphic1, metamorphic2, metamorphic3,
                    metamorphic4, metamorphic5, metamorphic6,
                    metamorphic7, metamorphic8, metamorphic9,
                    metamorphic10, metamorphic11, metamorphic12,
                    metamorphic13, metamorphic14, metamorphic15,
                    metamorphic16};
        } else {
            allowedBiomes = new UBBiome[] {igneous1, igneous2,
                    igneous3, igneous4,
                    igneous5, igneous6, igneous7,
                    igneous8, igneous9, igneous10,
                    igneous11, igneous12, igneous13,
                    igneous14, igneous15, igneous16,
                    metamorphic1, metamorphic2, metamorphic3,
                    metamorphic4, metamorphic5, metamorphic6,
                    metamorphic7, metamorphic8, metamorphic9,
                    metamorphic10, metamorphic11, metamorphic12,
                    metamorphic13, metamorphic14, metamorphic15,
                    metamorphic16,
                    vanillaStone1, vanillaStone2,
                    vanillaStone3, vanillaStone4
                    };
        }
        allowedBiomes = generatable(allowedBiomes);
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