package exterminatorjeff.undergroundbiomes.network;

import exterminatorjeff.undergroundbiomes.config.UBConfig;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 *
 * @author LouisDB
 *
 */
public final class ConfigSync implements IMessage {

	private boolean vanillaLike;
	private boolean ubifyRecipes;
	private boolean ubifyOres;
	private byte regularStoneCrafting;
	private float hardnessModifier;
	private float resistanceModifier;
	private boolean buttonsOn;
	private byte buttonsTypes;
	private byte buttonsStyles;
	private boolean stairsOn;
	private byte stairsTypes;
	private byte stairsStyle;
	private boolean wallsOn;
	private byte wallsTypes;
	private byte wallsStyles;

	public ConfigSync() {
		// Required
	}

	public ConfigSync(UBConfig serverConfig) {
		vanillaLike = serverConfig.realistic();
		ubifyRecipes = serverConfig.ubifyRecipes();
		ubifyOres = serverConfig.ubifyOres();
		regularStoneCrafting = serverConfig.regularStoneCrafting.getValue().byteValue();
		hardnessModifier = serverConfig.hardnessModifier();
		resistanceModifier = serverConfig.resistanceModifier();
		buttonsOn = serverConfig.buttonsOn();
		buttonsTypes = serverConfig.buttonsTypes.getValue().byteValue();
		buttonsStyles = serverConfig.buttonsStyles.getValue().byteValue();
		stairsOn = serverConfig.stairsOn();
		stairsTypes = serverConfig.stairsTypes.getValue().byteValue();
		stairsStyle = serverConfig.stairsStyles.getValue().byteValue();
		wallsOn = serverConfig.wallsOn();
		wallsTypes = serverConfig.wallsTypes.getValue().byteValue();
		wallsStyles = serverConfig.wallsStyles.getValue().byteValue();

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		vanillaLike = buf.readBoolean();
		ubifyRecipes = buf.readBoolean();
		ubifyOres = buf.readBoolean();
		regularStoneCrafting = buf.readByte();
		hardnessModifier = buf.readFloat();
		resistanceModifier = buf.readFloat();
		buttonsOn = buf.readBoolean();
		buttonsTypes = buf.readByte();
		buttonsStyles = buf.readByte();
		stairsOn = buf.readBoolean();
		stairsTypes = buf.readByte();
		stairsStyle = buf.readByte();
		wallsOn = buf.readBoolean();
		wallsTypes = buf.readByte();
		wallsStyles = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(vanillaLike);
		buf.writeBoolean(ubifyRecipes);
		buf.writeBoolean(ubifyOres);
		buf.writeByte(regularStoneCrafting);
		buf.writeFloat(hardnessModifier);
		buf.writeFloat(resistanceModifier);
		buf.writeBoolean(buttonsOn);
		buf.writeByte(buttonsTypes);
		buf.writeByte(buttonsStyles);
		buf.writeBoolean(stairsOn);
		buf.writeByte(stairsTypes);
		buf.writeByte(stairsStyle);
		buf.writeBoolean(wallsOn);
		buf.writeByte(wallsTypes);
		buf.writeByte(wallsStyles);
	}

	public static final class ConfigSyncHandler implements IMessageHandler<ConfigSync, IMessage> {

		@Override
		public IMessage onMessage(ConfigSync message, MessageContext ctx) {
                    UBConfig affected = ((UBConfig)(UBConfig.SPECIFIC));
			affected.realistic.changeValue(message.vanillaLike);
			affected.ubifyRecipes.changeValue(message.ubifyRecipes);
			affected.ubifyOres.changeValue(message.ubifyOres);
			affected.regularStoneCrafting.changeValue((int) message.regularStoneCrafting);
			affected.hardnessModifier.changeValue(message.hardnessModifier);
			affected.resistanceModifier.changeValue(message.resistanceModifier);
			affected.buttonsOn.changeValue(message.buttonsOn);
			affected.buttonsTypes.changeValue((int) message.buttonsTypes);
			affected.buttonsStyles.changeValue((int) message.buttonsStyles);
			affected.stairsOn.changeValue(message.stairsOn);
			affected.stairsTypes.changeValue((int) message.stairsTypes);
			affected.stairsStyles.changeValue((int) message.stairsStyle);
			affected.wallsOn.changeValue(message.wallsOn);
			affected.wallsTypes.changeValue((int) message.wallsTypes);
			affected.wallsStyles.changeValue((int) message.wallsStyles);
			return null; // No response
		}

	}

}
