package magicbees.main.utils.compat.bloodmagic;

import java.util.List;

import magicbees.main.CommonProxy;
import magicbees.main.utils.TabMagicBees;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.*;

public class ItemFrenziedFrame extends EnergyItems implements IHiveFrame {

    private final IBeeModifier beeModifier = new FrenziedFrameBeeModifier();
    private EntityPlayer owner = null;

    public ItemFrenziedFrame() {
        super();
        this.maxStackSize = 1;
        this.setMaxDamage(1);
        setEnergyUsed(1000);
        setCreativeTab(TabMagicBees.tabMagicBees);
        this.setUnlocalizedName("frenziedFrame");
    }

    private static class FrenziedFrameBeeModifier implements IBeeModifier {

        @Override
        public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return 10;
        }

        @Override
        public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) {
            return 0.0001f;
        }

        @Override
        public float getProductionModifier(IBeeGenome genome, float currentModifier) {
            return 0;
        }

        @Override
        public float getFloweringModifier(IBeeGenome genome, float currentModifier) {
            return 0;
        }

        @Override
        public float getGeneticDecay(IBeeGenome genome, float currentModifier) {
            return 10;
        }

        @Override
        public boolean isSealed() {
            return false;
        }

        @Override
        public boolean isSelfLighted() {
            return false;
        }

        @Override
        public boolean isSunlightSimulated() {
            return false;
        }

        @Override
        public boolean isHellish() {
            return false;
        }

        @Override
        public float getTerritoryModifier(IBeeGenome genome, float currentModifier) {
            return 1;
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("MAY CHAOS TAKE THE WORLD"));

        if (!(par1ItemStack.getTagCompound() == null)) {
            par3List.add(
                    StatCollector.translateToLocal("tooltip.owner.currentowner") + " "
                            + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":frenziedFrame");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {

        if (EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer)) {
            owner = par3EntityPlayer;
            if (par1ItemStack.getItemDamage() > 0) {
                if (EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed())) {
                    par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
                }
            }
        }

        return par1ItemStack;
    }

    @Override
    public ItemStack frameUsed(IBeeHousing housing, ItemStack frame, IBee queen, int wear) {
        // TODO Auto-generated method stub
        if (EnergyItems.canSyphonInContainer(frame, getEnergyUsed() * wear)) {
            if (owner != null) {
                EnergyItems.drainPlayerNetwork(owner, getEnergyUsed() * wear);
            }
            // EnergyItems.syphonWhileInContainer(frame, getEnergyUsed() * wear);
        } else {
            frame.setItemDamage(frame.getItemDamage() + wear);
            if (frame.getItemDamage() >= frame.getMaxDamage()) {
                return null;
            }
        }
        return frame;
    }

    @Override
    public IBeeModifier getBeeModifier() {
        return beeModifier;
    }
}
