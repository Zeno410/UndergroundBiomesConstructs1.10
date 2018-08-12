package exterminatorjeff.undergroundbiomes.api.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;

/**
 * Allows mods to UBifiy their ores.<br>
 * <br>
 * The setupOre methods must be called after UB pre-init<br>
 * The requestOreSetup methods must be called before UB pre-init<br>
 *
 * @author CurtisA, LouisDB
 */
public interface UBOresRegistry {


  public void registerBlocks(RegistryEvent.Register<Block> event);
  public void registerItems(RegistryEvent.Register<Item> event);
  public void registerRecipes(RegistryEvent.Register<IRecipe> event);

  /**
   * Request creation of UBified versions for the given ore with the given
   * metadata value.
   *
   * @param baseOre
   * @param config
   */
  void requestOreSetup(Block baseOre, IUBOreConfig config);

  /**
   * Register an overlay for the given ore.
   *
   * @param baseOre
   * @param overlayLocation
   */
  void registerOreOverlay(Block baseOre, ResourceLocation overlayLocation);

  /**
   * Register an overlay for the given ore.
   *
   * @param baseOre
   * @param baseOreMeta
   * @param overlayLocation
   */
  void registerOreOverlay(Block baseOre, int baseOreMeta, ResourceLocation overlayLocation);

}
