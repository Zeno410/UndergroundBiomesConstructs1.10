package exterminatorjeff.undergroundbiomes.world;

import exterminatorjeff.undergroundbiomes.api.UBBiome;
import exterminatorjeff.undergroundbiomes.api.UBStrataColumn;
import exterminatorjeff.undergroundbiomes.api.UBStrataColumnProvider;
import exterminatorjeff.undergroundbiomes.api.UndergroundBiomeSet;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import exterminatorjeff.undergroundbiomes.common.block.slab.UBStoneSlab;
import exterminatorjeff.undergroundbiomes.common.block.stairs.UBStoneStairs;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;
import exterminatorjeff.undergroundbiomes.world.noise.SimplexNoiseGenerator;
import exterminatorjeff.undergroundbiomes.world.noise.Voronoi;
import net.minecraft.block.*;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

/**
 *
 * @author CurtisA, LouisDB
 *
 */
public final class WorldGenManager implements UBStrataColumnProvider {

	private final UBLogger LOGGER;

	private final int dimensionID;
	private final UndergroundBiomeSet biomesSet;
	private final int biomeSize;
        private UBStoneReplacer stoneReplacer;

	private boolean worldLoaded = false;
	private World world;
	private int seed;
        private SimplexNoiseGenerator simplex;
	private Voronoi voronoi;

	public WorldGenManager(int dimensionID) {
		LOGGER = new UBLogger(WorldGenManager.class + " " + dimensionID, Level.INFO);
		LOGGER.debug("Dimension " + dimensionID + " will be UBified");

		this.dimensionID = dimensionID;
		biomesSet = new UBBiomesSet(UBConfig.SPECIFIC);
                        //new UBBiomesSet(UBConfig.INSTANCE.regularStoneBiomes(), UBConfig.INSTANCE);

		biomeSize = UBConfig.SPECIFIC.biomeSize();
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		world = event.getWorld();

		// TODO World specific config
		world.getSaveHandler().getWorldDirectory();

		if (world.provider.getDimension() == dimensionID && !worldLoaded) {
			LOGGER.debug("Dimension " + dimensionID + " loaded");

			worldLoaded = true;

			seed = (int) world.getSeed();
			if (UBConfig.SPECIFIC.dimensionSpecificSeeds())
				seed += dimensionID;
                        this.stoneReplacer = new TraditionalStoneReplacer(seed,UBConfig.SPECIFIC.biomeSize(),biomesSet);
                        //this.stoneReplacer = new SimpleStoneReplacer(this.biomesSet.allowedBiomes(),seed,
                                //UBConfig.INSTANCE.biomeSize());
			simplex = new SimplexNoiseGenerator(seed);
			voronoi = new Voronoi();
			voronoi.setSeed(seed);
			voronoi.setFrequency(0.05D / biomeSize);
		}
	}

	private double perlinNoise(int xPos, int zPos) {
		double var = 0;
		var += simplex.noise(xPos, zPos, 2, 0.005D, 1000D, true) * 4;
		var += simplex.noise(xPos, zPos, 2, 0.025D, 1000D, true);
		return var /= 5;
	}

	private UBBiome blockBiomeValue(int xPos, int zPos) {
            return stoneReplacer.UBBiomeAt(xPos, zPos);
		//double var = perlinNoise(xPos, zPos);
		//double value = voronoi.getValue(xPos, zPos, var * 8 * biomeSize);
		//return (int) ((((value + 1.0D) / 2.0D) * biomesSet.allowedBiomes().length - 1) + 0.5D);
	}



	@SubscribeEvent
	public void onPopulateChunkPost(PopulateChunkEvent.Post event) {
		if (event.getWorld().provider.getDimension() == dimensionID && worldLoaded) {
			Chunk chunk = event.getWorld().getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());
                        this.stoneReplacer.replaceStoneInChunk(chunk);
                        stoneReplacer.redoOres(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onGenerateMinable(GenerateMinable event) {
		if (event.getWorld().provider.getDimension() == dimensionID && worldLoaded) {
			switch (event.getType()) {
			case GRANITE:
			case DIORITE:
			case ANDESITE:
				event.setResult(Result.DENY);
				break;
                        default:
				break;
			}
		}
	}

	/*
	 * Villages
	 */

	private BlockPos pos;
	private boolean hasChanged = false;
	private UBBiome currentBiome;

	/**
	 * Currently this event is not fired for every village structures block. It
	 * will probably be fixed by Forge or Mojang.
	 *
	 * @param event
	 *
	 * @see MinecraftForge/MinecraftForge#3257
	 */
	@SubscribeEvent
	public void onVillageSelectBlock(GetVillageBlockID event) {
		IBlockState originalState = event.getOriginal();
		Block originalBlock = originalState.getBlock();

		if (hasChanged) {
			currentBiome = blockBiomeValue(pos.getX(), pos.getZ());
			hasChanged = false;
		}

                if (currentBiome == null) return;
		if (currentBiome.filler.getBlock() instanceof UBStone) {
			UBStone stone = (UBStone) currentBiome.filler.getBlock();
			if (originalBlock.equals(Blocks.COBBLESTONE))
				event.setReplacement(((UBStone) StonesRegistry.INSTANCE.stoneFor(stone.getStoneType(), UBStoneStyle.COBBLE).getBlock()).getStateFromMeta(currentBiome.meta));

			else if (originalBlock == Blocks.STONE_STAIRS) {
				EnumFacing facing = originalState.getValue(BlockStairs.FACING);
				event.setReplacement(((UBStoneStairs) StonesRegistry.INSTANCE.stairsFor(stone.getStoneType(), UBStoneStyle.COBBLE).getBlock(facing)).getStateFromMeta(currentBiome.meta));
			}

			else if (originalBlock == Blocks.STONE_SLAB) {
				EnumBlockHalf half = originalState.getValue(BlockSlab.HALF);
				event.setReplacement(((UBStoneSlab) StonesRegistry.INSTANCE.slabFor(stone.getStoneType(), UBStoneStyle.STONE).getHalfSlab()).getStateFromMeta(currentBiome.meta).withProperty(BlockSlab.HALF, half));
			}

			else if (originalBlock == Blocks.DOUBLE_STONE_SLAB)
				event.setReplacement(((UBStoneSlab) StonesRegistry.INSTANCE.slabFor(stone.getStoneType(), UBStoneStyle.STONE).getFullSlab()).getStateFromMeta(currentBiome.meta));

			// More ?
                        if (event.getReplacement() == null) {
                            //event.setReplacement(Blocks.END_STONE.getDefaultState());

                        }
                        if (event.getReplacement()!=null &&event.getReplacement().getBlock().equals(Blocks.COBBLESTONE)) {
                            throw new RuntimeException();
                        }
                        if (event.getResult().equals(Result.DENY)) throw new RuntimeException();
			if (event.getReplacement() != null) {
				event.setResult(Result.DENY);
				LOGGER.trace("Replaced village block " + originalBlock.getRegistryName() + " with " + event.getReplacement().getBlock().getRegistryName());
			} else {
                            if (event.getOriginal().getBlock() == Blocks.WOODEN_SLAB) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.LOG) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.PLANKS) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.STONE_SLAB) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.DOUBLE_STONE_SLAB) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.GLASS_PANE) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.AIR) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.TORCH) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.OBSIDIAN) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.WOODEN_PRESSURE_PLATE) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.BOOKSHELF) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.CHEST) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.CRAFTING_TABLE) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.FURNACE) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.IRON_BARS) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.LAVA) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.OAK_FENCE) {
            return;
        }
        if (event.getOriginal().getBlock() instanceof BlockDoor) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.OAK_STAIRS) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.DIRT) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.WHEAT) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.CARROTS) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.POTATOES) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.FARMLAND) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.WATER) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.LADDER) {
            return;
        }
        if (event.getOriginal().getBlock() instanceof BlockColored) {
            return;
        }
        if (event.getOriginal().getBlock() instanceof BlockDynamicLiquid) {
            return;
        }
        if (event.getOriginal().getBlock() == Blocks.GRASS_PATH) {
            return;
        }

        if (event.getOriginal().getBlock() == Blocks.GRAVEL) {
            return;
        }

        if (originalBlock instanceof UBStone) {
            return;
        }
        if (1>0) throw new RuntimeException(event.getOriginal().getBlock().toString());
                        }
		}
	}

	@SubscribeEvent
	public void initMapGen(InitMapGenEvent event) {
		if (event.getType() == EventType.VILLAGE) {
			event.setNewGen(new MapGenVillage() {
				@Override
				public StructureStart getStructureStart(int chunkX, int chunkZ) {
					LOGGER.debug("Village start in chunk: " + chunkX + ";" + chunkZ);
					pos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
					hasChanged = true;
					return super.getStructureStart(chunkX, chunkZ);
				}
			});
			event.setResult(Result.DENY);
		}
	}

    @Override
    public UBStrataColumn strataColumn(int x, int z) {
        return this.stoneReplacer.strataColumn(x, z);
    }


}
