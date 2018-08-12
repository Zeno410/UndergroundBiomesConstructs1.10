package exterminatorjeff.undergroundbiomes.common;

import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.config.SettingTracker;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import org.apache.logging.log4j.Level;

/**
 * @author LouisDB
 */
public final class ButtonRecipe implements SettingTracker<Integer> {

  private static final UBLogger LOGGER = new UBLogger(ButtonRecipe.class, Level.INFO);

  /*
   *
   */

  @Override
  public void update(Integer n) {
    LOGGER.info("Modifying buttons recipes to output " + n + " buttons");
    // Retrieve and modify button recipes
    CraftingManager.REGISTRY.getKeys().forEach(recipe_key -> {
      IRecipe recipe = CraftingManager.REGISTRY.getObject(recipe_key);
      ItemStack output = recipe.getRecipeOutput();
      if (output == null)
        return;

      Item item = output.getItem();
      Block block = Block.getBlockFromItem(item);

      if ( //
        (block != null && (block == Blocks.STONE_BUTTON || block == Blocks.WOODEN_BUTTON)) || //
          (StonesRegistry.INSTANCE.allButtons().contains(item)) || //
          (item != null &&
            item.getRegistryName() != null &&
            item.getRegistryName().getResourcePath() != null &&
            item.getRegistryName().getResourcePath().contains("button")) //
        ) {
        recipe.getRecipeOutput().setCount(n);
        LOGGER.debug(String.format("%s for '%s' modified", recipe.getClass().getSimpleName(), recipe.getRecipeOutput().getItem().getRegistryName()));
      }
    });
  }

}
