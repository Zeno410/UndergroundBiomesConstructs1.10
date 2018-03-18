package exterminatorjeff.undergroundbiomes.api.common;

import exterminatorjeff.undergroundbiomes.api.enums.UBStoneStyle;
import exterminatorjeff.undergroundbiomes.api.enums.UBStoneType;
import exterminatorjeff.undergroundbiomes.common.block.UBStone;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Common interface for all UB blocks.
 *
 * @author LouisDB
 *
 */
public interface UBBlock extends IForgeRegistryEntry<Block>, Variable {

	Block toBlock();

	Item getItemBlock();

	UBStoneType getStoneType();

	UBStoneStyle getStoneStyle();

	UBStone baseStone();

}
