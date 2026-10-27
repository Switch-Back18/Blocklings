package fr.switchback.blocklings.util.helpers;

import fr.switchback.blocklings.util.CropStruct;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper {
    private static List<Block> ores = new ArrayList<>();
    private static List<Block> logs = new ArrayList<>();
    private static List<Block> leaves = new ArrayList<>();
    private static List<Block> dirt = new ArrayList<>();
    private static List<CropStruct> crops = new ArrayList<>();

    static {
        ores.add(Blocks.QUARTZ_ORE);
        ores.add(Blocks.COAL_ORE);
        ores.add(Blocks.IRON_ORE);
        ores.add(Blocks.GOLD_ORE);
        ores.add(Blocks.LAPIS_ORE);
        ores.add(Blocks.REDSTONE_ORE);
        ores.add(Blocks.LIT_REDSTONE_ORE);
        ores.add(Blocks.EMERALD_ORE);
        ores.add(Blocks.DIAMOND_ORE);

        logs.add(Blocks.LOG);
        logs.add(Blocks.LOG2);

        leaves.add(Blocks.LEAVES);
        leaves.add(Blocks.LEAVES2);

        dirt.add(Blocks.DIRT);
        dirt.add(Blocks.GRASS);

        crops.add(new CropStruct(Blocks.WHEAT, Items.WHEAT_SEEDS, 7));
        crops.add(new CropStruct(Blocks.POTATOES, Items.POTATO, 7));
        crops.add(new CropStruct(Blocks.CARROTS, Items.CARROT, 7));
        crops.add(new CropStruct(Blocks.BEETROOTS, Items.BEETROOT_SEEDS, 3));
        crops.add(new CropStruct(Blocks.PUMPKIN, Items.AIR, -1));
        crops.add(new CropStruct(Blocks.MELON_BLOCK, Items.AIR, -1));
    }

    public static boolean isOre(Block block) {
        if (block == null || block == Blocks.AIR) return false;
        if (ores.contains(block) || block instanceof BlockOre) return true;
        ItemStack stack = new ItemStack(block);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.startsWith("ore")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static double getOreValue(Block block) {
        int index = ores.indexOf(block);
        if (index != -1) return index;
        return 5.0;
    }

    public static boolean isLog(Block block) {
        if (block == null || block == Blocks.AIR) return false;
        if (logs.contains(block) || block instanceof BlockLog) return true;
        ItemStack stack = new ItemStack(block);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && name.startsWith("log")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLeaf(Block block) {
        if (block == null || block == Blocks.AIR) return false;
        if (leaves.contains(block) || block instanceof BlockLeaves) return true;
        ItemStack stack = new ItemStack(block);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && (name.contains("leaf") || name.contains("leaves"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDirt(Block block) {
        if (block == null || block == Blocks.AIR) return false;
        if (dirt.contains(block)) return true;
        ItemStack stack = new ItemStack(block);
        if (!stack.isEmpty()) {
            for (int id : OreDictionary.getOreIDs(stack)) {
                String name = OreDictionary.getOreName(id);
                if (name != null && (name.equals("dirt") || name.equals("grass"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCrop(Block block) {
        if (block == null || block == Blocks.AIR) return false;
        for (CropStruct cropStruct : crops) {
            if (cropStruct.crop == block) {
                return true;
            }
        }
        return block instanceof BlockCrops || (block instanceof BlockBush && !(block instanceof BlockFlower) && !(block instanceof BlockMushroom));
    }

    public static Item getSeed(Block block) {
        for (CropStruct cropStruct : crops) {
            if (cropStruct.crop == block) {
                return cropStruct.seed;
            }
        }
        Item itemFromBlock = Item.getItemFromBlock(block);
        if (itemFromBlock != null && itemFromBlock != Items.AIR) {
            return itemFromBlock;
        }
        return Items.AIR;
    }

    public static boolean isGrown(IBlockState blockState) {
        Block block = blockState.getBlock();
        int grownAge = getGrownAge(block);

        if (grownAge != -1) {
            int age = BlockHelper.getAge(blockState);
            if (age < grownAge) {
                return false;
            }
        }

        return true;
    }

    public static int getGrownAge(Block block) {
        for (CropStruct cropStruct : crops) {
            if (cropStruct.crop == block) {
                return cropStruct.age;
            }
        }
        if (block instanceof BlockCrops) {
            return 7;
        }

        return 0;
    }

    public static int getAge(IBlockState blockState) {
        Block block = blockState.getBlock();

        if (block == Blocks.BEETROOTS) {
            return blockState.getValue(BlockBeetroot.BEETROOT_AGE);
        }
        if (block instanceof BlockCrops) {
            try {
                return blockState.getValue(BlockCrops.AGE);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
}