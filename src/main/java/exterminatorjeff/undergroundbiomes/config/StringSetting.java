package exterminatorjeff.undergroundbiomes.config;

import net.minecraftforge.common.config.Configuration;

/**
 * 
 * @author LouisDB
 *
 */
public class StringSetting extends Setting<String> {

	public StringSetting(String category, String key) {
		super(category, key);
	}

	public String getValue() {
		return property.getString();
	}

	@Override
	protected void createProperty(Configuration configuration, String defaultValue, String comment) {
		property = configuration.get(category, key, defaultValue, comment);
	}

	@Override
	protected void setValue(String value) {
		property.setValue(value);
	}

	@Override
	protected boolean valueEquals(String condition) {
		return condition.equals(property.getString());
	}

}
