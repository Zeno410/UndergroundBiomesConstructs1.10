package exterminatorjeff.undergroundbiomes.client;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.api.common.UBLogger;
import exterminatorjeff.undergroundbiomes.common.block.UBOre;
import exterminatorjeff.undergroundbiomes.intermod.OresRegistry;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.Collection;

/**
 * The model used to render {@link UBOre} blocks.<br>
 * One instance per UBOre instance.
 *
 * @author LouisDB
 *
 */
@SideOnly(Side.CLIENT)
public class UBOreModel implements IModel {

	public static final UBLogger LOGGER = new UBLogger(UBOreModel.class, Level.INFO);
	public static final String UBORE_MODEL_NAME = "custom_ore";
	public static final String UBORE_MODEL_PATH = ModInfo.MODID + ":block/" + UBORE_MODEL_NAME;

	private final ResourceLocation stoneTexture;
	private final ResourceLocation oreTexture;
	private final IModel baseModel;

	public UBOreModel(UBOreModelResourceLocation location) {
		IModel baseModel = null;
		try {
			baseModel = ModelLoaderRegistry.getModel(new ResourceLocation(UBORE_MODEL_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.baseModel = baseModel;
		stoneTexture = new ResourceLocation(ModInfo.MODID + ":blocks/" + location.getVariant());
		oreTexture = OresRegistry.INSTANCE.getOverlayFor(location.ubOre.baseOre, location.ubOre.baseOreMeta);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableSet.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.of();
	}

	@Override
  public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		IModel finalModel = baseModel.retexture(ImmutableMap.of("stone", stoneTexture.toString(), "ore", oreTexture.toString()));
		return finalModel.bake(state, format, bakedTextureGetter);
	}

  @Override
	public IModelState getDefaultState() {
		return ModelRotation.X0_Y0;
	}

}
