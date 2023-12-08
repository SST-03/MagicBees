package magicbees.tileentity;

import java.util.Objects;

import magicbees.main.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeHousing;
import magicbees.bees.BeeManager;
import magicbees.bees.BeeSpecies;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileEntityPhialingCabinet extends TileEntity implements IAspectContainer, IEssentiaTransport {

    public static final String tileEntityName = "Phialing Cabinet";

    // added directly from the crucible of souls
    public AspectList myAspects = new AspectList();
    public Aspect aspect;
    public int amount = 0;
    public int maxAmount = 64;
    public int increment = 0;

    @Override
    public void updateEntity() {
        // If there's no stored aspect we shouldn't even check for the above block.
        if (aspect == null) return;

        if (increment % Config.thaumcraftEssentiaBeePhialingCabinetTimeBetween == 0) {
            try {
                TileEntity above = worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
                if (IBeeHousing.class.isAssignableFrom(above.getClass())) {
                    IBeeHousing heeHousing = (IBeeHousing) above;
                    ItemStack queenStack = heeHousing.getBeeInventory().getQueen();

                    // If the bee isn't a queen then what are we even doing?
                    if (!BeeManager.beeRoot.isMated(queenStack)) return;

                    IBee queen = BeeManager.beeRoot.getMember(queenStack);
                    String queenUID = queen.getGenome().getPrimary().getUID();

                    if (Objects.equals(queenUID, BeeSpecies.TC_ESSENTIA.getSpecies().getUID())) {
                        this.myAspects.add(aspect, Config.thaumcraftEssentiaBeePhialingCabinetAmount);
                        increment = 0;
                    }
                }
            } catch (Exception ignored) {}
        }

        increment++;
    }

    // added directly from the crucible of souls
    @Override
    public AspectList getAspects() {
        // TODO Auto-generated method stub
        return this.myAspects;
    }

    // added directly from the crucible of souls
    @Override
    public void setAspects(AspectList aspects) {
        this.myAspects = aspects;
    }

    // added directly from the crucible of souls
    @Override
    public boolean doesContainerAccept(Aspect tag) {
        return false;
    }

    // added directly from the crucible of souls
    @Override
    public int addToContainer(Aspect tag, int am) {
        if (am == 0) {
            return am;
        } else {
            if (this.amount < this.maxAmount && tag == this.aspect || this.amount == 0) {
                this.aspect = tag;
                int added = Math.min(am, this.maxAmount - this.amount);
                this.amount += added;
                am -= added;
            }
            return am;
        }
    }

    // added directly from the crucible of souls
    @Override
    public boolean takeFromContainer(Aspect tag, int amount) {
        if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        if (this.myAspects.getAmount(tag) >= amount) {
            this.myAspects.reduce(tag, amount);
            return true;
        } else {
            return false;
        }
    }

    // added directly from the crucible of souls
    @Override
    public boolean takeFromContainer(AspectList ot) {
        // TODO Auto-generated method stub
        boolean hasIt = true;
        if (!this.worldObj.isRemote) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        ot.aspects.keySet().iterator();
        for (Aspect next : ot.aspects.keySet()) {
            if (this.myAspects.getAmount(next) < ot.getAmount(next)) hasIt = false;
        }
        if (hasIt) {
            for (Aspect next : ot.aspects.keySet()) {
                myAspects.reduce(next, ot.getAmount(next));
            }
            return true;
        } else {
            return false;
        }
    }

    // added directly from the crucible of souls
    @Override
    public boolean doesContainerContainAmount(Aspect tag, int amount) {
        // TODO Auto-generated method stub
        return (this.myAspects.getAmount(tag) > amount);
    }

    // added directly from the crucible of souls
    @Override
    public boolean doesContainerContain(AspectList ot) {
        boolean hasIt = true;
        ot.aspects.keySet().iterator();
        for (Aspect next : ot.aspects.keySet()) {
            if (this.myAspects.getAmount(next) < ot.getAmount(next)) hasIt = false;
        }
        return hasIt;
    }

    // added directly from the crucible of souls
    @Override
    public int containerContains(Aspect tag) {
        return this.myAspects.getAmount(tag);
    }

    // added directly from the crucible of souls
    @Override
    public boolean isConnectable(ForgeDirection face) {
        return (face != ForgeDirection.UP);
    }

    // added directly from the crucible of souls
    @Override
    public boolean canInputFrom(ForgeDirection face) {
        return false;
    }

    // added directly from the crucible of souls
    @Override
    public boolean canOutputTo(ForgeDirection face) {
        return (face != ForgeDirection.UP);
    }

    // added directly from the crucible of souls
    @Override
    public void setSuction(Aspect aspect, int amount) {
        // TODO Auto-generated method stub

    }

    // added directly from the crucible of souls
    @Override
    public int takeEssentia(Aspect aspect, int amount, ForgeDirection arg2) {
        if (arg2 != ForgeDirection.UP) {
            if (!this.worldObj.isRemote) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            if (amount > this.myAspects.getAmount(aspect)) {
                int total = this.myAspects.getAmount(aspect);
                this.myAspects.reduce(aspect, total);
                return total;
            } else {
                this.myAspects.reduce(aspect, amount);
                return amount;
            }

        } else {
            return 0;
        }

    }

    // added directly from the crucible of souls
    @Override
    public int getMinimumSuction() {
        // TODO Auto-generated method stub
        return 0;
    }

    // added directly from the crucible of souls
    @Override
    public boolean renderExtendedTube() {
        // TODO Auto-generated method stub
        return false;
        // NEW AFTER THIS LINE
    }

    // added directly from the crucible of souls
    @Override
    public Aspect getSuctionType(ForgeDirection face) {
        return null;
    }

    // added directly from the crucible of souls
    @Override
    public int getSuctionAmount(ForgeDirection face) {
        return 0;
    }

    // added directly from the crucible of souls
    @Override
    public Aspect getEssentiaType(ForgeDirection face) {
        return this.myAspects.size() > 0
                ? this.myAspects.getAspects()[this.worldObj.rand.nextInt(this.myAspects.getAspects().length)]
                : null;
    }

    // added directly from the crucible of souls
    @Override
    public int getEssentiaAmount(ForgeDirection face) {
        return this.myAspects.visSize();
    }

    // added directly from the crucible of souls
    @Override
    public int addEssentia(Aspect arg0, int arg1, ForgeDirection arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * @Override public boolean isItemValidForSlot(int i, ItemStack itemStack) { return true; }
     * @Override public void openInventory() { }
     * @Override public void closeInventory() { }
     * @Override public boolean isUseableByPlayer(EntityPlayer entityPlayer) { return entityPlayer.getDistanceSq(xCoord
     * + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64; }
     * @Override public int getInventoryStackLimit() { return inventory.getInventoryStackLimit(); }
     * @Override public boolean hasCustomInventoryName() { return false; }
     * @Override public String getInventoryName() { return tileEntityName; }
     * @Override public void setInventorySlotContents(int i, ItemStack itemStack) {
     * inventory.setInventorySlotContents(i, itemStack); markDirty(); }
     * @Override public ItemStack getStackInSlotOnClosing(int i) { ItemStack item = getStackInSlot(i);
     * setInventorySlotContents(i, null); return item; }
     * @Override public ItemStack decrStackSize(int i, int j) { ItemStack itemStack = getStackInSlot(i); if (itemStack
     * != null) { if (itemStack.stackSize <= j) { setInventorySlotContents(i, null); }else{ itemStack =
     * itemStack.splitStack(j); markDirty(); } } return itemStack; }
     * @Override public ItemStack getStackInSlot(int i) { return inventory.getStackInSlot(i); }
     * @Override public int[] getAccessibleSlotsFromSide(int i) { return inventory.getAccessibleSlotsFromSide(i); }
     * @Override public boolean canInsertItem(int i, ItemStack itemStack, int i1) { return inventory.canInsertItem(i,
     * itemStack, i1); }
     * @Override public boolean canExtractItem(int i, ItemStack itemStack, int i1) { return inventory.canExtractItem(i,
     * itemStack, i1); }
     * @Override public int getSizeInventory() { return inventory.getSizeInventory(); } private static class
     * PhialingCabinetInventory implements IPhialingCabinetInventory { public static final int SLOT_ONE = 0; public
     * static final int SLOT_TWO = 1; public static final int SLOT_THREE = 2; public static final int SLOT_FOUR = 3;
     * public static final int SLOT_FIVE = 4; public static final int SLOT_SIX = 5; public static final int SLOT_SEVEN =
     * 6; public static final int SLOT_EIGHT = 7; public static final int SLOT_NINE = 8; private final
     * TileEntityPhialingCabinet phialingCabinet; private ItemStack[] items; private
     * PhialingCabinetInventory(TileEntityPhialingCabinet phialingCabinet) { this.phialingCabinet = phialingCabinet;
     * this.items = new ItemStack[12]; }
     * @Override public ItemStack getSlot1() { return phialingCabinet.getStackInSlot(SLOT_ONE); }
     * @Override public ItemStack getSlot2() { return phialingCabinet.getStackInSlot(SLOT_TWO); }
     * @Override public ItemStack getSlot3() { return phialingCabinet.getStackInSlot(SLOT_THREE); }
     * @Override public ItemStack getSlot4() { return phialingCabinet.getStackInSlot(SLOT_FOUR); }
     * @Override public ItemStack getSlot5() { return phialingCabinet.getStackInSlot(SLOT_FIVE); }
     * @Override public ItemStack getSlot6() { return phialingCabinet.getStackInSlot(SLOT_SIX); }
     * @Override public ItemStack getSlot7() { return phialingCabinet.getStackInSlot(SLOT_SEVEN); }
     * @Override public ItemStack getSlot8() { return phialingCabinet.getStackInSlot(SLOT_EIGHT); }
     * @Override public ItemStack getSlot9() { return phialingCabinet.getStackInSlot(SLOT_NINE); }
     * @Override public void setSlot1(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_ONE,
     * itemstack); }
     * @Override public void setSlot2(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_TWO,
     * itemstack); }
     * @Override public void setSlot3(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_THREE,
     * itemstack); }
     * @Override public void setSlot4(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_FOUR,
     * itemstack); }
     * @Override public void setSlot5(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_FIVE,
     * itemstack); }
     * @Override public void setSlot6(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_SIX,
     * itemstack); }
     * @Override public void setSlot7(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_SEVEN,
     * itemstack); }
     * @Override public void setSlot8(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_EIGHT,
     * itemstack); }
     * @Override public void setSlot9(ItemStack itemstack) { phialingCabinet.setInventorySlotContents(SLOT_NINE,
     * itemstack); } public int getSizeInventory() { return items.length; } public ItemStack getStackInSlot(int i) {
     * return items[i]; } public void setInventorySlotContents(int i, ItemStack itemStack) { items[i] = itemStack; if
     * (itemStack != null && itemStack.stackSize > getInventoryStackLimit()){ itemStack.stackSize =
     * getInventoryStackLimit(); } } public int[] getAccessibleSlotsFromSide(int side) { if (side == 0 || side == 1) {
     * return new int[] { SLOT_ONE, SLOT_TWO }; } else { int[] slots = new int[SLOT_NINE+1]; for (int i = 0, slot =
     * SLOT_ONE; i < SLOT_NINE; i++, slot++) { slots[i] = slot; } return slots; } } public boolean canInsertItem(int
     * slot, ItemStack itemStack, int side) { return true; } public boolean canExtractItem(int slot, ItemStack
     * itemStack, int side) { return true; } public int getInventoryStackLimit() { return 64; } public void
     * writeToNBT(NBTTagCompound compound) { NBTTagList itemsNBT = new NBTTagList(); for (int i = 0; i < items.length;
     * i++) { ItemStack itemStack = items[i]; if (itemStack != null) { NBTTagCompound item = new NBTTagCompound();
     * item.setByte("Slot", (byte)i); itemStack.writeToNBT(item); itemsNBT.appendTag(item); } } compound.setTag("Items",
     * itemsNBT); } public void readFromNBT(NBTTagCompound compound) { NBTTagList items = compound.getTagList("Items",
     * Constants.NBT.TAG_COMPOUND); for (int i = 0; i < items.tagCount(); i++) { NBTTagCompound item =
     * items.getCompoundTagAt(i); int slot = item.getByte("Slot"); if (slot >= 0 && slot < getSizeInventory()) {
     * setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item)); } } } }
     */
}
