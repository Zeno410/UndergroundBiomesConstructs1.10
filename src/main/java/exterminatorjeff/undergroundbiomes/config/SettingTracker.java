package exterminatorjeff.undergroundbiomes.config;

/**
 * @param <T>
 * @author LouisDB
 */
@FunctionalInterface
public interface SettingTracker<T> {

  /**
   * @param value The new value
   */
  void update(T value);

}
