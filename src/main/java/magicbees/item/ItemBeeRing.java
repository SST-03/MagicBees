package magicbees.item;

import magicbees.client.gui.UIScreens;
import magicbees.itemInventories.InventoryBeeRing;
import magicbees.main.CommonProxy;
import magicbees.main.MagicBees;
import magicbees.tileentity.RingHousing;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import thaumcraft.common.Thaumcraft;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IBee;

public class ItemBeeRing extends Item implements IBauble {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    private final int DRONE_SLOT = 0;
    private final int QUEEN_SLOT = 1;

    public ItemBeeRing() {
        super();
        this.maxStackSize = 1;
        this.canRepair = false;
        this.setMaxDamage(0);
        this.setCreativeTab(Thaumcraft.tabTC);
        this.setUnlocalizedName("beeRing");
        GameRegistry.registerItem(this, "beeRing");
    }

    // Equips item if shift is held and otherwise it opens the GUI
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (!player.isSneaking()) {
                player.openGui(
                        MagicBees.object,
                        UIScreens.EFFECT_RING.ordinal(),
                        world,
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ);
            } else {
                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);

                for (int i = 0; i < baubles.getSizeInventory(); ++i) {
                    if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, itemStack)) {
                        baubles.setInventorySlotContents(i, itemStack.copy());
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        this.onEquipped(itemStack, player);
                        break;
                    }
                }
            }
        }
        return itemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CommonProxy.DOMAIN + ":beeRing");
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {

        // If I'm being honest I'm not sure if this needs to go here, but it works if I put it here.
        if (!entityLivingBase.worldObj.isRemote) {
            if (entityLivingBase instanceof EntityPlayer) {
                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles((EntityPlayer) entityLivingBase);
                int slot = -1;
                for (int i = 0; i < baubles.getSizeInventory(); i++) {
                    if (baubles.getStackInSlot(i) == itemStack) {
                        slot = i;
                        break;
                    }
                }

                // Logic to run the ring (this logic could be moved out of the item class, but it seems fine here
                if (slot != -1) {
                    if (hasQueen(itemStack, (EntityPlayer) entityLivingBase, slot)) {
                        tickQueen(itemStack, (EntityPlayer) entityLivingBase, slot);
                    } else if (hasDrone(itemStack, (EntityPlayer) entityLivingBase, slot)) {
                        createQueenFromDrone(itemStack, (EntityPlayer) entityLivingBase, slot);
                    }
                }
            }
        }
    }

    private void createQueenFromDrone(ItemStack itemStack, EntityPlayer player, int slot) {

        InventoryBeeRing IBR = getInventory(player, itemStack, slot, true);
        ItemStack droneStack = IBR.getStackInSlot(0);
        if (magicbees.bees.BeeManager.beeRoot.isDrone(droneStack)) {
            IBee bee = magicbees.bees.BeeManager.beeRoot.getMember(droneStack);
            if (droneStack.stackSize == 1) {
                IBR.setInventorySlotContents(0, null);
            } else {
                droneStack.stackSize = droneStack.stackSize - 1;
                IBR.setInventorySlotContents(0, droneStack);
            }
            ItemStack temp = droneStack.copy();
            temp.stackSize = 1;
            IBR.setInventorySlotContents(1, temp);

            int current = bee.getHealth();
            int max = bee.getMaxHealth();
            IBR.setCurrentBeeHealth((current * 100) / max);
            IBR.setCurrentBeeColour(bee.getGenome().getPrimary().getIconColour(0));
        }
    }

    private boolean hasDrone(ItemStack itemStack, EntityPlayer player, int slot) {
        return getInventory(player, itemStack, slot, true).getStackInSlot(DRONE_SLOT) != null;
    }

    private void tickQueen(ItemStack itemStack, EntityPlayer player, int slot) {

        InventoryBeeRing IBR = getInventory(player, itemStack, slot, true);

        IBee queen = magicbees.bees.BeeManager.beeRoot.getMember(IBR.getStackInSlot(QUEEN_SLOT));
        IBR.setCurrentBeeHealth((queen.getHealth() * 100) / queen.getMaxHealth());
        IBR.setCurrentBeeColour(queen.getGenome().getPrimary().getIconColour(0));

        RingHousing housingLogic = new RingHousing(player, IBR);

        IBR.setEffectAndInitialize(queen);

        IBR.effectData = queen.doEffect(IBR.effectData, housingLogic);
        if (player.isDead) {
            return;
        }

        IBR.writeEffectNBT();

        // Crashes irregularly for some reason I can't figure out, but this is just the bee effects around you, so it's
        // not a huge deal. It would be nice to have though
        // I will get around to it later. (Crash has something to do with entityFX when the player is moving)
        // if (player.getEntityWorld().getWorldTime() % 5 == 0) {
        // IBR.effectData = queen.doFX(IBR.effectData, housingLogic);
        // }

        // run the queen
        if (IBR.throttle > 550) {
            IBR.setThrottle(0);
            queen.age(player.getEntityWorld(), 0.26f);

            if (queen.getHealth() == 0) {
                IBR.setInventorySlotContents(1, null);
                IBR.setCurrentBeeHealth(0);
                IBR.setCurrentBeeColour(0x0ffffff);
            } else {
                queen.writeToNBT(IBR.contents[QUEEN_SLOT].stackTagCompound);
            }
        } else {
            IBR.setThrottle(IBR.throttle + 1);
        }
    }

    private boolean hasQueen(ItemStack itemStack, EntityPlayer player, int slot) {
        return getInventory(player, itemStack, slot, true).getStackInSlot(QUEEN_SLOT) != null;
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    public static InventoryBeeRing getInventory(EntityPlayer player, ItemStack itemStack, int slot,
            boolean baubleFlag) {
        return new InventoryBeeRing(itemStack, player, slot, baubleFlag);
    }
}
