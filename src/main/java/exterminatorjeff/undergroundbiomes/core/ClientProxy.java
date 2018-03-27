package exterminatorjeff.undergroundbiomes.core;

import exterminatorjeff.undergroundbiomes.api.API;
import exterminatorjeff.undergroundbiomes.client.UBCreativeTab;
import exterminatorjeff.undergroundbiomes.client.UBOreModelLoader;
import exterminatorjeff.undergroundbiomes.client.UBStateMappers;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.intermod.OresRegistry;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author CurtisA, LouisDB
 */
public final class ClientProxy extends CommonProxy {

  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);

    MinecraftForge.EVENT_BUS.register(OresRegistry.INSTANCE);
    ModelLoaderRegistry.registerLoader(new UBOreModelLoader());
    OresRegistry.INSTANCE.addVanillaOverlays();
    OresRegistry.INSTANCE.registerOreModels();
  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);

    UBCreativeTab.UB_BLOCKS_TAB.setTabIconItem(API.IGNEOUS_BRICK.getItemBlock());
    UBCreativeTab.UB_ITEMS_TAB.setTabIconItem(API.LIGNITE_COAL.getItem());
    // TODO
    //UBCreativeTab.UB_ORES_TAB.setTabIconItem(OresRegistry.INSTANCE.getUBOresTabIcon());
  }

  @Override
  public void postInit(FMLPostInitializationEvent e) {
    super.postInit(e);

  }

  public void registerModels(ModelRegistryEvent event) {
    registerBlocksModels();
    registerItemsModels();
  };

  private final void registerBlocksModels() {
    // Stones
    API.IGNEOUS_STONE.registerModel();
    API.IGNEOUS_COBBLE.registerModel();
    API.IGNEOUS_BRICK.registerModel();
    API.METAMORPHIC_STONE.registerModel();
    API.METAMORPHIC_COBBLE.registerModel();
    API.METAMORPHIC_BRICK.registerModel();
    API.SEDIMENTARY_STONE.registerModel();
    // Slabs
    if (!UBConfig.SPECIFIC.alternativeSlabTextures()) {
      API.IGNEOUS_STONE_SLAB.registerModel(UBStateMappers.UBSLAB_STATE_MAPPER);
      API.METAMORPHIC_STONE_SLAB.registerModel(UBStateMappers.UBSLAB_STATE_MAPPER);
    } else {
      API.IGNEOUS_STONE_SLAB.registerModel();
      API.METAMORPHIC_STONE_SLAB.registerModel();
    }
    API.IGNEOUS_COBBLE_SLAB.registerModel();
    API.METAMORPHIC_COBBLE_SLAB.registerModel();
    API.IGNEOUS_BRICK_SLAB.registerModel();
    API.METAMORPHIC_BRICK_SLAB.registerModel();
    API.SEDIMENTARY_STONE_SLAB.registerModel();
    // Buttons
    API.IGNEOUS_STONE_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    API.IGNEOUS_COBBLE_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    API.IGNEOUS_BRICK_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    API.METAMORPHIC_STONE_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    API.METAMORPHIC_COBBLE_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    API.METAMORPHIC_BRICK_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    API.SEDIMENTARY_STONE_BUTTON.registerModel(UBStateMappers.UBBUTTON_STATE_MAPPER);
    // Walls
    API.IGNEOUS_STONE_WALL.registerModel();
    API.IGNEOUS_COBBLE_WALL.registerModel();
    API.IGNEOUS_BRICK_WALL.registerModel();
    API.METAMORPHIC_STONE_WALL.registerModel();
    API.METAMORPHIC_COBBLE_WALL.registerModel();
    API.METAMORPHIC_BRICK_WALL.registerModel();
    API.SEDIMENTARY_STONE_WALL.registerModel();
    // Stairs
    API.IGNEOUS_STONE_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
    API.IGNEOUS_COBBLE_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
    API.IGNEOUS_BRICK_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
    API.METAMORPHIC_STONE_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
    API.METAMORPHIC_COBBLE_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
    API.METAMORPHIC_BRICK_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
    API.SEDIMENTARY_STONE_STAIRS.registerModel(UBStateMappers.UBSTAIRS_STATE_MAPPER);
  }

  private final void registerItemsModels() {
    API.LIGNITE_COAL.registerModel();
    API.FOSSIL_PIECE.registerModel();
  }

}
