package fr.switchback.blocklings.util.helpers;

import fr.switchback.blocklings.BlocklingsConfig;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolHelper {
    public static List<Item> weapons = new ArrayList<>();
    public static List<Item> pickaxes = new ArrayList<>();
    public static List<Item> axes = new ArrayList<>();
    public static List<Item> hoes = new ArrayList<>();

    static {
        weapons.add(Items.WOODEN_SWORD);
        weapons.add(Items.STONE_SWORD);
        weapons.add(Items.GOLDEN_SWORD);
        weapons.add(Items.IRON_SWORD);
        weapons.add(Items.DIAMOND_SWORD);

        pickaxes.add(Items.WOODEN_PICKAXE);
        pickaxes.add(Items.STONE_PICKAXE);
        pickaxes.add(Items.GOLDEN_PICKAXE);
        pickaxes.add(Items.IRON_PICKAXE);
        pickaxes.add(Items.DIAMOND_PICKAXE);

        axes.add(Items.WOODEN_AXE);
        axes.add(Items.STONE_AXE);
        axes.add(Items.GOLDEN_AXE);
        axes.add(Items.IRON_AXE);
        axes.add(Items.DIAMOND_AXE);

        hoes.add(Items.WOODEN_HOE);
        hoes.add(Items.STONE_HOE);
        hoes.add(Items.GOLDEN_HOE);
        hoes.add(Items.IRON_HOE);
        hoes.add(Items.DIAMOND_HOE);
    }

    public static boolean isTool(Item item) {
        if (item == null) return false;
        return isWeapon(item) || isPickaxe(item) || isAxe(item) || isHoe(item) || item instanceof ItemTool;
    }

    public static boolean isWeapon(Item item) {
        if (item == null || item == Items.AIR) return false;
        if (weapons.contains(item) || item instanceof ItemSword || BlocklingsConfig.isItemInList(item, BlocklingsConfig.swords))
            return true;
        ItemStack stack = new ItemStack(item);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.contains("sword")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPickaxe(Item item) {
        if (item == null || item == Items.AIR) return false;
        if (pickaxes.contains(item) || item instanceof ItemPickaxe || BlocklingsConfig.isItemInList(item, BlocklingsConfig.pickaxes))
            return true;
        ItemStack stack = new ItemStack(item);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.contains("pickaxe")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAxe(Item item) {
        if (item == null || item == Items.AIR) return false;
        if (axes.contains(item) || item instanceof ItemAxe || BlocklingsConfig.isItemInList(item, BlocklingsConfig.axes)) return true;
        ItemStack stack = new ItemStack(item);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.contains("axe")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHoe(Item item) {
        if (item == null || item == Items.AIR) return false;
        if (hoes.contains(item) || item instanceof ItemHoe || BlocklingsConfig.isItemInList(item, BlocklingsConfig.hoes)) return true;
        ItemStack stack = new ItemStack(item);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.contains("hoe")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getWeaponLevel(Item item) {
        int vanillaIndex = weapons.indexOf(item);
        if (vanillaIndex != -1) return vanillaIndex;

        if (item instanceof ItemSword) {
            float damage = ((ItemSword) item).getAttackDamage();
            if (damage > 6.0f) return 4;
            if (damage > 5.0f) return 3;
            if (damage > 4.0f) return 1;
            return 2;
        }
        return 0;
    }

    public static int getPickaxeLevel(Item item) {
        int vanillaIndex = pickaxes.indexOf(item);
        if (vanillaIndex != -1) return vanillaIndex;

        if (item instanceof ItemPickaxe) {
            return 3;
        }
        return 0;
    }

    public static int getAxeLevel(Item item) {
        int vanillaIndex = axes.indexOf(item);
        if (vanillaIndex != -1) return vanillaIndex;
        return item instanceof ItemAxe ? 2 : 0;
    }

    public static int getHoeLevel(Item item) {
        int vanillaIndex = hoes.indexOf(item);
        if (vanillaIndex != -1) return vanillaIndex;
        return item instanceof ItemHoe ? 1 : 0;
    }

    public static float getToolAttackDamage(ItemStack stack) {
        float damage = 0;
        if (!stack.isEmpty()) {
            Object[] array = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get("generic.attackDamage").toArray();
            if (array.length > 0) {
                damage = (float) ((AttributeModifier) (array[0])).getAmount() + 1.0f;
            }
        }
        return damage;
    }

    public static float getToolAttackSpeed(ItemStack stack) {
        float speed = 1000.0f;

        if (!stack.isEmpty()) {
            Object[] array = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get("generic.attackSpeed").toArray();
            if (array.length > 0) {
                speed = (float) Math.abs(((AttributeModifier) (array[0])).getAmount()) + 1f;
            }
        }
        return 1.0f / speed;
    }

    public static float getToolAttackPower(ItemStack stack) {
        return (float) Math.log(getToolAttackDamage(stack)) * getToolAttackSpeed(stack);
    }

    public static List<Enchantment> getEnchantmentsFromTool(ItemStack toolStack) {
        List<Enchantment> enchantmentList = new ArrayList<Enchantment>();

        if (!toolStack.isEmpty()) {
            NBTTagList enchantments = toolStack.getEnchantmentTagList();
            for (int e = 0; e < enchantments.tagCount(); e++) {
                NBTTagCompound enchantmentTag = enchantments.getCompoundTagAt(e);
                ToolHelper.Enchantment enchantment = ToolHelper.Enchantment.getEnchantmentFromID(enchantmentTag.getId());
                if (enchantment != null) {
                    enchantmentList.add(enchantment);
                }
            }
        }
        return enchantmentList;
    }

    public static List<NBTTagCompound> getEnchantmentTagsFromTool(ItemStack toolStack) {
        List<NBTTagCompound> enchantmentList = new ArrayList<NBTTagCompound>();

        if (!toolStack.isEmpty()) {
            NBTTagList enchantments = toolStack.getEnchantmentTagList();
            for (int e = 0; e < enchantments.tagCount(); e++) {
                enchantmentList.add(enchantments.getCompoundTagAt(e));
            }
        }
        return enchantmentList;
    }

    public enum Enchantment {
        FORTUNE(35),
        UNBREAKING(34),
        SILKTOUCH(33),
        EFFICIENCY(32),
        SHARPNESS(16),
        SMITE(17),
        BANEOFARTHROPODS(18),
        KNOCKBACK(19),
        FIREASPECT(20),
        LOOTING(21);

        private int id;

        Enchantment(int id) {
            this.id = id;
        }

        public static Enchantment getEnchantmentFromID(int id) {
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.id == id) {
                    return enchantment;
                }
            }
            return null;
        }

        public static Enchantment getEnchantmentFromTag(NBTTagCompound tag) {
            return getEnchantmentFromID(tag.getInteger("id"));
        }
    }
}