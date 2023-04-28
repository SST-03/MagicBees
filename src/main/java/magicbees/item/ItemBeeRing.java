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

                // Logic to run the ring (this logic could be moved out of the item class, but it seems fine here)
                if (slot != -1) {
                    InventoryBeeRing inventoryRing = getInventory((EntityPlayer) entityLivingBase, itemStack, slot, true);
                    if (hasQueen(itemStack, (EntityPlayer) entityLivingBase, slot, inventoryRing)) {
                        tickQueen(itemStack, (EntityPlayer) entityLivingBase, slot, inventoryRing);
                    } else if (hasDrone(itemStack, (EntityPlayer) entityLivingBase, slot, inventoryRing)) {
                        createQueenFromDrone(itemStack, (EntityPlayer) entityLivingBase, slot, inventoryRing);
                    }
                }
            }
        }
    }

    private void createQueenFromDrone(ItemStack itemStack, EntityPlayer player, int slot, InventoryBeeRing ringInventory) {

        ItemStack droneStack = ringInventory.getStackInSlot(0);
        if (magicbees.bees.BeeManager.beeRoot.isDrone(droneStack)) {
            IBee bee = magicbees.bees.BeeManager.beeRoot.getMember(droneStack);
            if (droneStack.stackSize == 1) {
                ringInventory.setInventorySlotContents(0, null);
            } else {
                droneStack.stackSize = droneStack.stackSize - 1;
                ringInventory.setInventorySlotContents(0, droneStack);
            }
            ItemStack temp = droneStack.copy();
            temp.stackSize = 1;
            ringInventory.setInventorySlotContents(1, temp);

            int current = bee.getHealth();
            int max = bee.getMaxHealth();
            ringInventory.setCurrentBeeHealth((current * 100) / max);
            ringInventory.setCurrentBeeColour(bee.getGenome().getPrimary().getIconColour(0));
        }
    }

    private boolean hasDrone(ItemStack itemStack, EntityPlayer player, int slot, InventoryBeeRing ringInventory) {
        return getInventory(player, itemStack, slot, true).getStackInSlot(DRONE_SLOT) != null;
    }

    private void tickQueen(ItemStack itemStack, EntityPlayer player, int slot, InventoryBeeRing ringInventory) {


        IBee queen = magicbees.bees.BeeManager.beeRoot.getMember(ringInventory.getStackInSlot(QUEEN_SLOT));
        ringInventory.setCurrentBeeHealth((queen.getHealth() * 100) / queen.getMaxHealth());
        ringInventory.setCurrentBeeColour(queen.getGenome().getPrimary().getIconColour(0));

        RingHousing housingLogic = new RingHousing(player, ringInventory);

        ringInventory.setEffectAndInitialize(queen);
        ringInventory.effectData = queen.doEffect(ringInventory.effectData, housingLogic);
        ringInventory.writeEffectNBT();

        // Crashes irregularly for some reason I can't figure out, but this is just the bee effects around you, so it's
        // not a huge deal. It would be nice to have though
        // I will get around to it later. (Crash has something to do with entityFX when the player is moving)

        // if (player.getEntityWorld().getWorldTime() % 5 == 0) {
        // IBR.effectData = queen.doFX(IBR.effectData, housingLogic);
        // }

        // run the queen
        if (ringInventory.throttle > 550) {
            ringInventory.setThrottle(0);
            queen.age(player.getEntityWorld(), 0.26f);

            if (queen.getHealth() == 0) {
                ringInventory.setInventorySlotContents(1, null);
                ringInventory.setCurrentBeeHealth(0);
                ringInventory.setCurrentBeeColour(0x0ffffff);
            } else {
                queen.writeToNBT(ringInventory.contents[QUEEN_SLOT].stackTagCompound);
            }
        } else {
            ringInventory.setThrottle(ringInventory.throttle + 1);
        }
    }

    private boolean hasQueen(ItemStack itemStack, EntityPlayer player, int slot, InventoryBeeRing ringInventory) {
        return ringInventory.getStackInSlot(QUEEN_SLOT) != null;
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
