package exterminatorjeff.undergroundbiomes.intermod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.common.UBModOreRegistrar;
import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ModOreRegistrar implements UBModOreRegistrar {

  private static final UBLogger LOGGER = new UBLogger(OresRegistry.class, Level.INFO);
  private final File directory;
  private ArrayList<ModOre> ores = new ArrayList<>();
  private final Type jsonType = new TypeToken<ArrayList<ModOre>>() {
  }.getType();

  public ModOreRegistrar(FMLPreInitializationEvent event) {
    this.directory = Paths.get(event.getModConfigurationDirectory().toString(), "undergroundbiomes", "ores").toFile();
    try {
      Files.createDirectories(this.directory.toPath());
      createDefaults();
      setupOres();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void requestOreSetup(RegistryEvent.Register<Block> event, ModOre ore) {
    ResourceLocation location = new ResourceLocation(ore.internalOreName);
    IForgeRegistry<Block> registry = event.getRegistry();
    if (!registry.containsKey(location)) {
      return;
    }
    Block block = registry.getValue(location);
    API.ORES_REGISTRY.requestOreSetup(block, ore.meta);
    API.ORES_REGISTRY.registerOreOverlay(block, ore.meta, new ResourceLocation(ore.overlay));
  }

  public void requestOreSetups(RegistryEvent.Register<Block> event) {
    for (int i = 0; i < ores.size(); i++) {
      requestOreSetup(event, ores.get(i));
    }
  }

  private void createDefaults() {
    writeDefaults(getActuallyAdditionsOres(), "actuallyadditions.json");
    writeDefaults(getAppliedEnergisticsOres(), "appliedenergistics2.json");
    writeDefaults(getBaseMetalsOres(), "basemetals.json");
    writeDefaults(getBaseMineralsOres(), "baseminerals.json");
    writeDefaults(getBiomesOPlentyOres(), "biomesoplenty.json");
    writeDefaults(getBetterUndergroundOres(), "betterunderground.json");
    writeDefaults(getDraconicEvolutionOres(), "draconicevolution.json");
    writeDefaults(getEnderOreOres(), "enderore.json");
    writeDefaults(getExtremeReactorsOres(), "extremereactors.json");
    writeDefaults(getForestyOres(), "forestry.json");
    writeDefaults(getImmersiveEngineeringOres(), "immersiveengineering.json");
    writeDefaults(getIndustrialCraftOres(), "industrialcraft.json");
    writeDefaults(getMekanismOres(), "mekanism.json");
    writeDefaults(getModernMetalsOres(), "modernmetals.json");
    writeDefaults(getRFToolsOres(), "rftools.json");
    writeDefaults(getTechRebornOres(), "techreborn.json");
    writeDefaults(getThermalFoundationOres(), "thermalfoundation.json");
    writeDefaults(getThaumcraftOres(), "thaumcraft.json");
  }

  private void writeDefaults(ArrayList<ModOre> ores, String filename) {
    try {
      Path filepath = Paths.get(this.directory.toString(), filename);
      if (!filepath.toFile().exists()) {
        Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .create();
        String json = gson.toJson(ores, jsonType);
        Files.write(filepath, Arrays.asList(json.split("\n")), Charset.forName("UTF-8"));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setupOres() {
    for (final File fileEntry : directory.listFiles()) {
      if (fileEntry.isFile() && FilenameUtils.getExtension(fileEntry.getPath()).equals("json")) {
        readFile(fileEntry);
      }
    }
  }

  private void readFile(File file) {
    try {
      Gson gson = new Gson();
      String json = null;
      json = FileUtils.readFileToString(file, "UTF8");
      ArrayList<ModOre> ores = gson.fromJson(json, jsonType);
      if (ores != null) {
        this.ores.addAll(ores);
      } else {
        LOGGER.warn("No ores found in " + file.getPath());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private ArrayList<ModOre> getActuallyAdditionsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "actuallyadditions:block_misc";
    ores.add(new ModOre(oreName, 3, ModInfo.MODID + ":blocks/overlays/custom/black_quarz"));
    return ores;
  }

  private ArrayList<ModOre> getAppliedEnergisticsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("appliedenergistics2:quartz_ore", "appliedenergistics2:blocks/charged_quartz_ore_light"));
    ores.add(new ModOre("appliedenergistics2:charged_quartz_ore", "appliedenergistics2:blocks/charged_quartz_ore_light"));
    return ores;
  }

  private ArrayList<ModOre> getBaseMetalsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("basemetals:antimony_ore", ModInfo.MODID + ":blocks/overlays/basemetals/antimony"));
    ores.add(new ModOre("basemetals:bismuth_ore", ModInfo.MODID + ":blocks/overlays/basemetals/bismuth"));
    ores.add(new ModOre("basemetals:copper_ore", ModInfo.MODID + ":blocks/overlays/thermalfoundation/copper"));
    ores.add(new ModOre("basemetals:lead_ore", ModInfo.MODID + ":blocks/overlays/basemetals/lead"));
    ores.add(new ModOre("basemetals:mercury_ore", ModInfo.MODID + ":blocks/overlays/basemetals/mercury"));
    ores.add(new ModOre("basemetals:nickel_ore", ModInfo.MODID + ":blocks/overlays/basemetals/nickel"));
    ores.add(new ModOre("basemetals:platinum_ore", ModInfo.MODID + ":blocks/overlays/basemetals/platinum"));
    ores.add(new ModOre("basemetals:silver_ore", ModInfo.MODID + ":blocks/overlays/thermalfoundation/silver"));
    ores.add(new ModOre("basemetals:tin_ore", ModInfo.MODID + ":blocks/overlays/basemetals/tin"));
    ores.add(new ModOre("basemetals:zinc_ore", ModInfo.MODID + ":blocks/overlays/basemetals/zinc"));
    return ores;
  }

  private ArrayList<ModOre> getBaseMineralsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("baseminerals:lithium_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/lithium"));
    ores.add(new ModOre("baseminerals:nither_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/nither"));
    ores.add(new ModOre("baseminerals:phosphorus_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/phosphorus"));
    ores.add(new ModOre("baseminerals:potash_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/potash"));
    ores.add(new ModOre("baseminerals:salt_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/salt"));
    ores.add(new ModOre("baseminerals:saltpeter_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/saltpeter"));
    ores.add(new ModOre("baseminerals:sulfur_ore", ModInfo.MODID + ":blocks/overlays/baseminerals/sulfur"));
    return ores;
  }

  private ArrayList<ModOre> getBetterUndergroundOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("betterunderground:blockfossils", ModInfo.MODID + ":blocks/overlays/custom/fossil"));
    return ores;
  }

  private ArrayList<ModOre> getBiomesOPlentyOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "biomesoplenty:gem_ore";
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/custom/ruby"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/custom/peridot"));
    ores.add(new ModOre(oreName, 3, ModInfo.MODID + ":blocks/overlays/custom/topaz"));
    ores.add(new ModOre(oreName, 4, ModInfo.MODID + ":blocks/overlays/custom/tanzanite"));
    ores.add(new ModOre(oreName, 5, ModInfo.MODID + ":blocks/overlays/custom/malachite"));
    ores.add(new ModOre(oreName, 6, ModInfo.MODID + ":blocks/overlays/custom/sapphire"));
    ores.add(new ModOre(oreName, 7, ModInfo.MODID + ":blocks/overlays/custom/amber"));
    return ores;
  }

  private ArrayList<ModOre> getDraconicEvolutionOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("draconicevolution:draconium_ore", 0,ModInfo.MODID + ":blocks/overlays/draconicevolution/draconium"));
    return ores;
  }

  private ArrayList<ModOre> getEnderOreOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("enderore:ore_ender", ModInfo.MODID + ":blocks/overlays/enderore/ender"));
    return ores;
  }

  private ArrayList<ModOre> getExtremeReactorsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("bigreactors:brore", 0,ModInfo.MODID + ":blocks/overlays/bigreactors/yellorite"));
    return ores;
  }

  private ArrayList<ModOre> getForestyOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "forestry:resources";
    ores.add(new ModOre(oreName, 0, ModInfo.MODID + ":blocks/overlays/custom/apatite"));
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/thermalfoundation/copper"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/thermalfoundation/tin"));
    return ores;
  }

  private ArrayList<ModOre> getIndustrialCraftOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "ic2:resource";
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/thermalfoundation/copper"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/thermalfoundation/lead"));
    ores.add(new ModOre(oreName, 3, ModInfo.MODID + ":blocks/overlays/thermalfoundation/tin"));
    ores.add(new ModOre(oreName, 4, ModInfo.MODID + ":blocks/overlays/custom/uranium"));
    return ores;
  }

  private ArrayList<ModOre> getImmersiveEngineeringOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "immersiveengineering:ore";
    ores.add(new ModOre(oreName, 0, ModInfo.MODID + ":blocks/overlays/thermalfoundation/copper"));
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/thermalfoundation/aluminum"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/thermalfoundation/lead"));
    ores.add(new ModOre(oreName, 3, ModInfo.MODID + ":blocks/overlays/thermalfoundation/silver"));
    ores.add(new ModOre(oreName, 4, ModInfo.MODID + ":blocks/overlays/thermalfoundation/nickel"));
    ores.add(new ModOre(oreName, 5, ModInfo.MODID + ":blocks/overlays/custom/uranium"));
    return ores;
  }

  private ArrayList<ModOre> getMekanismOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "mekanism:oreblock";
    ores.add(new ModOre(oreName, 0, ModInfo.MODID + ":blocks/overlays/mekanism/osmium"));
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/mekanism/copper"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/mekanism/tin"));
    return ores;
  }

  private ArrayList<ModOre> getModernMetalsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("modernmetals:aluminum_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/aluminum"));
    ores.add(new ModOre("modernmetals:beryllium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/beryllium"));
    ores.add(new ModOre("modernmetals:boron_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/boron"));
    ores.add(new ModOre("modernmetals:cadmium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/cadmium"));
    ores.add(new ModOre("modernmetals:chromium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/chromium"));
    ores.add(new ModOre("modernmetals:iridium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/iridium"));
    ores.add(new ModOre("modernmetals:magnesium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/magnesium"));
    ores.add(new ModOre("modernmetals:manganese_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/manganese"));
    ores.add(new ModOre("modernmetals:osmium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/osmium"));
    ores.add(new ModOre("modernmetals:plutonium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/plutonium"));
    ores.add(new ModOre("modernmetals:rutile_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/rutile"));
    ores.add(new ModOre("modernmetals:tantalum_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/tantalum"));
    ores.add(new ModOre("modernmetals:titanium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/titanium"));
    ores.add(new ModOre("modernmetals:tungsten_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/tungsten"));
    ores.add(new ModOre("modernmetals:uranium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/uranium"));
    ores.add(new ModOre("modernmetals:zirconium_ore", ModInfo.MODID + ":blocks/overlays/modernmetals/zirconium"));
    return ores;
  }

  private ArrayList<ModOre> getRFToolsOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("rftools:dimensional_shard_ore", 0, ModInfo.MODID + ":blocks/overlays/rftools/dimensionalshard"));
    return ores;
  }

  private ArrayList<ModOre> getTechRebornOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "techreborn:ore";
    ores.add(new ModOre(oreName, 0, ModInfo.MODID + ":blocks/overlays/techreborn/galena"));
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/techreborn/iridium"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/techreborn/ruby"));
    ores.add(new ModOre(oreName, 3, ModInfo.MODID + ":blocks/overlays/techreborn/sapphire"));
    ores.add(new ModOre(oreName, 4, ModInfo.MODID + ":blocks/overlays/techreborn/bauxite"));
    ores.add(new ModOre(oreName, 12, ModInfo.MODID + ":blocks/overlays/techreborn/lead"));
    ores.add(new ModOre(oreName, 13, ModInfo.MODID + ":blocks/overlays/techreborn/silver"));
    return ores;
  }

  private ArrayList<ModOre> getThaumcraftOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    ores.add(new ModOre("thaumcraft:ore_cinnabar", ModInfo.MODID + ":blocks/overlays/custom/cinnabar"));
    return ores;
  }

  private ArrayList<ModOre> getThermalFoundationOres() {
    ArrayList<ModOre> ores = new ArrayList<>();
    String oreName = "thermalfoundation:ore";
    ores.add(new ModOre(oreName, 0, ModInfo.MODID + ":blocks/overlays/thermalfoundation/copper"));
    ores.add(new ModOre(oreName, 1, ModInfo.MODID + ":blocks/overlays/thermalfoundation/tin"));
    ores.add(new ModOre(oreName, 4, ModInfo.MODID + ":blocks/overlays/thermalfoundation/aluminum"));
    ores.add(new ModOre(oreName, 3, ModInfo.MODID + ":blocks/overlays/thermalfoundation/lead"));
    ores.add(new ModOre(oreName, 2, ModInfo.MODID + ":blocks/overlays/thermalfoundation/silver"));
    ores.add(new ModOre(oreName, 5, ModInfo.MODID + ":blocks/overlays/thermalfoundation/nickel"));
    ores.add(new ModOre(oreName, 6, ModInfo.MODID + ":blocks/overlays/thermalfoundation/platinum"));
    ores.add(new ModOre(oreName, 7, ModInfo.MODID + ":blocks/overlays/thermalfoundation/iridium"));
    ores.add(new ModOre(oreName, 8, ModInfo.MODID + ":blocks/overlays/thermalfoundation/mana_infused"));
    ores.add(new ModOre("thermalfoundation:ore_fluid", 2, ModInfo.MODID + ":blocks/overlays/thermalfoundation/destabilized_redstone"));
    return ores;
  }

  class ModOre {
    public String internalOreName;
    public int meta = UBOre.NO_METADATA;
    public String overlay;

    public ModOre(String internalOreName, int meta, String overlay) {
      this.internalOreName = internalOreName;
      this.meta = meta;
      this.overlay = overlay;
    }

    public ModOre(String ore_name, String overlay) {
      this(ore_name, UBOre.NO_METADATA, overlay);
    }
  }
}
