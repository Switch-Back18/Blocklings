package fr.switchback.blocklings.util.helpers;

import fr.switchback.blocklings.BlocklingsConfig;
import fr.switchback.blocklings.util.BlocklingType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemHelper {
    public static HashMap<IBlockState, ItemStack> saplings = new HashMap<>();
    private static List<ItemStack> upgradeMaterials = new ArrayList<ItemStack>();

    static {
        for (BlocklingType type : BlocklingType.blocklingTypes) {
            upgradeMaterials.addAll(Arrays.asList(type.upgradeMaterials));
        }
    }

    static {
        saplings.put(Blocks.LOG.getStateFromMeta(0), new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 0));
        saplings.put(Blocks.LOG.getStateFromMeta(1), new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 1));
        saplings.put(Blocks.LOG.getStateFromMeta(2), new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 2));
        saplings.put(Blocks.LOG.getStateFromMeta(3), new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 3));
        saplings.put(Blocks.LOG2.getStateFromMeta(0), new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 4));
        saplings.put(Blocks.LOG2.getStateFromMeta(1), new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 5));
    }

    public static boolean isFlower(Item item) {
        if (item == null || item == Items.AIR) {
            return false;
        }

        Block block = Block.getBlockFromItem(item);
        if (block instanceof BlockFlower || BlocklingsConfig.isItemInList(item, BlocklingsConfig.flowers)) {
            return true;
        }

        ItemStack stack = new ItemStack(item);
        int[] oreIDs = OreDictionary.getOreIDs(stack);
        for (int id : oreIDs) {
            String oreName = OreDictionary.getOreName(id);
            if (oreName != null && oreName.toLowerCase().contains("flower")) {
                return true;
            }
        }

        return false;
    }

    public static boolean isUpgradeMaterial(ItemStack itemStack) {
        for (ItemStack material : upgradeMaterials) {
            if (material.getItem() == itemStack.getItem() && material.getMetadata() == itemStack.getMetadata()) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack getSaplingForLog(IBlockState logState) {
        if (saplings.containsKey(logState)) {
            return saplings.get(logState).copy();
        }
        Block block = logState.getBlock();
        if (block instanceof BlockLog) {
            ItemStack logStack = new ItemStack(block, 1, block.getMetaFromState(logState));
            int[] oreIDs = OreDictionary.getOreIDs(logStack);
            for (int id : oreIDs) {
                String oreName = OreDictionary.getOreName(id);
                if (oreName != null && oreName.startsWith("log")) {
                    String woodSuffix = oreName.substring(3); // ex: "Maple"
                    ItemStack sapling = findSaplingBySuffix(woodSuffix);
                    if (!sapling.isEmpty()) {
                        return sapling;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findSaplingBySuffix(String suffix) {
        String[] possiblePrefixes = {"treeSapling", "sapling"};
        for (String prefix : possiblePrefixes) {
            String targetName = prefix + suffix;
            for (ItemStack ores : OreDictionary.getOres(targetName)) {
                if (!ores.isEmpty()) {
                    return ores.copy();
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
