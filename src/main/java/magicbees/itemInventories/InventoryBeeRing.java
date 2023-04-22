package magicbees.itemInventories;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class InventoryBeeRing extends AbstractItemInventories {

    private static final String KEY_ITEMS = "Items"; // legacy
    private static final String KEY_SLOTS = "Slots";
    private static final String KEY_UID = "UID";
    private static final Random rand = new Random();

    public int currentBeeHealth;
    private final ItemStack parent;
    private final EntityPlayer player;

    public InventoryBeeRing(ItemStack parent, EntityPlayer player)
    {
        this.parent = parent;
        this.player = player;
        readFromNBT(parent.getTagCompound());

    }

    public boolean isParentItemInventory(ItemStack itemStack) {
        ItemStack parent = getParent();
        return isSameItemInventory(parent, itemStack);
    }

    protected ItemStack getParent() {
        ItemStack equipped = player.getCurrentEquippedItem();
        if (isSameItemInventory(equipped, parent)) {
            return equipped;
        }
        return parent;
    }

    private static boolean isSameItemInventory(ItemStack base, ItemStack comparison) {
        if (base == null || comparison == null) {
            return false;
        }

        if (base.getItem() != comparison.getItem()) {
            return false;
        }

        if (!base.hasTagCompound() || !comparison.hasTagCompound()) {
            return false;
        }

        String baseUID = base.getTagCompound().getString(KEY_UID);
        String comparisonUID = comparison.getTagCompound().getString(KEY_UID);
        return baseUID != null && comparisonUID != null && baseUID.equals(comparisonUID);
    }

    public void readFromNBT(NBTTagCompound nbt) {

        if (nbt == null) {
            return;
        }

        if (nbt.hasKey(KEY_SLOTS)) {
            NBTTagCompound nbtSlots = nbt.getCompoundTag(KEY_SLOTS);
            for (int i = 0; i < inventoryContent.length; i++) {
                String slotKey = getSlotNBTKey(i);
                if (nbtSlots.hasKey(slotKey)) {
                    NBTTagCompound itemNbt = nbtSlots.getCompoundTag(slotKey);
                    ItemStack itemStack = ItemStack.loadItemStackFromNBT(itemNbt);
                    inventoryContent[i] = itemStack;
                } else {
                    inventoryContent[i] = null;
                }
            }
        }
    }

    private void writeToParentNBT() {
        ItemStack parent = getParent();
        if (parent == null) {
            return;
        }

        NBTTagCompound nbt = parent.getTagCompound();
        NBTTagCompound slotsNbt = new NBTTagCompound();
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack itemStack = getStackInSlot(i);
            if (itemStack != null) {
                String slotKey = getSlotNBTKey(i);
                NBTTagCompound itemNbt = new NBTTagCompound();
                itemStack.writeToNBT(itemNbt);
                slotsNbt.setTag(slotKey, itemNbt);
            }
        }

        nbt.setTag(KEY_SLOTS, slotsNbt);
        nbt.removeTag(KEY_ITEMS);
    }

    private static String getSlotNBTKey(int i) {
        return Integer.toString(i, Character.MAX_RADIX);
    }

    @Override
    public void markDirty() {
        writeToParentNBT();
    }
}
