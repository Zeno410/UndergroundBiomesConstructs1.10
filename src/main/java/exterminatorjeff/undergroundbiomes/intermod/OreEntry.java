package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.names.Entry;
import exterminatorjeff.undergroundbiomes.client.UBOreModelResourceLocation;
import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 
 * @author LouisDB
 *
 */
class OreEntry extends Entry<UBOre> {

	public OreEntry(Block baseStone, Block baseOre) {
		super(baseStone.getRegistryName().getResourcePath() + "_" + baseOre.getRegistryName().getResourcePath());
	}

	public Block getBlock() {
		return getThing();
	}

	public Item getItem() {
		return getThing().getItemBlock();
	}

	protected UBOre ore() {
		return getThing();
	}

	@Override
	protected void doRegister() {
		getBlock().setUnlocalizedName(internalName);
		GameRegistry.register(getBlock().setRegistryName(internalName));
		GameRegistry.register(getItem(), getBlock().getRegistryName());
	}

	@Override
	protected void doRegisterModel(IStateMapper stateMapper) {
		// Block
		ModelLoader.setCustomStateMapper(getBlock(), stateMapper);
		// Item
		for (int meta = 0; meta < ore().getNbVariants(); ++meta) {
			ModelResourceLocation location = new UBOreModelResourceLocation(ore(), meta);
			ModelLoader.setCustomModelResourceLocation(getItem(), meta, location);
		}

	}

}
