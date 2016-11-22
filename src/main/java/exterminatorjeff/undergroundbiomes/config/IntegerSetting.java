package exterminatorjeff.undergroundbiomes.config;

import net.minecraftforge.common.config.Configuration;

/**
 * 
 * @author LouisDB
 *
 */
public class IntegerSetting extends Setting<Integer> {

	public IntegerSetting(String category, String key) {
		super(category, key);
	}

	public Integer getValue() {
		return property.getInt();
	}

	@Override
	protected void createProperty(Configuration configuration, Integer defaultValue, String comment) {
		property = configuration.get(category, key, defaultValue, comment);
	}

	@Override
	protected void setValue(Integer value) {
		property.setValue(value);
	}

	@Override
	protected boolean valueEquals(Integer condition) {
		return condition.equals(property.getInt());
	}

}
