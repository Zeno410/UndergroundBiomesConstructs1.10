package exterminatorjeff.undergroundbiomes.client;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LouisDB
 */
@SideOnly(Side.CLIENT)
public class UBOreModelLoader implements ICustomModelLoader {

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager) {
  }

  @Override
  public boolean accepts(ResourceLocation modelLocation) {
    return modelLocation.toString().contains(UBOreModel.UBORE_MODEL_PATH);
  }

  @Override
  public IModel loadModel(ResourceLocation modelLocation) {
    String modelName = modelLocation.getResourcePath();
    if (modelName.contains(UBOreModel.UBORE_MODEL_NAME))
      return new UBOreModel((UBOreModelResourceLocation) modelLocation);
    throw new RuntimeException("A builtin model '" + modelName + "' is not defined.");
  }

}
