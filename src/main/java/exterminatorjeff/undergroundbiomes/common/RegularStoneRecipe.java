package exterminatorjeff.undergroundbiomes.common;

import com.google.common.collect.Lists;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.names.StoneEntry;
import exterminatorjeff.undergroundbiomes.config.SettingTracker;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author CurtisA, LouisDB
 */
public final class RegularStoneRecipe implements SettingTracker<Integer> {

  private static final UBLogger LOGGER = new UBLogger(RegularStoneRecipe.class, Level.INFO);

  private final List<IRecipe> recipes = new ArrayList<>();

  @Override
  public void update(Integer value) {
//    LOGGER.info("Choosing regular stone recipe n°" + value);
//    RegistryNamespaced<ResourceLocation, IRecipe> recipeRegistry = CraftingManager.REGISTRY;
//    String recipe_name = "undergroundbiomes:regular_cobblestone";
//    ResourceLocation vanilla_cobble = new ResourceLocation(recipe_name);
//    IRecipe cobble_recipe = recipeRegistry.getObject(vanilla_cobble);
//    int cobble_key = recipeRegistry.getIDForObject(cobble_recipe);
//    // Remove previous recipes
////    recipeRegistry.remove(vanilla_cobble);
////    CraftingManager.getRecipeList().removeAll(recipes);
//    // Create the new recipes
//    switch (value) {
//      case 1:
//        cobble_recipe.getIngredients().clear();
//        cobble_recipe.getIngredients().add(OreIngredient.fromItem(new ItemBlock(Blocks.COBBLESTONE)));
//        cobble_recipe.getRecipeOutput().setCount(1);
//        break;
//      case 2:
//        cobble_recipe.getIngredients().clear();
//        cobble_recipe.getIngredients().add(OreIngredient.fromItem(new ItemBlock(Blocks.COBBLESTONE)));
//        cobble_recipe.getIngredients().add(OreIngredient.fromItem(Items.REDSTONE));
//        cobble_recipe.getRecipeOutput().setCount(1);
//        break;
//      case 3:
//        cobble_recipe.getIngredients().clear();
//        cobble_recipe.getIngredients().add(new OreIngredient("cobblestone"));
//        cobble_recipe.getRecipeOutput().setCount(1);
////        cobble_recipe = new ShapedOreRecipe(vanilla_cobble, new ItemStack(Blocks.COBBLESTONE, 1), "XX", "XX", 'X', "cobblestone");
//        break;
//      case 4:
//        cobble_recipe.getIngredients().clear();
//        cobble_recipe.getIngredients().add(OreIngredient.fromItem(new ItemBlock(Blocks.COBBLESTONE)));
//        cobble_recipe.getRecipeOutput().setCount(4);
////        cobble_recipe = new ShapedOreRecipe(vanilla_cobble, new ItemStack(Blocks.COBBLESTONE, 4), "XX", "XX", 'X', "cobblestone");
//    }
  }

}
