package fr.switchback.blocklings;

import fr.switchback.blocklings.events.AttackEventHandler;
import fr.switchback.blocklings.events.BlocklingPickupHandler;
import fr.switchback.blocklings.events.BlocklingsConfigHandler;
import fr.switchback.blocklings.events.FirstSpawnEventHandler;
import fr.switchback.blocklings.guis.GuiHandler;
import fr.switchback.blocklings.items.BlocklingsItems;
import fr.switchback.blocklings.proxy.IProxy;
import fr.switchback.blocklings.util.helpers.EntityHelper;
import fr.switchback.blocklings.util.helpers.NetworkHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Blocklings.MODID, name = Blocklings.MODNAME, version = "1.0")
public class Blocklings {
    public static final String MODID = "blocklings";
    public static final String MODNAME = "Blocklings";

    @SidedProxy(clientSide = "fr.switchback.blocklings.proxy.ClientProxy", serverSide = "fr.switchback.blocklings.proxy.ServerProxy")
    public static IProxy proxy;

    @Mod.Instance
    public static Blocklings instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BlocklingsItems.init();
        EntityHelper.registerEntities();
        NetworkHelper.registerMessages();
        NetworkRegistry.INSTANCE.registerGuiHandler(Blocklings.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new AttackEventHandler());
        MinecraftForge.EVENT_BUS.register(new FirstSpawnEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlocklingsConfigHandler());
        MinecraftForge.EVENT_BUS.register(new BlocklingPickupHandler());
        ConfigManager.sync(Blocklings.MODID, Config.Type.INSTANCE);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
