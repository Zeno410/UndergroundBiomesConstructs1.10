package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.common.IUBOreConfig;
import exterminatorjeff.undergroundbiomes.common.block.UBOre;

import java.util.ArrayList;

public class UBOreConfig implements IUBOreConfig {
  private String internalOreName;
  private int meta = UBOre.NO_METADATA;
  private String overlay;
  private String color;
  private ArrayList<String> oreDirectories;

  public UBOreConfig(String internalOreName, int meta, String overlay, ArrayList<String> oreDirectories, String color) {
    this.internalOreName = internalOreName;
    this.meta = meta;
    this.overlay = overlay;
    this.oreDirectories = oreDirectories;
    this.color = color;
  }

  public UBOreConfig(String internalOreName, int meta, String overlay, ArrayList<String> oreDirectories) {
    this(internalOreName, meta, overlay, oreDirectories, null);
  }

  public UBOreConfig(String internalOreName, int meta, String overlay) {
    this(internalOreName, meta, overlay, new ArrayList<String>());
  }

  public UBOreConfig(String ore_name, String overlay) {
    this(ore_name, UBOre.NO_METADATA, overlay, new ArrayList<String>());
  }

  public UBOreConfig(String ore_name, String overlay, ArrayList<String> oreDirectories) {
    this(ore_name, UBOre.NO_METADATA, overlay, oreDirectories);
  }

  public String toKey() {
    return internalOreName + ":" + meta;
  }

  public String getInternalOreName() {
    return internalOreName;
  }

  public int getMeta() {
    return meta;
  }

  public String getOverlay() {
    return overlay;
  }

  public String getColor() {
    return color;
  }

  public ArrayList<String> getOreDirectories() {
    if(oreDirectories == null)
      return new ArrayList<>();
    return oreDirectories;
  }
}
