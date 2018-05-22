package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.common.UBOresRegistry;
import exterminatorjeff.undergroundbiomes.client.UBStateMappers;
import exterminatorjeff.undergroundbiomes.common.block.*;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.core.UndergroundBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.util.*;


/**
 * @author LouisDB
 * @see UBOre
 */
public enum OresRegistry implements UBOresRegistry {
  INSTANCE;

  /*
   * Logging
   */

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);

  private final String SETUP_ERROR_MSG = "Cannot setup UBOres for '%s', " + ModInfo.NAME + "'s block registering has not started yet!";
  private final String SETUP_INFO_MSG = "The ore '%s' has been successfully UBfied.";
  private final String REQUEST_ERROR_MSG = "Cannot request UBOres setup for '%s', " + ModInfo.NAME + "'s block registering is done!";
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
    boolean result = ubifiedOres.containsKey(toKey(baseOre, baseOreMeta, baseStone));
    return result;
  }

  /**
   * Get the UBified version of an ore.<br>
   * Must be called after checking {@link #isUBified(Block, IBlockState)}.
   *
   * @param baseStone
   * @param baseStoneMeta
   * @param baseOreState
   */
  public IBlockState getUBifiedOre(UBStone baseStone, int baseStoneMeta, IBlockState baseOreState) {
    Block baseOre = baseOreState.getBlock();
    int baseOreMeta = baseOre.getMetaFromState(baseOreState);
    return ubifiedOres.get(toKey(baseOre, baseOreMeta, baseStone)).ore().getStateFromMeta(baseStoneMeta);
  }

  /**
   * Add smelting recipes for UBified versions.
   */
  private void applyBaseOreSmelting(Block baseOre, int meta, OreEntry... ores) {
    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(baseOre, 1, meta));
    if (result != null && result.getItem() != (new ItemStack(Blocks.AIR)).getItem()) {
      for (OreEntry ore : ores)
        for (int i = 0; i < ore.ore().getNbVariants(); ++i)
          GameRegistry.addSmelting(new ItemStack(ore.getBlock(), 1, i), result, FurnaceRecipes.instance().getSmeltingExperience(result));
    }
  }

  public void registerBlocks(RegistryEvent.Register<Block> event) {
    for (UBifyRequest request : requests) {
      Block baseOre = request.baseOre;
      int baseOreMeta = request.baseOreMeta;
      LOGGER.info("Registering ore: " + baseOre.getUnlocalizedName());
      request.getIgneousOreEntry().registerBlock(event, new UBOreIgneous(baseOre, baseOreMeta));
      request.getMetamorphicOreEntry().registerBlock(event, new UBOreMetamorphic(baseOre, baseOreMeta));
      request.getSedimentraryOreEntry().registerBlock(event, new UBOreSedimentary(baseOre, baseOreMeta));
      ubifiedOres.put(toKey(baseOre, baseOreMeta, API.IGNEOUS_STONE.getBlock()), request.getIgneousOreEntry());
      ubifiedOres.put(toKey(baseOre, baseOreMeta, API.METAMORPHIC_STONE.getBlock()), request.getMetamorphicOreEntry());
      ubifiedOres.put(toKey(baseOre, baseOreMeta, API.SEDIMENTARY_STONE.getBlock()), request.getSedimentraryOreEntry());
    }
  }

  public void registerItems(RegistryEvent.Register<Item> event) {
    for (UBifyRequest request : requests) {
      request.getIgneousOreEntry().registerItem(event, new UBOreIgneous(request.baseOre, request.baseOreMeta));
      request.getMetamorphicOreEntry().registerItem(event, new UBOreMetamorphic(request.baseOre, request.baseOreMeta));
      request.getSedimentraryOreEntry().registerItem(event, new UBOreSedimentary(request.baseOre, request.baseOreMeta));
    }
  }

  public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
    //TODO
    for (UBifyRequest request : requests) {
      applyBaseOreSmelting(request.baseOre, request.baseOreMeta, request.getIgneousOreEntry(), request.getMetamorphicOreEntry(),
        request.getSedimentraryOreEntry());
    }
  }

  @Override
  public void requestOreSetup(Block baseOre) {
    if (UndergroundBiomes.areBlocksAlreadyRegistered || alreadySetup)
      throw new RuntimeException(format(REQUEST_ERROR_MSG, baseOre));
    else {
      requests.add(new UBifyRequest(baseOre));
      LOGGER.debug(format(REQUEST_INFO_MSG, baseOre));
    }
  }

  @Override
  public void requestOreSetup(Block baseOre, int baseOreMeta) {
    if (UndergroundBiomes.areBlocksAlreadyRegistered || alreadySetup)
      throw new RuntimeException(format(REQUEST_ERROR_MSG, baseOre, baseOreMeta));
    else {
      requests.add(new UBifyRequest(baseOre, baseOreMeta));
      LOGGER.debug(format(REQUEST_INFO_MSG, baseOre, baseOreMeta));
    }
  }

  private class UBifyRequest {
    protected final Block baseOre;
    protected final int baseOreMeta;
    protected OreEntry igneousOreEntry;
    protected OreEntry metamorphicOreEntry;
    protected OreEntry sedimentraryOreEntry;

    UBifyRequest(Block baseOre) {
      this(baseOre, UBOre.NO_METADATA);
    }

    UBifyRequest(Block baseOre, int baseOreMeta) {
      if (baseOre == null) throw new RuntimeException();
      this.baseOre = baseOre;
      this.baseOreMeta = baseOreMeta;
    }

    public OreEntry getIgneousOreEntry() {
      if (igneousOreEntry == null)
        this.igneousOreEntry = new OreEntry(API.IGNEOUS_STONE.getBlock(), baseOre, baseOreMeta);
      return igneousOreEntry;
    }

    public OreEntry getMetamorphicOreEntry() {
      if (metamorphicOreEntry == null)
        this.metamorphicOreEntry = new OreEntry(API.METAMORPHIC_STONE.getBlock(), baseOre, baseOreMeta);
      return metamorphicOreEntry;
    }

    public OreEntry getSedimentraryOreEntry() {
      if (sedimentraryOreEntry == null)
        this.sedimentraryOreEntry = new OreEntry(API.SEDIMENTARY_STONE.getBlock(), baseOre, baseOreMeta);
      return sedimentraryOreEntry;
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
    return baseOre.getRegistryName() + "." + baseOreMeta;
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
    registerOreOverlay(Blocks.COAL_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/coal"));
    registerOreOverlay(Blocks.DIAMOND_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/diamond"));
    registerOreOverlay(Blocks.EMERALD_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/emerald"));
    registerOreOverlay(Blocks.GOLD_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/gold"));
    registerOreOverlay(Blocks.IRON_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/iron"));
    registerOreOverlay(Blocks.LAPIS_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/lapis"));
    registerOreOverlay(Blocks.REDSTONE_ORE, new ResourceLocation(ModInfo.MODID + ":blocks/overlays/minecraft/redstone"));
  }

  /**
   * Must be called during pre-init
   */
  public void registerOreModels() {
    ubifiedOres.values().forEach((oreEntry) -> oreEntry.registerModel(UBStateMappers.UBORE_STATE_MAPPER));
  }

  public void copyOreDictionaries() {
    for (OreEntry oreEntry : ubifiedOres.values()) {
      LOGGER.info("Copying ore dictionaries");
      copyOreDictionary(oreEntry);
    }
  }

  private void copyOreDictionary(OreEntry oreEntry) {
    Block block = oreEntry.getBlock();
    Block baseOre = oreEntry.ore().baseOre;
    int baseOreMeta = oreEntry.ore().baseOreMeta;
    ItemStack baseOreStack = null;
    if (baseOreMeta == UBOre.NO_METADATA) {
      baseOreStack = new ItemStack(baseOre, 1);
    } else {
      baseOreStack = new ItemStack(baseOre, 1, baseOreMeta);
    }
    int[] registrationIDs = OreDictionary.getOreIDs(baseOreStack);
    for (int i = 0; i < registrationIDs.length; i++) {
      String registrationName = OreDictionary.getOreName(registrationIDs[i]);
      LOGGER.info(baseOre.getLocalizedName() + " " + registrationName + " " + block.getLocalizedName());
      for (int j = 0; j < 8; j++) {
        ItemStack stack = new ItemStack(block, 1, j);
        OreDictionary.registerOre(registrationName, stack);
      }
    }
  }

  @SubscribeEvent
  public void registerOverlayTextures(TextureStitchEvent.Pre e) {
    oresToOverlays.values().forEach((overlayLocation) -> e.getMap().registerSprite(overlayLocation));
  }

  private int dimension(IBlockAccess access) {
    return ((World) access).provider.getDimension();
  }

  private final HashMap<Integer, HashMap<ChunkPos, ArrayList<BlockPos>>> storedLocations = new HashMap();

  private final ArrayList<BlockPos> blockPosList(IBlockAccess world, ChunkPos chunkID) {
    int dimension = dimension(world);
    HashMap<ChunkPos, ArrayList<BlockPos>> worldMap = storedLocations.get(dimension);
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
    synchronized (storedLocations) {
      ChunkPos chunkID = new ChunkPos(pos);
      blockPosList(world, chunkID).add(pos);
    }
  }

  public HashMap<ChunkPos, ArrayList<BlockPos>> forRedo(IBlockAccess world) {
    HashMap<ChunkPos, ArrayList<BlockPos>> result = null;
    synchronized (storedLocations) {
      int dimension = dimension(world);
      result = storedLocations.get(dimension);
      if (result == null) result = new HashMap();
      storedLocations.remove(dimension);
    }
    return result;
  }

  public void recheckPile() {
    int result = 0;
    for (Integer world : this.storedLocations.keySet()) {
      int worldResult = 0;
      HashMap<ChunkPos, ArrayList<BlockPos>> chunkPosMap = storedLocations.get(world);
      for (ChunkPos chunkPos : chunkPosMap.keySet()) {
        worldResult += chunkPosMap.get(chunkPos).size();
      }
      result += worldResult;
      System.out.println("" + worldResult + " Blocks in World : " + world.toString());
    }
    System.out.println("Blocks queued for redo: " + result);
  }
}
