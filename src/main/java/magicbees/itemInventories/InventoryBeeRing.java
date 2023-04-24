package magicbees.itemInventories;

import baubles.common.lib.PlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;


public class InventoryBeeRing implements IInventory {

    public EntityPlayer player;
    public ItemStack parent;
    public ItemStack[] contents;

    public int currentBeeHealth;
    public int throttle;

    private final static String KEY_SLOTS = "Slots";
    private static final String KEY_UID = "UID";
    private static final Random rand = new Random();

    public InventoryBeeRing(ItemStack stack, EntityPlayer player)
    {
        parent = stack;
        this.player = player;
        this.contents = new ItemStack[2];

        readFromNBT(stack.getTagCompound());
    }

    public void readFromNBT(NBTTagCompound compound) {
        if (compound == null) {
            return;
        }

        if (compound.hasKey(KEY_SLOTS)) {
            NBTTagCompound nbtSlots = compound.getCompoundTag(KEY_SLOTS);
            for (int i = 0; i < contents.length; i++) {
                String slotKey = getSlotNBTKey(i);
                if (nbtSlots.hasKey(slotKey)) {
                    NBTTagCompound itemNbt = nbtSlots.getCompoundTag(slotKey);
                    ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemNbt);
                    contents[i] = itemStack;
                } else {
                    contents[i] = null;
                }
            }
        }
        if (compound.getInteger("throttle") != 0) {
            throttle = compound.getInteger("throttle");
        }

        if (compound.getInteger("currentBeeHealth") != 0)
        {
            currentBeeHealth = compound.getInteger("throttle");
        }

    }

    private static String getSlotNBTKey(int i) {
        return Integer.toString(i, Character.MAX_RADIX);
    }

    @Override
    public int getSizeInventory() {
        return contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return contents[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack stack = getStackInSlot(slot);
        if (stack == null) {
            return null;
        }

        if (stack.stackSize <= count) {
            setInventorySlotContents(slot, null);
            return stack;
        } else {
            ItemStack product = stack.splitStack(count);
            setInventorySlotContents(slot, stack);
            return product;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack toReturn = getStackInSlot(slot);

        if (toReturn != null) {
            setInventorySlotContents(slot, null);
        }

        return toReturn;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (stack != null && stack.stackSize == 0) {
            stack = null;
        }

        contents[slot] = stack;

        // I have literally no idea, but for some reason when this is 'this.parent' it doesn't work...

        ItemStack parent = player.getCurrentEquippedItem();

        NBTTagCompound nbt = parent.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            parent.setTagCompound(nbt);
        }

        NBTTagCompound slotNbt;
        if (!nbt.hasKey("KEY_SLOTS")) {
            slotNbt = new NBTTagCompound();
            nbt.setTag(KEY_SLOTS, slotNbt);
        } else {
            slotNbt = nbt.getCompoundTag(KEY_SLOTS);
        }

        String slotKey = getSlotNBTKey(slot);

        if (stack == null) {
            slotNbt.removeTag(slotKey);
        } else {
            NBTTagCompound itemNbt = new NBTTagCompound();
            stack.writeToNBT(itemNbt);

            slotNbt.setTag(slotKey, itemNbt);
        }
    }

    @Override
    public String getInventoryName() {
        return "beeRingInventory";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

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
