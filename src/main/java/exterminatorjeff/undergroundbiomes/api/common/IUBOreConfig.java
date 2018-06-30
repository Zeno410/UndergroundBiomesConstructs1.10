package exterminatorjeff.undergroundbiomes.api.common;

import java.util.ArrayList;

public interface IUBOreConfig {

  public String toKey();

  public String getInternalOreName();

  public int getMeta();

  public String getOverlay();

  public String getColor();

  public int getLightValue();

  public ArrayList<String> getOreDirectories();
}
