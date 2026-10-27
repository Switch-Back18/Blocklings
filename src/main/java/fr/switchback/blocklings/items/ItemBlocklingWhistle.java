package fr.switchback.blocklings.items;

import fr.switchback.blocklings.Blocklings;
import fr.switchback.blocklings.entities.EntityBlockling;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ItemBlocklingWhistle extends Item {

    @Nonnull
    public static final String BLOCKLING_UUID_KEY = "blockling_uuid";

    @Nonnull
    public static final String BLOCKLING_NAME_KEY = "blockling_name";

    @Nonnull
    public static final Map<EntityBlockling, Set<ItemStack>> BLOCKLINGS_TO_WHISTLES = new HashMap<>();

    public ItemBlocklingWhistle(String name) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxStackSize(1);
        setMaxDamage(250);
        setCreativeTab(CreativeTabs.MISC);
    }

    public static void setBlockling(@Nonnull ItemStack stack, @Nonnull EntityBlockling blockling) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setUniqueId(BLOCKLING_UUID_KEY, blockling.getUniqueID());
        tag.setString(BLOCKLING_NAME_KEY, blockling.getName());
        stack.setTagCompound(tag);
        addStackToMap(blockling, stack);
    }

    public static void addStackToMap(@Nonnull EntityBlockling blockling, @Nonnull ItemStack stack) {
        Set<ItemStack> stacks = BLOCKLINGS_TO_WHISTLES.getOrDefault(blockling, new HashSet<>());
        stacks.add(stack);
        BLOCKLINGS_TO_WHISTLES.put(blockling, stacks);
    }

    public static void onBlocklingDestroyed(@Nonnull EntityBlockling blockling) {
        Set<ItemStack> stacks = BLOCKLINGS_TO_WHISTLES.get(blockling);
        if (stacks != null) {
            for (ItemStack stack : stacks) {
                NBTTagCompound stackTag = stack.getTagCompound();
                if (stackTag != null) {
                    stackTag.removeTag(BLOCKLING_UUID_KEY);
                    stackTag.removeTag(BLOCKLING_NAME_KEY);
                }
            }
        }
        BLOCKLINGS_TO_WHISTLES.remove(blockling);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (world instanceof WorldServer) {
            WorldServer serverWorld = (WorldServer) world;
            if (stack.hasTagCompound()) {
                NBTTagCompound stackTag = stack.getTagCompound();
                if (stackTag.getUniqueId(BLOCKLING_UUID_KEY) != null) {
                    UUID blocklingId = stackTag.getUniqueId(BLOCKLING_UUID_KEY);
                    Entity entity = serverWorld.getEntityFromUuid(blocklingId);
                    if (entity instanceof EntityBlockling) {
                        EntityBlockling blockling = (EntityBlockling) entity;
                        if (player == blockling.getOwner()) {
                            blockling.attemptTeleport(player.posX, player.posY, player.posZ);
                            stack.damageItem(1, player);
                            serverWorld.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.5f);
                        }
                    }
                }
            }
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        super.onUsingTick(stack, player, count);
        if (stack.hasTagCompound() && stack.getTagCompound().getUniqueId(BLOCKLING_UUID_KEY) != null) {
            if (player.world.isRemote) {
                EntityBlockling blockling = findBlockling(stack, (WorldClient) player.world);
                if (blockling != null) {
                    addStackToMap(blockling, stack);
                }
                String name = findBlocklingName(stack, (WorldClient) player.world);
                if (name != null) {
                    stack.getTagCompound().setString(BLOCKLING_NAME_KEY, name);
                }
            } else {
                EntityBlockling blockling = (EntityBlockling) ((WorldServer) player.world).getEntityFromUuid(stack.getTagCompound().getUniqueId(BLOCKLING_UUID_KEY));
                if (blockling != null) {
                    addStackToMap(blockling, stack);
                    stack.getTagCompound().setString(BLOCKLING_NAME_KEY, blockling.getCustomNameTag());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    private EntityBlockling findBlockling(@Nonnull ItemStack stack, @Nonnull WorldClient world) {
        if (stack.hasTagCompound() && stack.getTagCompound().getUniqueId(BLOCKLING_UUID_KEY) != null) {
            Int2ObjectMap<Entity> entitiesById = ObfuscationReflectionHelper.getPrivateValue(WorldClient.class, world, "field_217429_b");
            Entity blockling = entitiesById.values().stream().filter(e -> e.getUniqueID().equals(stack.getTagCompound().getUniqueId(BLOCKLING_UUID_KEY))).findFirst().orElse(null);
            return (EntityBlockling) blockling;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    private String findBlocklingName(@Nonnull ItemStack stack, @Nonnull WorldClient world) {
        EntityBlockling blockling = findBlockling(stack, world);
        if (blockling != null) {
            String customName = blockling.getCustomNameTag();
            if (customName != null) {
                return customName;
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    private String findBlocklingLocation(@Nonnull ItemStack stack, @Nonnull WorldClient world) {
        EntityBlockling blockling = findBlockling(stack, world);
        if (blockling != null) {
            return String.format("%d %d %d", (int) blockling.posX, (int) blockling.posY, (int) blockling.posZ);
        }
        return null;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().getString(BLOCKLING_NAME_KEY) != null) {
            return TextFormatting.LIGHT_PURPLE + "(" + stack.getTagCompound().getString(BLOCKLING_NAME_KEY) + ") Whistle";
        }
        return super.getItemStackDisplayName(stack);
    }
}