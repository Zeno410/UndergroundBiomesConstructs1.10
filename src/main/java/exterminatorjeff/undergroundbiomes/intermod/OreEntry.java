package exterminatorjeff.undergroundbiomes.intermod;

import exterminatorjeff.undergroundbiomes.api.names.Entry;
import exterminatorjeff.undergroundbiomes.api.names.ItemEntry;
import exterminatorjeff.undergroundbiomes.client.UBOreModelResourceLocation;
import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author LouisDB
 */
class OreEntry extends Entry<UBOre> {
  private static String name(Block baseStone, Block baseOre, int meta) {
    ResourceLocation oreResource = baseOre.getRegistryName();
    if (meta == UBOre.NO_METADATA || meta == 0) {
      if (oreResource.getResourceDomain().equals("minecraft")) {
        return baseStone.getRegistryName().getResourcePath() + "_" + oreResource.getResourcePath();
      }
      else {
        return baseStone.getRegistryName().getResourcePath() + "_" + oreResource.getResourceDomain() + "_" + oreResource.getResourcePath();
      }
    }
    ItemStack stack = new ItemStack(baseOre, 1, meta);
    String name = oreResource.getResourceDomain() + "_";
    if (stack.getItem() == new ItemStack(Blocks.AIR, 1, meta).getItem()) {
      name += oreResource.getResourcePath() + "_" + meta;
    } else {
      name = stack.getUnlocalizedName();
    }
    return baseStone.getRegistryName().getResourcePath() + "_" + name;
  }

  public OreEntry(Block baseStone, Block baseOre, int meta) {
    super(name(baseStone, baseOre, meta));
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
  protected void doRegisterItem(IForgeRegistry<Item> registry) {
    getBlock().setUnlocalizedName(internalName);
    registry.register(getItem().setRegistryName(internalName));
  }

  @Override
  protected void doRegisterBlock(IForgeRegistry<Block> registry) {
    getBlock().setUnlocalizedName(internalName);
    registry.register(getBlock().setRegistryName(internalName));
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
