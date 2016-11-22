/*
 */

package exterminatorjeff.undergroundbiomes.core;

import exterminatorjeff.undergroundbiomes.api.UBDimensionalStrataColumnProvider;
import exterminatorjeff.undergroundbiomes.api.UBStrataColumn;
import exterminatorjeff.undergroundbiomes.api.UBStrataColumnProvider;
import exterminatorjeff.undergroundbiomes.config.ConfigManager;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.world.WorldGenManager;
import java.io.File;
import java.util.HashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author Zeno410
 */
public class DimensionManager implements UBDimensionalStrataColumnProvider {
    public HashMap<Integer,WorldGenManager> managers = new HashMap();
    private boolean villageRegistered = false;
    private boolean oreRegistered = false;
    private ConfigManager configManager;
    
    public DimensionManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
    
    public void refreshManagers() {
        managers = new HashMap();

        if (UBConfig.SPECIFIC.ubifyVillages()&&!villageRegistered) {
                MinecraftForge.TERRAIN_GEN_BUS.register(this);
                villageRegistered = true;
        }        
        if (!UBConfig.SPECIFIC.ubifyVillages()&&villageRegistered) {
                MinecraftForge.TERRAIN_GEN_BUS.unregister(this);
                villageRegistered = false;
        }
        
        if (UBConfig.SPECIFIC.disableVanillaStoneVariants()&&!oreRegistered) {
                MinecraftForge.ORE_GEN_BUS.register(this);
                oreRegistered = true;
        }
        
        if (!UBConfig.SPECIFIC.disableVanillaStoneVariants()&&oreRegistered) {
                MinecraftForge.ORE_GEN_BUS.unregister(this);
                oreRegistered = false;
        }      
        ((UBConfig)(UBConfig.SPECIFIC)).getUBifiedDimensions().forEach(dimensionID -> {
                WorldGenManager manager = new WorldGenManager(dimensionID);
                managers.put(dimensionID, manager);
        });
        if (1<0)throw new RuntimeException(""+managers.size()+ " " + ((UBConfig)(UBConfig.SPECIFIC)).includedDimensions + 
                ((UBConfig)(UBConfig.SPECIFIC)).getUBifiedDimensions().size());
    }
    
       public void serverLoad(MinecraftServer server) {
            //logger.info("server starting");
            File worldSaveDirectory = null;
            String worldName = server.getFolderName();
            if (server.isSinglePlayer()) {
                File saveDirectory = server.getFile("saves");
                worldSaveDirectory = new File(saveDirectory,worldName);
            } else {
                PropertyManager settings = new PropertyManager(server.getFile("server.properties"));
                worldName = settings.getStringProperty("level-name", worldName);
                worldSaveDirectory = server.getFile(worldName);
            }
            try {
                WorldServer worldServer = server.worldServerForDimension(0);
                File worldLocation = worldServer.getChunkSaveLocation();
                //UndergroundBiomes.logger.info(world.toString() + " " +worldLocation.getAbsolutePath());
                configManager.setWorldFile(worldLocation);
            } catch (NullPointerException e) {
                throw e;
            }    
            refreshManagers();
        }
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
            if (managers.size() == 0) serverLoad(event.getWorld().getMinecraftServer());
            int dimension = event.getWorld().provider.getDimension();
            WorldGenManager target  = managers.get(dimension);
            //if (dimension == 0) throw new RuntimeException(target.toString());
            if (target != null) target.onWorldLoad(event);
        }
        
        @SubscribeEvent
	public void onPopulateChunkPost(PopulateChunkEvent.Post event) {
            int dimension = event.getWorld().provider.getDimension();
            WorldGenManager target  = managers.get(dimension);
            if (target != null) target.onPopulateChunkPost(event);
	}
        
        @SubscribeEvent
	public void onGenerateMinable(OreGenEvent.GenerateMinable event) {
            int dimension = event.getWorld().provider.getDimension();
            WorldGenManager target  = managers.get(dimension);
            if (target != null) target.onGenerateMinable(event);
        }
        @SubscribeEvent
	public void onVillageSelectBlock(BiomeEvent.GetVillageBlockID event) {
            // this goes to the overworld since there's no ID
            WorldGenManager target  = managers.get(0);
            if (target != null) target.onVillageSelectBlock(event);
        }
        
        @SubscribeEvent
	public void initMapGen(InitMapGenEvent event) {
            // this goes to the overworld since there's no ID
            WorldGenManager target  = managers.get(0);
            if (target != null) target.initMapGen(event);
            
        }
        
        public void clearWorldManagers() {
            managers = new HashMap();
        }

    @Override
    public UBStrataColumnProvider ubStrataColumnProvider(int dimension) {
        WorldGenManager manager = this.managers.get(dimension);
        if (manager == null) return vanillaColumnProvider;
        return manager;
    }
    
    private final UBStrataColumnProvider vanillaColumnProvider = new UBStrataColumnProvider() {
        @Override
        public UBStrataColumn strataColumn(int x, int z) {
            return vanillaColumn;
        }
        
    };
    private final UBStrataColumn vanillaColumn  = new UBStrataColumn() {
        
        @Override
        public IBlockState stone(int height) {
            return Blocks.STONE.getDefaultState();
        }

        @Override
        public IBlockState cobblestone(int height) {
            return Blocks.COBBLESTONE.getDefaultState();
        }

        @Override
        public IBlockState stone() {
            return Blocks.STONE.getDefaultState();
        }

        @Override
        public IBlockState cobblestone() {
            return Blocks.COBBLESTONE.getDefaultState();
        }
        
    };
}
