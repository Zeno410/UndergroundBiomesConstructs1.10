package exterminatorjeff.undergroundbiomes.config;

import net.minecraftforge.common.config.Configuration;

/**
 * 
 * @author LouisDB
 *
 */
public class FloatSetting extends Setting<Float> {

	public FloatSetting(String category, String key) {
		super(category, key);
	}

	public Float getValue() {
		return (float) property.getDouble();
	}

	@Override
	protected void createProperty(Configuration configuration, Float defaultValue, String comment) {
		property = configuration.get(category, key, defaultValue, comment);
	}

	@Override
	protected void setValue(Float value) {
		property.setValue(value);
	}

	@Override
	protected boolean valueEquals(Float condition) {
		return condition.equals(property.getDouble());
	}

}
