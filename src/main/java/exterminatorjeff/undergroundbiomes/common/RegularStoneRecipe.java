package exterminatorjeff.undergroundbiomes.common;

import com.google.common.collect.Lists;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.names.StoneEntry;
import exterminatorjeff.undergroundbiomes.config.SettingTracker;
import exterminatorjeff.undergroundbiomes.intermod.StonesRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author CurtisA, LouisDB
 *
 */
public final class RegularStoneRecipe implements SettingTracker<Integer> {

	private static final UBLogger LOGGER = new UBLogger(RegularStoneRecipe.class, Level.INFO);

	private final List<IRecipe> recipes = new ArrayList<>();

	@Override
	public void update(Integer value) {
		LOGGER.info("Choosing regular stone recipe n°" + value);
		// Remove previous recipes
		CraftingManager.getInstance().getRecipeList().removeAll(recipes);
		// Create the new recipes
		StonesRegistry.INSTANCE.stonesFor(UBStoneStyle.COBBLE).forEach(new Consumer<StoneEntry>() {
                    @Override
                    public void accept(StoneEntry cobbleEntry) {
                        ItemStack cobble = new ItemStack(cobbleEntry.getItemBlock());
                        switch (value) {
                            case 1:
                                recipes.add(new ShapelessRecipes(new ItemStack(Blocks.COBBLESTONE), Lists.newArrayList(cobble)));
                            case 2:
                                recipes.add(new ShapelessRecipes(new ItemStack(Blocks.COBBLESTONE), Lists.newArrayList(cobble, new ItemStack(Items.REDSTONE))));
                            case 3:
                                recipes.add(new ShapedOreRecipe(new ItemStack(Blocks.COBBLESTONE, 1), "XX", "XX", 'X', "cobblestone"));
//new ShapedRecipes(2, 2, new ItemStack[] { cobble, cobble, cobble, cobble }, new ItemStack(Blocks.COBBLESTONE)));
                            case 4:
                                recipes.add(new ShapedOreRecipe(new ItemStack(Blocks.COBBLESTONE, 4), "XX", "XX", 'X', "cobblestone"));
                                //new ShapedRecipes(2, 2, new ItemStack[] { cobble, cobble, cobble, cobble }, new ItemStack(Blocks.COBBLESTONE, 4)));
                        }
                    }
                });
		// Add the new recipes
		CraftingManager.getInstance().getRecipeList().addAll(recipes);
	}

}
