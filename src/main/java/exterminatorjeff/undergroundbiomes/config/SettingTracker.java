package exterminatorjeff.undergroundbiomes.config;

/**
 * 
 * @author LouisDB
 *
 * @param <T>
 */
@FunctionalInterface
public interface SettingTracker<T> {

	/**
	 * 
	 * @param value
	 *            The new value
	 */
	void update(T value);

}
