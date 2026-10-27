package fr.switchback.blocklings.inventories;

import fr.switchback.blocklings.util.helpers.ToolHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEquipment extends Slot {
    public SlotEquipment(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return ToolHelper.isTool(stack.getItem());
    }
}
