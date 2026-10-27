package fr.switchback.blocklings.events;

import fr.switchback.blocklings.Blocklings;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlocklingsConfigHandler {

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Blocklings.MODID)) {
            ConfigManager.sync(Blocklings.MODID, Config.Type.INSTANCE);
        }
    }
}