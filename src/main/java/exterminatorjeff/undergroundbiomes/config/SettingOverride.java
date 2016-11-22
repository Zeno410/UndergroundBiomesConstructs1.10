package exterminatorjeff.undergroundbiomes.config;

/**
 * A {@link SettingOverride} allows to force a setting's value depending on
 * another.
 * 
 * @author LouisDB
 *
 * @param <T>
 * @param <U>
 */
public final class SettingOverride<T, U> {

	private final Setting<T> condition;
	private final T conditionValue;
	private final Setting<U> forced;
	private final U forcedValue;

	public SettingOverride(Setting<T> condition, T conditionValue, Setting<U> forced, U forcedValue) {
		this.condition = condition;
		this.conditionValue = conditionValue;
		this.forced = forced;
		this.forcedValue = forcedValue;
	}

	/**
	 * Change the value if the condition is met
	 */
	public void apply() {
		if (condition.valueEquals(conditionValue))
			forced.changeValue(forcedValue);
	}

}
