package magicbees.tileentity;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBeeHousing;
import forestry.apiculture.genetics.BeeGenome;
import magicbees.bees.BeeSpecies;
import magicbees.main.Config;
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
    public int maxAmount = Config.thaumcraftEssentiaBeePhialingCabinetCapacity;

    private int increment = 0;

    @Override
    public void updateEntity() {
        // If there's no stored aspect we shouldn't even check for the above block.
        if (aspect == null) return;

        if (increment % Config.thaumcraftEssentiaBeePhialingCabinetTimeBetween == 0) {
            try {
                TileEntity above = worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
                if (IBeeHousing.class.isAssignableFrom(above.getClass())) {
                    IBeeHousing beeHousing = (IBeeHousing) above;
                    ItemStack queenStack = beeHousing.getBeeInventory().getQueen();

                    // This performs all the checks to see if the bee is a living queen and if the species conditions
                    // are met.
                    if (!beeHousing.getBeekeepingLogic().canWork()) return;

                    IAlleleBeeSpecies queenSpecies = BeeGenome.getSpecies(queenStack);
                    if (queenSpecies == null) return;

                    if (Objects.equals(queenSpecies.getUID(), BeeSpecies.TC_ESSENTIA.getSpecies().getUID())) {
                        addToContainer(aspect, Config.thaumcraftEssentiaBeePhialingCabinetAmount);
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
        if (am != 0) {
            if (this.amount < this.maxAmount && tag == this.aspect || this.amount == 0) {
                this.aspect = tag;
                int added = Math.min(am, this.maxAmount - this.amount);
                this.amount += added;
                am -= added;
            }
        }
        return am;
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
    public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
        if (face != ForgeDirection.UP) {
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
    public int addEssentia(Aspect aspect, int amount, ForgeDirection arg2) {
        // TODO Auto-generated method stub
        return 0;
    }
}
