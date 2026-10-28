package fr.switchback.blocklings.events;

import fr.switchback.blocklings.Blocklings;
import fr.switchback.blocklings.items.ItemBlockling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlocklingPickupHandler {
    private static final Map<UUID, Long> MESSAGE_COOLDOWNS = new HashMap<>();

    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        EntityItem entityItem = event.getItem();
        ItemStack stack = entityItem.getItem();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemBlockling) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt != null && nbt.hasKey("OwnerMost") && nbt.hasKey("OwnerLeast")) {
                UUID ownerUUID = nbt.getUniqueId("Owner");
                EntityPlayer player = event.getEntityPlayer();
                if (ownerUUID != null && !ownerUUID.equals(player.getUniqueID())) {
                    event.setCanceled(true);
                    if (!player.world.isRemote) {
                        long currentTime = player.world.getTotalWorldTime();
                        long lastMessageTime = MESSAGE_COOLDOWNS.getOrDefault(player.getUniqueID(), 0L);
                        if (currentTime - lastMessageTime > 40) {
                            player.sendMessage(new TextComponentString("This Blockling does not belong to you!"));
                            MESSAGE_COOLDOWNS.put(player.getUniqueID(), currentTime);
                        }
                    }
                }
            }
        }
    }
}