package exterminatorjeff.undergroundbiomes.api.names;

import exterminatorjeff.undergroundbiomes.api.common.UBButton;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;


/**
 * @author LouisDB
 */
public final class ButtonEntry extends Entry<UBButton> {

  public ButtonEntry(StoneEntry baseStoneEntry) {
    super(baseStoneEntry.internalName + "_button");
    baseStoneEntry.button = this;
  }

  public Item getItemBlock() {
    return (Item) getThing();
  }

  public Block getBlock(EnumFacing facing) {
    return getThing().getBlock(facing);
  }

  protected UBButton button() {
    return getThing();
  }

  @Override
  protected void doRegisterItem(IForgeRegistry<Item> registry) {
    getItemBlock().setUnlocalizedName(internalName);
    getItemBlock().setRegistryName(internalName);
    registry.register(getItemBlock());
  }

  @Override
  protected void doRegisterBlock(IForgeRegistry<Block> registry) {
    getItemBlock().setUnlocalizedName(internalName);
    getItemBlock().setRegistryName(internalName);
    for (EnumFacing facing : EnumFacing.VALUES) {
      String name = internalName + "_" + facing;
      Block block = getBlock(facing);
      block.setUnlocalizedName(name);
      registry.register(block.setRegistryName(name));
    }
  }

  @Override
  protected void doRegisterModel(IStateMapper stateMapper) {
    for (EnumFacing facing : EnumFacing.VALUES)
      ModelLoader.setCustomStateMapper(getBlock(facing), stateMapper);
    for (int meta = 0; meta < button().getNbVariants(); ++meta) {
      String variants = "type=" + button().getVariantName(meta);
      ModelResourceLocation location = new ModelResourceLocation(externalName(internalName), variants);
      ModelLoader.setCustomModelResourceLocation(getItemBlock(), meta, location);
      LOGGER.debug("Model location: " + location);
    }
  }

}
