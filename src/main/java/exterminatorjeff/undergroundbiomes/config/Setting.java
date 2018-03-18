package exterminatorjeff.undergroundbiomes.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Common superclass for all type of settings.
 *
 * @author CurtisA, LouisDB
 *
 * @param <T>
 */
abstract class Setting<T> {

	protected final String category;
	protected final String key;
	protected final List<SettingTracker<T>> trackers = new ArrayList<>();
	protected final List<SettingOverride<T, ?>> overrides = new ArrayList<>();
	protected Property property;

	public Setting(String category, String key) {
		this.category = category;
		this.key = key;
	}

	/**
	 * Initialize the property.
	 *
	 * @param configuration
	 * @param defaultValue
	 * @param comment
	 */
	public void initProperty(Configuration configuration, T defaultValue, String comment) {
		UBConfig.LOGGER.trace("Creating property " + key + " with default value " + defaultValue);
		createProperty(configuration, defaultValue, comment);
		UBConfig.LOGGER.debug("Property " + key + " initialized with value " + getValue());
	}

	protected abstract void createProperty(Configuration configuration, T defaultValue, String comment);

	/**
	 * Change the value of the setting.
	 *
	 * @param value
	 */
	public final void changeValue(T value) {
		UBConfig.LOGGER.trace("Changing value of property " + key + ", previous value: " + getValue());
		setValue(value);
		UBConfig.LOGGER.info("Property " + key + ": value changed to " + value);
		overrides.forEach((setting) -> setting.apply());
		trackers.forEach(tracker -> tracker.update(value));
	}

	protected abstract void setValue(T value);

	/**
	 *
	 * @return The value of the property
	 */
	public abstract T getValue();

	/**
	 * Add a tracker that will get noticed when the value changes.
	 *
	 * @param tracker
	 */
	public final void addTracker(SettingTracker<T> tracker) {
		UBConfig.LOGGER.debug("Adding tracker to property " + key);
		trackers.add(tracker);
	}

	/**
	 * Add a tracker and update it right away.
	 *
	 * @param tracker
	 */
	public final void addTrackerAndUpdate(SettingTracker<T> tracker) {
		UBConfig.LOGGER.debug("Adding tracker to property " + key);
		trackers.add(tracker);
		tracker.update(getValue());
	}

	/**
	 * If this {@link Setting}'s value equals {@code condition}, then force the
	 * value of the given {@link Setting} to {@code forcedValue}.<br>
	 *
	 * Must be called after
	 * {@link #initProperty(Configuration, Object, String)}.
	 *
	 * @param condition
	 * @param setting
	 * @param forcedValue
	 * @return This instance, for convenience
	 */
	public final <U> Setting<T> addOverride(T condition, Setting<U> setting, U forcedValue) {
		UBConfig.LOGGER.debug("Adding override to property " + key);
		UBConfig.LOGGER.debug("If value == " + condition + " -> " + setting.key + " = " + forcedValue);
		overrides.add(new SettingOverride<>(this, condition, setting, forcedValue));
		if (valueEquals(condition))
			setting.setValue(forcedValue);
		return this;
	}

	protected abstract boolean valueEquals(T condition);

}
