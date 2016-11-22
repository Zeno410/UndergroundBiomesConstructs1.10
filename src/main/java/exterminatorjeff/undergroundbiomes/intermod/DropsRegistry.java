package exterminatorjeff.undergroundbiomes.intermod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.api.common.DropSource;
import exterminatorjeff.undergroundbiomes.api.common.UBDropsRegistry;
import exterminatorjeff.undergroundbiomes.api.enums.FossilVariant;
import exterminatorjeff.undergroundbiomes.api.enums.SedimentaryVariant;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Rare drops for {@link UBStone}s.
 * 
 * @author CurtisA, LouisDB
 *
 */
public enum DropsRegistry implements UBDropsRegistry {
	INSTANCE;

	/**
	 * Should not be accessed directly.
	 * 
	 * @see #addSourceFor(Block, DropSource)
	 * @see #getSourcesFor(Block)
	 */
	private final Map<Block, List<DropSource>> dropsSources = new HashMap<>();

	private void addSourceFor(Block stone, DropSource source) {
		if (!dropsSources.containsKey(stone))
			dropsSources.put(stone, new ArrayList<>());
		dropsSources.get(stone).add(source);
	}

	private List<DropSource> getSourcesFor(Block stone) {
		List<DropSource> sources = dropsSources.get(stone);
		if (sources == null)
			return Collections.emptyList();
		return sources;
	}

	@Override
	public void registerDropSourceFor(Block stone, DropSource source) {
		addSourceFor(stone, source);
	}

	/**
	 * Must be called during pre-init, after blocks and items have been created.
	 */
	public void init() {
		addSourceFor(API.IGNEOUS_STONE.getBlock(), (drops, world, pos, state, fortune) -> {
			if (pos.getY() <= 32 && world.rand.nextInt(100) <= fortune)
				drops.add(new ItemStack(Items.GOLD_NUGGET));
		});
		addSourceFor(API.METAMORPHIC_STONE.getBlock(), (drops, world, pos, state, fortune) -> {
			if (world.rand.nextInt(100) <= fortune) {
				if (pos.getY() <= 16)
					drops.add(new ItemStack(Items.REDSTONE));
				else if (pos.getY() <= 32)
					drops.add(new ItemStack(Items.DYE, 1, 4)); // Lapis lazuli
			}
		});
		addSourceFor(API.SEDIMENTARY_STONE.getBlock(), (drops, world, pos, state, fortune) -> {
			if (world.rand.nextInt(100) <= fortune) {
				switch (state.getValue(SedimentaryVariant.SEDIMENTARY_VARIANT_PROPERTY)) {
				case CHALK:
					drops.add(new ItemStack(API.FOSSIL_PIECE.getItem(), 1, world.rand.nextInt(FossilVariant.NB_VARIANTS)));
					break;
				case DOLOMITE:
				case LIGNITE:
				case LIMESTONE:
					drops.add(new ItemStack(API.FOSSIL_PIECE.getItem(), 1, world.rand.nextInt(FossilVariant.NB_VARIANTS)));
					break;
				case SILTSTONE:
					drops.add(new ItemStack(API.FOSSIL_PIECE.getItem(), 1, world.rand.nextInt(FossilVariant.NB_VARIANTS)));
					break;
				case SHALE:
					drops.add(new ItemStack(Items.CLAY_BALL));
					break;
				case CHERT:
					drops.add(new ItemStack(Items.FLINT));
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * Add random drops for the given stone into the {@code drops} list.
	 * 
	 * @param drops
	 * @param stone
	 * @param world
	 * @param pos
	 * @param state
	 * @param fortune
	 */
	public void addDrops(List<ItemStack> drops, Block stone, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		for (DropSource source : getSourcesFor(stone))
			source.addDrops(drops, (World) world, pos, state, fortune);
	}

}
