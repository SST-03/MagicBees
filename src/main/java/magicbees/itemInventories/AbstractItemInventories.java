package magicbees.itemInventories;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class AbstractItemInventories implements IInventory {

    protected ItemStack[] inventoryContent;
    protected int stackLimit = 64;

    @Override
    public int getSizeInventory() {
        if (inventoryContent == null)
        {
            return 0;
        }
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        if (inventoryContent != null) {
            return inventoryContent[slotIn];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = null;

        if (getStackInSlot(slot) != null) {
            if (getStackInSlot(slot).stackSize <= count) {
                itemstack = getStackInSlot(slot);
                setInventorySlotContents(slot, null);
            } else {
                itemstack = getStackInSlot(slot).splitStack(count);

                if (getStackInSlot(slot).stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (inventoryContent != null && slot == 1)
        {
            inventoryContent[slot] = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                stack.stackSize = getInventoryStackLimit();
            }
        }

        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "BeeRing";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    @Override
    public abstract void markDirty();

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }
}
