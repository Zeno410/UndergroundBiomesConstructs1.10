package exterminatorjeff.undergroundbiomes.api.names;

import exterminatorjeff.undergroundbiomes.api.common.UBSlab;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author LouisDB
 */
public final class SlabEntry extends Entry<UBSlab> {

  public SlabEntry(StoneEntry baseStoneEntry) {
    super(baseStoneEntry.internalName);
    baseStoneEntry.slab = this;
  }

  public Block getHalfSlab() {
    return getThing().getHalfSlab();
  }

  public Block getFullSlab() {
    return getThing().getFullSlab();
  }

  public Item getItem() {
    return getThing().toItem();
  }

  protected UBSlab slab() {
    return getThing();
  }

  @Override
  protected void doRegisterItem(IForgeRegistry<Item> registry) {
    String name = internalName + "_halfslab";
    registry.register(getItem().setRegistryName(name));

  }

  @Override
  protected void doRegisterBlock(IForgeRegistry<Block> registry) {
    String name = internalName + "_halfslab";
    getHalfSlab().setUnlocalizedName(name);
    registry.register(getHalfSlab().setRegistryName(name));
    name = internalName + "_fullslab";
    getFullSlab().setUnlocalizedName(name);
    registry.register(getFullSlab().setRegistryName(name));
  }

  public void registerModel() {
    super.registerModel(null);
  }

  @Override
  protected void doRegisterModel(IStateMapper stateMapper) {
    if (stateMapper != null) {
      ModelLoader.setCustomStateMapper(getHalfSlab(), stateMapper);
      ModelLoader.setCustomStateMapper(getFullSlab(), stateMapper);
    }
    for (int meta = 0; meta < slab().getNbVariants(); ++meta) {
      String variant = "half=bottom,type=" + slab().getVariantName(meta);
      ModelResourceLocation location = new ModelResourceLocation(externalName(internalName + "_halfslab"), variant);
      ModelLoader.setCustomModelResourceLocation(getItem(), meta, location);
      LOGGER.debug("Model location: " + location);
    }
  }

}
