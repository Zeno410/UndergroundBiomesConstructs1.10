package exterminatorjeff.undergroundbiomes.common;

import com.google.common.collect.Lists;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.names.StoneEntry;
import exterminatorjeff.undergroundbiomes.config.SettingTracker;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author CurtisA, LouisDB
 */
public final class RegularStoneRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe, SettingTracker<Integer> {

  private static final UBLogger LOGGER = new UBLogger(RegularStoneRecipe.class, Level.INFO);

  private IRecipe recipe = null;
  private String registryName = "undergroundbiomes:regular_cobblestone";
  private ResourceLocation resourceLocation = null;

  public RegularStoneRecipe() {
    recipe = new ShapedOreRecipe(resourceLocation, new ItemStack(Blocks.COBBLESTONE, 4), "XX", "XX", 'X', "cobblestone");
    setRegistryName(registryName);
  }

  @Override
  public void update(Integer value) {
    LOGGER.info("Choosing regular stone recipe n°" + value);

    // Create the new recipes
    switch (value) {
      case 1:
        recipe = new ShapelessOreRecipe(resourceLocation, Blocks.COBBLESTONE, new OreIngredient("cobblestone"));
        break;
      case 2:
        recipe = new ShapelessOreRecipe(resourceLocation, Blocks.COBBLESTONE, new OreIngredient("cobblestone"), Items.REDSTONE);
        break;
      case 3:
        recipe = new ShapedOreRecipe(resourceLocation, new ItemStack(Blocks.COBBLESTONE, 1), "XX", "XX", 'X', "cobblestone");
        break;
      case 4:
        recipe = new ShapedOreRecipe(resourceLocation, new ItemStack(Blocks.COBBLESTONE, 4), "XX", "XX", 'X', "cobblestone");
    }
    LOGGER.debug("recipe output" + recipe.getRecipeOutput().getDisplayName());
  }


  /**
   * Used to check if a recipe matches current crafting inventory
   *
   * @param inv
   * @param worldIn
   */
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn)
  {
    LOGGER.debug("Matching stone recipe: " + recipe.matches(inv, worldIn));
    return recipe.matches(inv, worldIn);
  }

  /**
   * Returns an Item that is the result of this recipe
   *
   * @param inv
   */
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return recipe.getCraftingResult(inv);
  }

  /**
   * Used to determine if this recipe can fit in a grid of the given width/height
   *
   * @param width
   * @param height
   */
  @Override
  public boolean canFit(int width, int height) {
    return recipe.canFit(width, height);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return recipe.getRecipeOutput();
  }
}
