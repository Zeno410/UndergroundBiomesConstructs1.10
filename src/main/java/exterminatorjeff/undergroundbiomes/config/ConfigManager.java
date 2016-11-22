package exterminatorjeff.undergroundbiomes.config;
/*
 */

import java.io.File;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This class manages overall and world-specific configs
 * @author Zeno410
 */
public class ConfigManager {
    //static Logger logger = new Zeno410Logger("ConfigManager").logger();
    public final static String CONFIG_DIRECTORY = "worldSpecificConfig";
    private File generalConfigFile;
    private UBConfig masterSettings;
    private UBConfig generalSettings;
    private File worldConfigFile;
    private UBConfig worldSpecific;
    
    public ConfigManager(FMLPreInitializationEvent event) {
        this(new UBConfig(),
                event.getSuggestedConfigurationFile());
    }

    public ConfigManager(UBConfig settings, File generalFile) {
        this.masterSettings = settings;
        this.generalConfigFile  = generalFile;
        generalSettings = new UBConfig();
        generalSettings.init(generalConfigFile);
        masterSettings.init(generalConfigFile);
        //if (1>0) throw new RuntimeException(""+generalSettings.crashOnProblems());
        UBConfig.SPECIFIC = masterSettings;
    }

    private boolean usable(File tested) {
        return tested != null;
    }

    public void saveWorldSpecific() {
        worldSpecific.save();
    }

    private void setWorldConfigFile(File newFile) {
        // refresh general for hot changes
        generalSettings = new UBConfig();
        generalSettings.init(generalConfigFile);
        
        if ((worldConfigFile== null)||(!newFile.getAbsolutePath().equals(worldConfigFile.getAbsolutePath()))) {
            worldConfigFile = newFile;
            if (usable(worldConfigFile)) {
                // usable world
                    //logger.info(worldConfigFile.getPath());
                if (newFile.exists()) {
                    worldSpecific = new UBConfig();
                    worldSpecific.init(worldConfigFile);
                } else {
                    worldSpecific = new UBConfig();
                    worldSpecific.init(worldConfigFile);
                    worldSpecific.copy(generalSettings);
                }
                masterSettings.copy(worldSpecific);
                worldSpecific.save();
            } else {
                //logger.info("null file");
                worldSpecific = null;
                //settings.readFrom(general);
            }
        }
    }

    public void setWorldFile(File newFile) {
        // this is the world save directory
        File configDirectory = new File(newFile,CONFIG_DIRECTORY);
        configDirectory.mkdir();

        String configName = generalConfigFile.getPath();
        String generalConfigDirectoryName = generalConfigFile.getParentFile().getPath();
        String detailName = configName.substring(generalConfigDirectoryName.length()+1);
        //logger.info("Filename "+detailName);
        File localConfigFile = new File(configDirectory,detailName);
        setWorldConfigFile(localConfigFile);
    }

    public void clearWorldFile() {
        worldConfigFile = null;
        worldSpecific = null;
        //logger.info("clearing ");
    }

    public void setWorldFile(WorldServer server) {
        setWorldFile(server.getChunkSaveLocation());
    }
    
}
