package fr.switchback.blocklings.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class FirstSpawnEventHandler {
    private static final String HAS_JOINED_BEFORE = "BlocklingsHasJoinedBefore";

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        NBTTagCompound playerEntityData = player.getEntityData();
        NBTTagCompound persistentTag = playerEntityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)
                ? playerEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
                : new NBTTagCompound();
        if (!persistentTag.getBoolean(HAS_JOINED_BEFORE)) {
            persistentTag.setBoolean(HAS_JOINED_BEFORE, true);
            ItemStack blocklingEgg = new ItemStack(Items.SPAWN_EGG);
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound entityTag = new NBTTagCompound();
            entityTag.setString("id", "blocklings:entity_blockling");
            tag.setTag("EntityTag", entityTag);
            blocklingEgg.setTagCompound(tag);
            if (!player.inventory.addItemStackToInventory(blocklingEgg)) {
                player.dropItem(blocklingEgg, false);
            }
        }
    }
}