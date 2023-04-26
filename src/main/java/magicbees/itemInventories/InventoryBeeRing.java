package magicbees.itemInventories;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.IAlleleBeeEffect;
import forestry.api.apiculture.IBee;
import forestry.api.genetics.IEffectData;

public class InventoryBeeRing implements IInventory {

    public EntityPlayer player;
    public ItemStack parent;
    public ItemStack[] contents;
    public int itemLocatedSlot;
    public boolean baubleFlag;
    public IEffectData[] effectData = new IEffectData[2];

    public int currentBeeHealth;
    public int currentBeeColour;
    public int throttle;

    private final static String KEY_SLOTS = "Slots";
    private final static String KEY_HEALTH = "Health";
    private final static String KEY_THROTTLE = "Throttle";

    private final static String KEY_EFFECT_1 = "Effect1";
    private final static String KEY_EFFECT_2 = "Effect2";
    private final static String KEY_COLOUR = "Colour";

    public InventoryBeeRing(ItemStack stack, EntityPlayer player, int slot, boolean baubleFlag) {
        this.parent = stack;
        this.player = player;
        this.contents = new ItemStack[2];
        this.itemLocatedSlot = slot;
        this.baubleFlag = baubleFlag;

        readFromNBT(stack.getTagCompound());
    }

    public void readFromNBT(NBTTagCompound compound) {
        if (compound == null) {
            return;
        }

        // Initializing slot contents
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

        // Initializing Throttle for logic and Health, Colour for GUI effects
        if (compound.getInteger(KEY_THROTTLE) != 0) {
            throttle = compound.getInteger(KEY_THROTTLE);
        }

        if (compound.getInteger(KEY_HEALTH) != 0) {
            currentBeeHealth = compound.getInteger(KEY_HEALTH);
        }

        if (compound.getInteger(KEY_COLOUR) != 0) {
            currentBeeColour = compound.getInteger(KEY_COLOUR);
        }
    }

    // Helper method for NBT Keys
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

    // sets the internal inventory of this class to the provided item stack and writes that to the NBT of the parent
    // (the ring)
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (stack != null && stack.stackSize == 0) {
            stack = null;
        }

        contents[slot] = stack;

        ItemStack parent = this.parent;

        NBTTagCompound nbt = parent.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            parent.setTagCompound(nbt);
        }

        NBTTagCompound slotNbt;
        if (!nbt.hasKey(KEY_SLOTS)) {
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

        setInventorySlot(parent);
    }

    // private method to manually set the inventory of a player
    private void setInventorySlot(ItemStack parent) {

        if (this.player.getHealth() == 0) {
            return;
        }

        if (baubleFlag) {
            InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(this.player);
            baubles.setInventorySlotContents(itemLocatedSlot, parent);
        } else {
            player.inventory.setInventorySlotContents(itemLocatedSlot, parent);
        }
    }

    // Writes the effectData field to NBT in a readable way (This and the next method could probably be condensed, but
    // I feel that this is the most readable form I could come up with.
    public void writeEffectNBT() {
        NBTTagCompound compound = parent.getTagCompound();

        if (compound == null) {
            compound = new NBTTagCompound();
            parent.setTagCompound(compound);
        }

        NBTTagCompound slotNbt1;
        if (!compound.hasKey(KEY_EFFECT_1)) {
            slotNbt1 = new NBTTagCompound();
            compound.setTag(KEY_EFFECT_1, slotNbt1);
        } else {
            slotNbt1 = compound.getCompoundTag(KEY_EFFECT_1);
        }

        NBTTagCompound slotNbt2;
        if (!compound.hasKey(KEY_EFFECT_2)) {
            slotNbt2 = new NBTTagCompound();
            compound.setTag(KEY_EFFECT_2, slotNbt2);
        } else {
            slotNbt2 = compound.getCompoundTag(KEY_EFFECT_2);
        }

        for (int i = 0; i < 5; i++) {
            String slotKey = getSlotNBTKey(i);
            try {
                slotNbt1.setInteger(slotKey + "i", effectData[0].getInteger(i));
                slotNbt1.setBoolean(slotKey + "b", effectData[0].getBoolean(i));
                slotNbt1.setFloat(slotKey + "f", effectData[0].getFloat(i));
            } catch (Exception ignored) {}
        }

        for (int i = 0; i < 5; i++) {
            String slotKey = getSlotNBTKey(i);
            try {
                slotNbt2.setInteger(slotKey + "i", effectData[1].getInteger(i));
                slotNbt2.setBoolean(slotKey + "b", effectData[1].getBoolean(i));
                slotNbt2.setFloat(slotKey + "f", effectData[1].getFloat(i));
            } catch (Exception ignored) {}
        }

        setInventorySlot(parent);
    }

    // Creates the correct implementation of IEffectData and pulls the data saved in the NBT into the effectData field
    public void setEffectAndInitialize(IBee queen) {
        effectData[0] = queen.getGenome().getEffect().validateStorage(effectData[0]);
        effectData[1] = ((IAlleleBeeEffect) queen.getGenome().getInactiveAllele(EnumBeeChromosome.EFFECT))
                .validateStorage(effectData[1]);

        NBTTagCompound compound = parent.getTagCompound();

        if (compound.hasKey(KEY_EFFECT_1)) {
            NBTTagCompound nbtSlots1 = compound.getCompoundTag(KEY_EFFECT_1);

            for (int i = 0; i < 5; i++) {
                String slotKey = getSlotNBTKey(i);

                if (nbtSlots1.hasKey(slotKey + "i")) {
                    int effectNBTInt = nbtSlots1.getInteger(slotKey + "i");
                    try {
                        effectData[0].setInteger(i, effectNBTInt);
                    } catch (Exception ignored) {}
                }
                if (nbtSlots1.hasKey(slotKey + "b")) {
                    boolean effectNBTBool = nbtSlots1.getBoolean(slotKey + "b");
                    try {
                        effectData[0].setBoolean(i, effectNBTBool);
                    } catch (Exception ignored) {}
                }
                if (nbtSlots1.hasKey(slotKey + "f")) {
                    float effectNBTFloat = nbtSlots1.getFloat(slotKey + "f");
                    try {
                        effectData[0].setFloat(i, effectNBTFloat);
                    } catch (Exception ignored) {}
                }
            }
        }

        if (compound.hasKey(KEY_EFFECT_2)) {
            NBTTagCompound nbtSlots2 = compound.getCompoundTag(KEY_EFFECT_2);

            for (int i = 0; i < 5; i++) {
                String slotKey = getSlotNBTKey(i);

                if (nbtSlots2.hasKey(slotKey + "i")) {
                    int effectNBTInt = nbtSlots2.getInteger(slotKey + "i");
                    try {
                        effectData[1].setInteger(i, effectNBTInt);
                    } catch (Exception ignored) {}
                }
                if (nbtSlots2.hasKey(slotKey + "b")) {
                    boolean effectNBTBool = nbtSlots2.getBoolean(slotKey + "b");
                    try {
                        effectData[1].setBoolean(i, effectNBTBool);
                    } catch (Exception ignored) {}
                }
                if (nbtSlots2.hasKey(slotKey + "f")) {
                    float effectNBTFloat = nbtSlots2.getFloat(slotKey + "f");
                    try {
                        effectData[1].setFloat(i, effectNBTFloat);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    // Setters for logic and GUI display info
    public void setThrottle(int throttle) {
        this.throttle = throttle;
        this.parent.getTagCompound().setInteger(KEY_THROTTLE, this.throttle);
        setInventorySlot(parent);
    }

    public void setCurrentBeeHealth(int health) {
        this.currentBeeHealth = health;
        this.parent.getTagCompound().setInteger(KEY_HEALTH, this.currentBeeHealth);
        setInventorySlot(parent);
    }

    public void setCurrentBeeColour(int colour) {
        this.currentBeeColour = colour;
        this.parent.getTagCompound().setInteger(KEY_COLOUR, this.currentBeeColour);
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
    public void markDirty() {}

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
