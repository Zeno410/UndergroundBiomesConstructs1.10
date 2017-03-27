package exterminatorjeff.undergroundbiomes.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableSet;
import exterminatorjeff.undergroundbiomes.api.API;

import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.common.UBSettings;
import exterminatorjeff.undergroundbiomes.api.enums.IgneousVariant;
import exterminatorjeff.undergroundbiomes.api.enums.MetamorphicVariant;
import exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant;
import exterminatorjeff.undergroundbiomes.api.names.BlockAccess;
import exterminatorjeff.undergroundbiomes.api.names.BlockEntry;
import exterminatorjeff.undergroundbiomes.api.names.VanillaBlockAccess;
import java.io.File;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;

/**
 * 
 * @author CurtisA, LouisDB
 *
 */
public class UBConfig implements UBSettings {
    public static UBSettings SPECIFIC;

	static final UBLogger LOGGER = new UBLogger(UBConfig.class, Level.DEBUG);

	private Configuration configuration;    
        private final HashMap<BlockAccess,HashMap<Integer,BooleanSetting>> stoneGenerationSettings =
            new HashMap();

	// TODO ImposeUBStone (Highlands)
	// TODO Translate config, add more comments and add GUI config
	// TODO Maybe UBify all structures? (Dungeon, stronghold...)

	private static final String CATEGORY_GENERAL = "General";
	public final BooleanSetting crashOnProblems = new BooleanSetting(CATEGORY_GENERAL, "CrashOnProblems");
	public final BooleanSetting realistic = new BooleanSetting(CATEGORY_GENERAL, "Realistic");

	private static final String CATEGORY_CONTENT = "Content";
	public final BooleanSetting ubifyRecipes = new BooleanSetting(CATEGORY_CONTENT, "UBifyRecipes");
	public final BooleanSetting ubifyOres = new BooleanSetting(CATEGORY_CONTENT, "UBifyOres");
	public final IntegerSetting regularStoneCrafting = new IntegerSetting(CATEGORY_CONTENT, "RegularStoneCrafting");
	public final FloatSetting hardnessModifier = new FloatSetting(CATEGORY_CONTENT, "HarnessModifier");
	public final FloatSetting resistanceModifier = new FloatSetting(CATEGORY_CONTENT, "ResistanceModifier");

	private static final String CATEGORY_GENERATION = "World generation";
	public final IntegerSetting biomeSize = new IntegerSetting(CATEGORY_GENERATION, "BiomeSize");
	public final IntegerSetting generationHeight = new IntegerSetting(CATEGORY_GENERATION, "GenerationHeight");
	public final BooleanSetting regularStoneBiomes = new BooleanSetting(CATEGORY_GENERATION, "RegularStoneBiomes");
	public final BooleanSetting harmoniousStrata = new BooleanSetting(CATEGORY_GENERATION, "HarmoniousStrata");
	public final StringSetting includedDimensions = new StringSetting(CATEGORY_GENERATION, "IncludedDimensions");
	public Set<Integer> includedDimensionsSet;
	public final StringSetting excludedDimensions = new StringSetting(CATEGORY_GENERATION, "ExcludedDimensions");
	public Set<Integer> excludedDimensionsSet;
	public final BooleanSetting dimensionSpecificSeeds = new BooleanSetting(CATEGORY_GENERATION, "DimensionSpecificSeeds");
	public final BooleanSetting ubifyVillages = new BooleanSetting(CATEGORY_GENERATION, "UBifyVillages");

	private static final String CATEGORY_VISUAL = "Visual";
	public final BooleanSetting plainSlabTextures = new BooleanSetting(CATEGORY_VISUAL, "PlainSlabTextures");

	private static final String CATEGORY_SPECIFIC = "Specific";
	public final BooleanSetting buttonsOn = new BooleanSetting(CATEGORY_SPECIFIC, "UndergroundBiomesButtons");
	public final IntegerSetting buttonsTypes = new IntegerSetting(CATEGORY_SPECIFIC, "UndergroundBiomesButtonsTypes");
	public final IntegerSetting buttonsStyles = new IntegerSetting(CATEGORY_SPECIFIC, "UndergroundBiomesButtonsStyles");
	public final BooleanSetting stairsOn = new BooleanSetting(CATEGORY_SPECIFIC, "UndergroundBiomesStairs");
	public final IntegerSetting stairsTypes = new IntegerSetting(CATEGORY_SPECIFIC, "UndergroundBiomesStairsTypes");
	public final IntegerSetting stairsStyles = new IntegerSetting(CATEGORY_SPECIFIC, "UndergroundBiomesStairsStyles");
	public final BooleanSetting wallsOn = new BooleanSetting(CATEGORY_SPECIFIC, "UndergroundBiomesWalls");
	public final IntegerSetting wallsTypes = new IntegerSetting(CATEGORY_SPECIFIC, "UndergroundBiomesWallsTypes");
	public final IntegerSetting wallsStyles = new IntegerSetting(CATEGORY_SPECIFIC, "UndergroundBiomesWallsStyles");

	private static final String CATEGORY_MISCELLANEOUS = "Miscellaneaous";
	public final IntegerSetting changeButtonRecipe = new IntegerSetting(CATEGORY_MISCELLANEOUS, "ChangeButtonRecipe");
	public final BooleanSetting disableVanillaStoneVariants = new BooleanSetting(CATEGORY_MISCELLANEOUS, "DisableVanillaStoneVariants");

	/*
	 * 
	 */

        public UBConfig() {        
             setActivations(API.IGNEOUS_STONE,"igneous stone",IgneousVariant.values());
             setActivations(API.METAMORPHIC_STONE,"metamorphic stone",MetamorphicVariant.values());
             setActivations(API.SEDIMENTARY_STONE,"sedimentary stone",SedimentaryVariant.values());
             setActivation(Blocks.SAND,0);
             setActivation(Blocks.SANDSTONE,0);
             setActivation(Blocks.STONE,0);
            
        }
        
        private void setActivations(BlockEntry block, String type, IStringSerializable [] names) {
             HashMap<Integer,BooleanSetting> blockActivations = new HashMap();
             this.stoneGenerationSettings.put(block, blockActivations);
             for (int i = 0; i < names.length;i++) {
                 BooleanSetting generationConfig = new BooleanSetting(CATEGORY_GENERATION,"Generate "+ type +
                    " metadata " + i + ", " +names[i].getName());
                 blockActivations.put(i, generationConfig);
             }
        }
        
        private void setActivation(Block block, int metadata) {
            HashMap<Integer,BooleanSetting> blockActivations = new HashMap();
            this.stoneGenerationSettings.put(new VanillaBlockAccess(block), blockActivations);
            BooleanSetting generationConfig = new BooleanSetting(CATEGORY_GENERATION, "Generate "+ block.getUnlocalizedName() +", metadata " + metadata);
            blockActivations.put(metadata, generationConfig);
        }
        
	/**
	 * Must be called be called in pre-init and before any blocks or items are
	 * created.
	 * 
	 * @param event
	 */
	public void init(File file) {
		LOGGER.info("Loading configuration");

		configuration = new Configuration(file);
		configuration.load();

		/*
		 *  
		 */

		crashOnProblems.initProperty(configuration, false, "Crash rather than try to get by when encountering problems");
		realistic.initProperty(configuration, false, "Only cobble walls, only stone button, no stone stairs\n" + "Disable regular stone variants\n" + "Overrides any specific settings");

		ubifyRecipes.initProperty(configuration, true, "Modify all recipes to include Underground Biomes stones");
		ubifyOres.initProperty(configuration, true, "Convert ores to have Underground Biomes stone background textures");
		regularStoneCrafting.initProperty(configuration, 4, "0 = none\n" + "1 = one rock\n" + "2 = with redstone\n" + "3 = 2x2 stone, lose 3\n" + "4 = 2x2 stone");
		hardnessModifier.initProperty(configuration, 1.0F, "Increase to make stone longer to mine. Normal is 1.0");
		resistanceModifier.initProperty(configuration, 1.0F, "Increase to make stone more resistant to explosions. Normal is 1.0");

		biomeSize.initProperty(configuration, 4, "Interval [1-16]\n" + "Warning: exponential");
		generationHeight.initProperty(configuration, 256, "Maximum height for UB world generation");
		regularStoneBiomes.initProperty(configuration, false, "Integrate vanilla stone into the world generation\n" + "Will cause sharp biome transitions if changed while playing the same world");
		harmoniousStrata.initProperty(configuration, false, "Avoid jarring strata transitions");
		includedDimensions.initProperty(configuration, "*", "Comma-separated list of dimension IDs, put * to use exclude list");
		excludedDimensions.initProperty(configuration, "-1,1", "Comma-separated list of dimension IDs, used only if include list is *");
		dimensionSpecificSeeds.initProperty(configuration, false, "Use a different seed for each dimensions");
		ubifyVillages.initProperty(configuration, true, "Use UB stones in villages structures");

		plainSlabTextures.initProperty(configuration, false, "Use normal stone textures for slabs");

		buttonsOn.initProperty(configuration, true, "Provide buttons for Underground Biomes blocks");
		buttonsTypes.initProperty(configuration, 7, "What types of stone for buttons : +1 for igneous, +2 for metamorphic and +4 for sedimentary\n" + "Default: 7 -> all");
		buttonsStyles.initProperty(configuration, 3, "What styles of stone for buttons : +1 for stone, +2 for cobble and +4 for brick\n" + "Default : 3 -> stone and cobble");
		stairsOn.initProperty(configuration, true, "Provide Stairs for Underground Biomes blocks");
		stairsTypes.initProperty(configuration, 7, "What types of stone for stairs : +1 for igneous, +2 for metamorphic and +4 for sedimentary\n" + "Default: 7 -> all");
		stairsStyles.initProperty(configuration, 7, "What styles of stone for stairs : +1 for stone, +2 for cobble and +4 for brick\n" + "Default : 7 -> all");
		wallsOn.initProperty(configuration, true, "Provide Walls for Underground Biomes blocks");
		wallsTypes.initProperty(configuration, 7, "What types of stone for walls : +1 for igneous, +2 for metamorphic and +4 for sedimentary\n" + "Default: 7 -> all");
		wallsStyles.initProperty(configuration, 7, "What styles of stone for walls : +1 for stone, +2 for cobble and +4 for brick\n" + "Default : 7 -> all");

		changeButtonRecipe.initProperty(configuration, 8, "Change the result of the button recipe\n" + "Default: 8");
		disableVanillaStoneVariants.initProperty(configuration, false, "Remove vanilla andesite, diorite, and granite");
                
                for (HashMap<Integer,BooleanSetting> blockActivations: stoneGenerationSettings.values()) {
                    for (BooleanSetting setting: blockActivations.values()) {
                        setting.initProperty(configuration, Boolean.TRUE, "");
                    }
                }

		/*
		 * 
		 */

		realistic.addOverride(true, wallsStyles, 2).addOverride(true, buttonsStyles, 1).addOverride(true, stairsStyles, 6) //
				.addOverride(true, disableVanillaStoneVariants, true);

		configuration.save();

		if (includedDimensions.getValue().equals("*")) {
			ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
			for (String v : excludedDimensions.property.getString().split(","))
				builder.add(Integer.parseInt(v));
			excludedDimensionsSet = builder.build();
			includedDimensionsSet = ImmutableSet.of();
		} else {
			ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
			for (String v : includedDimensions.property.getString().split(","))
				builder.add(Integer.parseInt(v));
			includedDimensionsSet = builder.build();
			excludedDimensionsSet = ImmutableSet.of();
		}
	}

        public void copy(UBConfig copied) {

		/*
		 *  
		 */

		crashOnProblems.setValue(copied.crashOnProblems.getValue());
		realistic.setValue(copied.realistic());

		ubifyRecipes.setValue(copied.ubifyRecipes());
		ubifyOres.setValue(copied.ubifyOres());
		regularStoneCrafting.setValue(copied.regularStoneCrafting());
		hardnessModifier.setValue(copied.hardnessModifier());
		resistanceModifier.setValue(copied.resistanceModifier());

		biomeSize.setValue(copied.biomeSize());
		generationHeight.setValue(copied.generationHeight());
		regularStoneBiomes.setValue(copied.regularStoneBiomes());
		harmoniousStrata.setValue(copied.harmoniousStrata());
		includedDimensions.setValue(copied.includedDimensions.getValue());
		excludedDimensions.setValue(copied.excludedDimensions.getValue());
		dimensionSpecificSeeds.setValue(copied.dimensionSpecificSeeds());
		ubifyVillages.setValue(copied.ubifyVillages());

		plainSlabTextures.setValue(copied.alternativeSlabTextures());

		buttonsOn.setValue(copied.buttonsOn());
		buttonsTypes.setValue(copied.buttonsTypes.getValue());
		buttonsStyles.setValue(copied.buttonsStyles.getValue());
		stairsOn.setValue(copied.stairsOn());
		stairsTypes.setValue(copied.stairsTypes.getValue());
		stairsStyles.setValue(copied.stairsStyles.getValue());
		wallsOn.setValue(copied.wallsOn());
		wallsTypes.setValue(copied.wallsTypes.getValue());
		wallsStyles.setValue(copied.wallsStyles.getValue());
		
		changeButtonRecipe.setValue(copied.changeButtonRecipe.getValue());
                disableVanillaStoneVariants.setValue(copied.disableVanillaStoneVariants());

		/*
		 * 
		 */

		realistic.addOverride(true, wallsStyles, 2).addOverride(true, buttonsStyles, 1).addOverride(true, stairsStyles, 6) //
				.addOverride(true, disableVanillaStoneVariants, true);
                
                // copy block generation settings
                for (BlockAccess blockAccess: copied.stoneGenerationSettings.keySet()) {
                    Block block = blockAccess.getBlock();
                    for (BlockAccess localAccess: this.stoneGenerationSettings.keySet()) {
                        if (localAccess.getBlock().equals(block)) {
                            HashMap<Integer,BooleanSetting> copiedSettings = copied.stoneGenerationSettings.get(blockAccess);
                            HashMap<Integer,BooleanSetting> thisSettings = this.stoneGenerationSettings.get(localAccess);
                            
                            copiedSettings.keySet().stream().forEach((metadata) -> {
                                thisSettings.get(metadata).setValue(copiedSettings.get(metadata).getValue());
                            });
                        }
                    }
                }

		if (includedDimensions.getValue().equals("*")) {
			ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
			for (String v : excludedDimensions.property.getString().split(","))
				builder.add(Integer.parseInt(v));
			excludedDimensionsSet = builder.build();
			includedDimensionsSet = ImmutableSet.of();
		} else {
			ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
			for (String v : includedDimensions.property.getString().split(","))
				builder.add(Integer.parseInt(v));
			includedDimensionsSet = builder.build();
			excludedDimensionsSet = ImmutableSet.of();
		}
	}
        
        public void save() {
            configuration.save();
        }
	/**
	 * Get the dimensions to generate UB stones into.
	 * 
	 * @return A list of dimension ID
	 */
	public List<Integer> getUBifiedDimensions() {
		List<Integer> ubifiedDims = new ArrayList<>();
                if (includedDimensionsSet.size() > 0) {
		     ubifiedDims.addAll(includedDimensionsSet);
                } else {
		    Integer [] allDims = DimensionManager.getIDs();//.getDimensions(DimensionType.OVERWORLD);
		    for (int dim : allDims)
			ubifiedDims.add(dim);
		    ubifiedDims.removeAll(excludedDimensionsSet);
                }
		return ubifiedDims;
	}

	/*
	 * General
	 */

	@Override
	public boolean crashOnProblems() {
		return crashOnProblems.getValue();
	}

	@Override
	public boolean realistic() {
		return realistic.getValue();
	}

	/*
	 * Content
	 */

	@Override
	public boolean ubifyRecipes() {
		return ubifyRecipes.getValue();
	}

	@Override
	public boolean ubifyOres() {
		return ubifyOres.getValue();
	}

	@Override
	public int regularStoneCrafting() {
		return regularStoneCrafting.getValue();
	}

	@Override
	public float hardnessModifier() {
		return (float) hardnessModifier.getValue();
	}

	@Override
	public float resistanceModifier() {
		return (float) resistanceModifier.getValue();
	}

	/*
	 * World generation
	 */

	@Override
	public int biomeSize() {
		return biomeSize.getValue();
	}

	@Override
	public int generationHeight() {
		return generationHeight.getValue();
	}

	@Override
	public boolean regularStoneBiomes() {
		return regularStoneBiomes.getValue();
	}

	@Override
	public boolean harmoniousStrata() {
		return harmoniousStrata.getValue();
	}

	@Override
	public Set<Integer> includedDimensions() {
		return includedDimensionsSet;
	}

	@Override
	public Set<Integer> excludedDimensions() {
		return excludedDimensionsSet;
	}

	@Override
	public boolean dimensionSpecificSeeds() {
		return dimensionSpecificSeeds.getValue();
	}

	@Override
	public boolean ubifyVillages() {
		return ubifyVillages.getValue();
	}

	/*
	 * Visual (Client only)
	 */

	@Override
	public boolean plainSlabTextures() {
		return plainSlabTextures.getValue();
	}

	/*
	 * Specific
	 */

	@Override
	public boolean buttonsOn() {
		return buttonsOn.getValue();
	}

	@Override
	public boolean igneousButtonsOn() {
		int i = buttonsTypes.getValue();
		return (i & 1) != 0;
	}

	@Override
	public boolean metamorphicButtonsOn() {
		int i = buttonsTypes.getValue();
		return (i & 2) != 0;
	}

	@Override
	public boolean sedimentaryButtonsOn() {
		int i = buttonsTypes.getValue();
		return (i & 4) != 0;
	}

	@Override
	public boolean stoneButtonsOn() {
		int i = buttonsStyles.getValue();
		return (i & 1) != 0;
	}

	@Override
	public boolean cobbleButtonsOn() {
		int i = buttonsStyles.getValue();
		return (i & 2) != 0;
	}

	@Override
	public boolean brickButtonsOn() {
		int i = buttonsStyles.getValue();
		return (i & 4) != 0;
	}

	@Override
	public boolean stairsOn() {
		return stairsOn.getValue();
	}

	@Override
	public boolean igneousStairsOn() {
		int i = stairsTypes.getValue();
		return (i & 1) != 0;
	}

	@Override
	public boolean metamorphicStairsOn() {
		int i = stairsTypes.getValue();
		return (i & 2) != 0;
	}

	@Override
	public boolean sedimentaryStairsOn() {
		int i = stairsTypes.getValue();
		return (i & 4) != 0;
	}

	@Override
	public boolean stoneStairsOn() {
		int i = stairsStyles.getValue();
		return (i & 1) != 0;
	}

	@Override
	public boolean cobbleStairsOn() {
		int i = stairsStyles.getValue();
		return (i & 2) != 0;
	}

	@Override
	public boolean brickStairsOn() {
		int i = stairsStyles.getValue();
		return (i & 4) != 0;
	}

	@Override
	public boolean wallsOn() {
		return wallsOn.getValue();
	}

	@Override
	public boolean igneousWallsOn() {
		int i = wallsTypes.getValue();
		return (i & 1) != 0;
	}

	@Override
	public boolean metamorphicWallsOn() {
		int i = wallsTypes.getValue();
		return (i & 2) != 0;
	}

	@Override
	public boolean sedimentaryWallsOn() {
		int i = wallsTypes.getValue();
		return (i & 4) != 0;
	}

	@Override
	public boolean stoneWallsOn() {
		int i = wallsStyles.getValue();
		return (i & 1) != 0;
	}

	@Override
	public boolean cobbleWallsOn() {
		int i = wallsStyles.getValue();
		return (i & 2) != 0;
	}

	@Override
	public boolean brickWallsOn() {
		int i = wallsStyles.getValue();
		return (i & 4) != 0;
	}

	/*
	 * Miscellaneous
	 */

	@Override
	public int buttonRecipeResult() {
		return changeButtonRecipe.getValue();
	}

	@Override
	public boolean disableVanillaStoneVariants() {
		return disableVanillaStoneVariants.getValue();
	}

        public boolean generationAllowed(IBlockState block) {
            int metadata = block.getBlock().getMetaFromState(block);
            return generationAllowed(block.getBlock(),metadata);
        }
        
    public final boolean generationAllowed(BlockEntry block, int metadata) {
        HashMap<Integer,BooleanSetting> info = stoneGenerationSettings.get(block);
        if (info == null) return true;
        return info.get(metadata).getValue();
    }
    
    public final boolean generationAllowed(Block block, int metadata) {
        for (BlockAccess access: stoneGenerationSettings.keySet()) {
            if (access.getBlock().equals(block)) {
                HashMap<Integer,BooleanSetting> info = stoneGenerationSettings.get(access);
                if (info == null) return true;
                return info.get(metadata).getValue();
            }
        }
        throw new RuntimeException();
        //return true;// not found in list so default to allowed.
    }

    @Override
    public boolean alternativeSlabTextures() {
        return plainSlabTextures();
    }
}
