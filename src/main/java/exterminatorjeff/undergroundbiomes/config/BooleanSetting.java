package exterminatorjeff.undergroundbiomes.config;

import net.minecraftforge.common.config.Configuration;

/**
 * @author LouisDB
 */
public class BooleanSetting extends Setting<Boolean> {

  public BooleanSetting(String category, String key) {
    super(category, key);
  }

  public Boolean getValue() {
    return property.getBoolean();
  }

  @Override
  protected void createProperty(Configuration configuration, Boolean defaultValue, String comment) {
    property = configuration.get(category, key, defaultValue, comment);
  }

  @Override
  protected void setValue(Boolean value) {
    property.setValue(value);
  }

  @Override
  protected boolean valueEquals(Boolean condition) {
    return condition.equals(property.getBoolean());
  }

}
