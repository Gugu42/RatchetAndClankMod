package com.gugu42.rcmod.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class VendorSlot extends Slot {
    public VendorSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
}
