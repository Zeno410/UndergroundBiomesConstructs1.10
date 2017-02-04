package exterminatorjeff.undergroundbiomes.intermod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.Level;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.common.UBOresRegistry;
import exterminatorjeff.undergroundbiomes.client.UBStateMappers;
import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import exterminatorjeff.undergroundbiomes.common.block.UBOreIgneous;
import exterminatorjeff.undergroundbiomes.common.block.UBOreMetamorphic;
import exterminatorjeff.undergroundbiomes.common.block.UBOreSedimentary;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.core.UndergroundBiomes;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;


/**
 * 
 * @author LouisDB
 * 
 * @see UBOre
 */
public enum OresRegistry implements UBOresRegistry {
	INSTANCE;

	/*
	 * Logging
	 */

	private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

	private final String SETUP_ERROR_MSG = "Cannot setup UBOres for '%s', " + ModInfo.NAME + "'s pre-init is not done yet!";
	private final String SETUP_INFO_MSG = "The ore '%s' has been successfully UBfied.";
	private final String REQUEST_ERROR_MSG = "Cannot request UBOres setup for '%s', " + ModInfo.NAME + "'s pre-init is done!";
	private final String REQUEST_INFO_MSG = "Request for '%s' to be UBfied added.";

	private String format(String message, Block baseOre) {
		return String.format(message, baseOre.getRegistryName());
	}

	private String format(String message, Block baseOre, int meta) {
		return String.format(message, baseOre.getRegistryName() + ":" + meta);
	}

	/*
	 * Ores registering
	 */

	private boolean alreadySetup = false;
	private final Set<UBifyRequest> requests = new HashSet<UBifyRequest>();
	private final Map<String, OreEntry> ubifiedOres = new HashMap<>();

	private String toKey(Block baseOre, int baseOreMeta, Block baseStone) {
		if (baseOreMeta == UBOre.NO_METADATA)
			baseOreMeta = 0;
		return baseOre.getRegistryName() + ":" + baseOreMeta + ":" + baseStone.getRegistryName();
	}

	/**
	 * Check if the given ore has been UBified.
	 * 
	 * @param baseStone
	 * @param baseOreState
	 */
	public boolean isUBified(Block baseStone, IBlockState baseOreState) {
            if (UBConfig.SPECIFIC.ubifyOres() == false) return false;
		Block baseOre = baseOreState.getBlock();
		int baseOreMeta = baseOre.getMetaFromState(baseOreState);
		return ubifiedOres.containsKey(toKey(baseOre, baseOreMeta, baseStone));
	}

	/**
	 * Get the UBified version of an ore.<br>
	 * Must be called after checking {@link #isUBified(Block, IBlockState)}.
	 * 
	 * @param baseStone
	 * @param baseStoneMeta
	 * @param baseOreState
	 */
	public IBlockState getUBifiedOre(Block baseStone, int baseStoneMeta, IBlockState baseOreState) {
		Block baseOre = baseOreState.getBlock();
		int baseOreMeta = baseOre.getMetaFromState(baseOreState);
		return ubifiedOres.get(toKey(baseOre, baseOreMeta, baseStone)).ore().getStateFromMeta(baseStoneMeta);
	}

	/**
	 * Add smelting recipes for UBified versions.
	 */
	private void applyBaseOreSmelting(Block baseOre, OreEntry... ores) {
		ItemStack result = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(baseOre));
		if (result != null) {
			for (OreEntry ore : ores)
				for (int i = 0; i < ore.ore().getNbVariants(); ++i)
					GameRegistry.addSmelting(new ItemStack(ore.getBlock(), 1, i), result, FurnaceRecipes.instance().getSmeltingExperience(result));
		}
	}

	private void createOre(Block baseOre) {
		createOre(baseOre, UBOre.NO_METADATA);
	}

	private void createOre(Block baseOre, int baseOreMeta) {
		OreEntry igneousOre = new OreEntry(API.IGNEOUS_STONE.getBlock(), baseOre);
		OreEntry metamorphicOre = new OreEntry(API.METAMORPHIC_STONE.getBlock(), baseOre);
		OreEntry sedimentaryOre = new OreEntry(API.SEDIMENTARY_STONE.getBlock(), baseOre);
		igneousOre.register(new UBOreIgneous(baseOre, baseOreMeta));
		metamorphicOre.register(new UBOreMetamorphic(baseOre, baseOreMeta));
		sedimentaryOre.register(new UBOreSedimentary(baseOre, baseOreMeta));
		ubifiedOres.put(toKey(baseOre, baseOreMeta, API.IGNEOUS_STONE.getBlock()), igneousOre);
		ubifiedOres.put(toKey(baseOre, baseOreMeta, API.METAMORPHIC_STONE.getBlock()), metamorphicOre);
		ubifiedOres.put(toKey(baseOre, baseOreMeta, API.SEDIMENTARY_STONE.getBlock()), sedimentaryOre);
		//
		applyBaseOreSmelting(baseOre, igneousOre, metamorphicOre, sedimentaryOre);
                ItemStack stack = null;
                if (baseOreMeta == UBOre.NO_METADATA) {
                    stack =new ItemStack(baseOre,1);
                } else {
                    stack =new ItemStack(baseOre,1,baseOreMeta);
                }
            int [] registrationIDs = OreDictionary.getOreIDs(stack); 
            for (int i = 0; i < registrationIDs.length; i++) {
                String registrationName = OreDictionary.getOreName(i);
                for (int j = 0; j < 8; j++) {
                    ItemStack igneousStack = new ItemStack(API.IGNEOUS_STONE.getBlock(),1,j);
                    OreDictionary.registerOre(registrationName, API.IGNEOUS_STONE.getBlock());
                    OreDictionary.registerOre(registrationName, API.IGNEOUS_STONE.getItemBlock());
                    OreDictionary.registerOre(registrationName, igneousStack);
                    ItemStack metamorphicStack = new ItemStack(API.METAMORPHIC_STONE.getBlock(),1,j);
                    OreDictionary.registerOre(registrationName, API.METAMORPHIC_STONE.getBlock());
                    OreDictionary.registerOre(registrationName, API.METAMORPHIC_STONE.getItemBlock());
                    OreDictionary.registerOre(registrationName, metamorphicStack);
                    ItemStack sedimentaryStack = new ItemStack(API.SEDIMENTARY_STONE.getBlock(),1,j);
                    OreDictionary.registerOre(registrationName, API.SEDIMENTARY_STONE.getBlock());
                    OreDictionary.registerOre(registrationName, API.SEDIMENTARY_STONE.getItemBlock());
                    OreDictionary.registerOre(registrationName, sedimentaryStack);
                }
            }
	}

	@Override
	public void setupOre(Block baseOre) {
		if (UndergroundBiomes.isPreInitDone) {
			createOre(baseOre);
			LOGGER.debug(format(SETUP_INFO_MSG, baseOre));
		} else
			throw new RuntimeException(format(SETUP_ERROR_MSG, baseOre));
	}

	@Override
	public void setupOre(Block baseOre, int baseOreMeta) {
		if (UndergroundBiomes.isPreInitDone) {
			createOre(baseOre, baseOreMeta);
			LOGGER.debug(format(SETUP_INFO_MSG, baseOre, baseOreMeta));
		} else
			throw new RuntimeException(format(SETUP_ERROR_MSG, baseOre, baseOreMeta));
	}

	@Override
	public void requestOreSetup(Block baseOre) {
		if (UndergroundBiomes.isPreInitDone || alreadySetup)
			throw new RuntimeException(format(REQUEST_ERROR_MSG, baseOre));
		else {
			requests.add(new UBifyRequest(baseOre));
			LOGGER.debug(format(REQUEST_INFO_MSG, baseOre));
		}
	}

	@Override
	public void requestOreSetup(Block baseOre, int baseOreMeta) {
		if (UndergroundBiomes.isPreInitDone || alreadySetup)
			throw new RuntimeException(format(REQUEST_ERROR_MSG, baseOre, baseOreMeta));
		else {
			requests.add(new UBifyRequestMeta(baseOre, baseOreMeta));
			LOGGER.debug(format(REQUEST_INFO_MSG, baseOre, baseOreMeta));
		}
	}

	/**
	 * Must be called during pre-init
	 */
	public void fulfillRequests() {
		if (!alreadySetup) {
			alreadySetup = true;
			for (UBifyRequest request : requests)
				request.fulfill();
			requests.clear();
		}
	}

	private class UBifyRequest {
		protected final Block baseOre;

		UBifyRequest(Block baseOre) {
			this.baseOre = baseOre;
		}

		void fulfill() {
			createOre(baseOre);
			LOGGER.debug(format(SETUP_INFO_MSG, baseOre));
		}
	}

	private class UBifyRequestMeta extends UBifyRequest {
		protected final int baseOreMeta;

		UBifyRequestMeta(Block baseOre, int baseOreMeta) {
			super(baseOre);
			this.baseOreMeta = baseOreMeta;
		}

		@Override
		void fulfill() {
			createOre(baseOre, baseOreMeta);
			LOGGER.debug(format(SETUP_INFO_MSG, baseOre, baseOreMeta));
		}
	}

	/**
	 * Get a random {@link ItemStack} to be used as creative tab icon for
	 * {@link UBOre}s.
	 */
	public ItemStack getUBOresTabIcon() {
		Random random = new Random();
		Object[] ores = ubifiedOres.values().toArray();
		UBOre ore = ((OreEntry) ores[random.nextInt(ores.length)]).ore();
		return new ItemStack(Item.getItemFromBlock(ore), 1, random.nextInt(ore.getNbVariants()));
	}

	/*
	 * Overlays registering
	 */

	private final Map<String, ResourceLocation> oresToOverlays = new HashMap<>();

	private String toKey(Block baseOre, int baseOreMeta) {
		if (baseOreMeta == UBOre.NO_METADATA)
			baseOreMeta = 0;
		return baseOre.getRegistryName() + ":" + baseOreMeta;
	}

	private ResourceLocation getOverlayFor(String key) {
		ResourceLocation location = oresToOverlays.get(key);
		if (location == null)
			LOGGER.error("There is no registered overlay for '" + key + "'!");
		else
			LOGGER.debug("Found overlay for '" + key + "': " + location);
		return location;
	}

	public ResourceLocation getOverlayFor(Block baseOre) {
		return getOverlayFor(toKey(baseOre, UBOre.NO_METADATA));
	}

	public ResourceLocation getOverlayFor(Block baseOre, int baseOreMeta) {
		return getOverlayFor(toKey(baseOre, baseOreMeta));
	}

	private void registerOreOverlay(String key, ResourceLocation overlayLocation) {
		if (!oresToOverlays.containsKey(key)) {
			oresToOverlays.put(key, overlayLocation);
			LOGGER.debug("Overlay for '" + key + "' registered.");
		} else
			LOGGER.warn("An overlay for '" + key + "' has already been registered!");
	}

	@Override
	public void registerOreOverlay(Block baseOre, ResourceLocation overlayLocation) {
		registerOreOverlay(toKey(baseOre, UBOre.NO_METADATA), overlayLocation);
	}

	@Override
	public void registerOreOverlay(Block baseOre, int baseOreMeta, ResourceLocation overlayLocation) {
		registerOreOverlay(toKey(baseOre, baseOreMeta), overlayLocation);
	}

	/**
	 * Must be called during pre-init
	 */
	public void addVanillaOverlays() {
		registerOreOverlay(Blocks.COAL_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/coal_overlay"));
		registerOreOverlay(Blocks.DIAMOND_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/diamond_overlay"));
		registerOreOverlay(Blocks.EMERALD_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/emerald_overlay"));
		registerOreOverlay(Blocks.GOLD_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/gold_overlay"));
		registerOreOverlay(Blocks.IRON_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/iron_overlay"));
		registerOreOverlay(Blocks.LAPIS_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/lapis_overlay"));
		registerOreOverlay(Blocks.REDSTONE_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/redstone_overlay"));
	}

	/**
	 * Must be called during pre-init
	 */
	public void registerOreModels() {
		ubifiedOres.values().forEach((oreEntry) -> oreEntry.registerModel(UBStateMappers.UBORE_STATE_MAPPER));
	}

	@SubscribeEvent
	public void registerOverlayTextures(TextureStitchEvent.Pre e) {
		oresToOverlays.values().forEach((overlayLocation) -> e.getMap().registerSprite(overlayLocation));
	}

        private int dimension(IBlockAccess access) {
            return ((World)access).provider.getDimension();
        }
        private final HashMap<Integer,HashMap<ChunkPos,ArrayList<BlockPos>>> storedLocations = new HashMap();
        
        private final ArrayList<BlockPos> blockPosList(IBlockAccess world, ChunkPos chunkID) {
            int dimension = dimension(world);
            HashMap<ChunkPos,ArrayList<BlockPos>> worldMap = storedLocations.get(dimension);
            if (worldMap == null) {
                worldMap = new HashMap();
                storedLocations.put(dimension, worldMap);
            }
            ArrayList<BlockPos> result = worldMap.get(chunkID);
            if (result == null) {
                result = new ArrayList();
                worldMap.put(chunkID, result);
            }
            return result;
        }
        
        public void setRecheck(IBlockAccess world, BlockPos pos) {
            synchronized(storedLocations) {
                ChunkPos chunkID = new ChunkPos(pos);
                blockPosList(world, chunkID).add(pos);
            }
        }
        
        public HashMap<ChunkPos,ArrayList<BlockPos>> forRedo(IBlockAccess world) {
            HashMap<ChunkPos,ArrayList<BlockPos>> result = null;
            synchronized(storedLocations) {
                int dimension = dimension(world);
                result = storedLocations.get(dimension);
                if (result == null) result = new HashMap();
                storedLocations.remove(dimension);
            }
            return result;
        }
        
        public void recheckPile() {
            int result = 0;
            for (Integer world: this.storedLocations.keySet()) {
                int worldResult = 0;
                HashMap<ChunkPos,ArrayList<BlockPos>> chunkPosMap = storedLocations.get(world);
                for (ChunkPos chunkPos: chunkPosMap.keySet()) {
                    worldResult += chunkPosMap.get(chunkPos).size();
                }
                result += worldResult;
                System.out.println("" + worldResult + " Blocks in World : "+world.toString());
            }
            System.out.println("Blocks queued for redo: "+result);
        }
}
