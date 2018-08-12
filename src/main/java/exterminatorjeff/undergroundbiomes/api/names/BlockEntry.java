package exterminatorjeff.undergroundbiomes.api.names;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author LouisDB
 */
public abstract class BlockEntry extends Entry<UBBlock> implements BlockAccess {

  public BlockEntry(String internalName) {
    super(internalName);
  }

  public BlockEntry(Block block) {
    super(block.getUnlocalizedName());
    // sanitize inputs
    if (getBlock() == null) throw new RuntimeException();
  }

  public Block getBlock() {
    return getThing().toBlock();
  }

  public ItemBlock getItemBlock() {
    return getThing().getItemBlock();
  }

  protected UBBlock getUBBlock() {
    return getThing();
  }

  @Override
  protected void doRegisterItem(IForgeRegistry<Item> registry) {
    registry.register(getItemBlock());
  }

  @Override
  protected void doRegisterBlock(IForgeRegistry<Block> registry) {
    registry.register(getBlock());
  }

  public void registerModel() {
    super.registerModel(null);
  }

  @Override
  protected void doRegisterModel(IStateMapper stateMapper) {
    for (int meta = 0; meta < getUBBlock().getNbVariants(); ++meta) {
      ModelResourceLocation location = new ModelResourceLocation(externalName(internalName), "type=" + getUBBlock().getVariantName(meta));
      ModelLoader.setCustomModelResourceLocation(getItemBlock(), meta, location);
      LOGGER.debug("Model location: " + location);
    }
  }

}
