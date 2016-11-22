package exterminatorjeff.undergroundbiomes.network;

import exterminatorjeff.undergroundbiomes.api.ModInfo;
import exterminatorjeff.undergroundbiomes.config.UBConfig;
import exterminatorjeff.undergroundbiomes.core.UndergroundBiomes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author LouisDB
 *
 */
@SideOnly(Side.SERVER)
public enum DedicatedServer {
	INSTANCE;

	private final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
	private int id = 0;

	private DedicatedServer() {
		NETWORK.registerMessage(ConfigSync.ConfigSyncHandler.class, ConfigSync.class, id++, Side.CLIENT);
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		NETWORK.sendTo(new ConfigSync(((UBConfig)(UBConfig.SPECIFIC))), player);
	}

}
