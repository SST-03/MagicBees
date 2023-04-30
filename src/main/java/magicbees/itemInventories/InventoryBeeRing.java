package magicbees.itemInventories;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import forestry.core.inventory.ItemInventory;

public class InventoryBeeRing extends ItemInventory {

    private static final String KEY_SLOT_INDEX = "SlotIndex";
    private static final String KEY_HEALTH = "Health";
    private static final String KEY_THROTTLE = "Throttle";
    private static final String KEY_COLOUR = "Colour";

    private static final int DRONE_SLOT = 0;
    private static final int QUEEN_SLOT = 1;

    private int ringSlotIndex;
    private int currentBeeHealth;
    private int currentBeeColour;
    private int throttle;

    public InventoryBeeRing(ItemStack stack, EntityPlayer player) {
        super(player, 2, stack);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound == null) {
            compound = new NBTTagCompound();
            getParent().setTagCompound(compound);
        }

        super.readFromNBT(compound);
        if (compound.hasKey(KEY_SLOT_INDEX)) {
            this.ringSlotIndex = compound.getInteger(KEY_SLOT_INDEX);
        }
        if (compound.hasKey(KEY_HEALTH)) {
            this.currentBeeHealth = compound.getInteger(KEY_HEALTH);
        }
        if (compound.hasKey(KEY_COLOUR)) {
            this.currentBeeColour = compound.getInteger(KEY_COLOUR);
        }
        if (compound.hasKey(KEY_THROTTLE)) {
            this.throttle = compound.getInteger(KEY_THROTTLE);
        }
    }

    public boolean hasDrone() {
        return this.getStackInSlot(DRONE_SLOT) != null;
    }

    public ItemStack getDrone() {
        return this.getStackInSlot(DRONE_SLOT);
    }

    public void setDrone(ItemStack droneStack) {
        this.setInventorySlotContents(DRONE_SLOT, droneStack);
    }

    public boolean hasQueen() {
        return this.getStackInSlot(QUEEN_SLOT) != null;
    }

    public ItemStack getQueen() {
        return this.getStackInSlot(QUEEN_SLOT);
    }

    public void setQueen(ItemStack queenStack) {
        this.setInventorySlotContents(QUEEN_SLOT, queenStack);
    }

    public int getRingSlotIndex() {
        return this.ringSlotIndex;
    }

    public void setRingSlotIndex(int ringSlotIndex) {
        this.ringSlotIndex = ringSlotIndex;
        getParent().getTagCompound().setInteger(KEY_SLOT_INDEX, this.ringSlotIndex);
    }

    public int getCurrentBeeHealth() {
        return this.currentBeeHealth;
    }

    public void setCurrentBeeHealth(int health) {
        this.currentBeeHealth = health;
        getParent().getTagCompound().setInteger(KEY_HEALTH, this.currentBeeHealth);
    }

    public int getCurrentBeeColour() {
        return this.currentBeeColour;
    }

    public void setCurrentBeeColour(int colour) {
        this.currentBeeColour = colour;
        getParent().getTagCompound().setInteger(KEY_COLOUR, this.currentBeeColour);
    }

    public int getThrottle() {
        return this.throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
        getParent().getTagCompound().setInteger(KEY_THROTTLE, this.throttle);
    }

    @Override
    public String getInventoryName() {
        return "beeRingInventory";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
}
