package fr.switchback.blocklings.items;

import fr.switchback.blocklings.entities.EntityBlockling;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
    public boolean hasEffect(ItemStack stack) {
        NBTTagCompound stackTag = stack.getTagCompound();
        if(stackTag != null) {
            return !stackTag.getString(BLOCKLING_NAME_KEY).isEmpty();
        }
        return super.hasEffect(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
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
                            blockling.setLocationAndAngles(player.posX, player.posY, player.posZ, player.cameraYaw, player.cameraPitch);
                            stack.damageItem(1, player);
                            serverWorld.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.5f);
                        }
                    }
                }
            }
        }
        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        super.onUsingTick(stack, player, count);
        if (stack.hasTagCompound() && stack.getTagCompound().getUniqueId(BLOCKLING_UUID_KEY) != null) {
            if (player.world.isRemote) {
                EntityBlockling blockling = findBlockling(stack, player.world);
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
    private EntityBlockling findBlockling(@Nonnull ItemStack stack, @Nonnull World world) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            String mostKey = BLOCKLING_UUID_KEY + "Most";
            String leastKey = BLOCKLING_UUID_KEY + "Least";
            if (nbt.hasKey(mostKey) && nbt.hasKey(leastKey)) {
                long most = nbt.getLong(mostKey);
                long least = nbt.getLong(leastKey);
                UUID uuid = new UUID(most, least);
                for (Entity entity : world.loadedEntityList) {
                    if (entity.getUniqueID().equals(uuid)) {
                        if (entity instanceof EntityBlockling) {
                            return (EntityBlockling) entity;
                        }
                    }
                }
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    private String findBlocklingName(@Nonnull ItemStack stack, @Nonnull WorldClient world) {
        EntityBlockling blockling = findBlockling(stack, world);
        if (blockling != null) {
            String customName = blockling.getCustomNameTag();
            return customName;
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
        if (stack.hasTagCompound()) {
            if (!stack.getTagCompound().getString(BLOCKLING_NAME_KEY).isEmpty()) {
                return TextFormatting.LIGHT_PURPLE + stack.getTagCompound().getString(BLOCKLING_NAME_KEY) + " Whistle";
            }
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (world != null) {
            String location = findBlocklingLocation(stack, (WorldClient) world);
            if (location != null) {
                String translatedKey = net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedName() + ".location");
                tooltip.add(TextFormatting.GREEN + translatedKey + location);
            }
        }
        super.addInformation(stack, world, tooltip, flag);
    }
}