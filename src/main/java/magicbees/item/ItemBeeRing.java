package magicbees.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IBee;
import magicbees.client.gui.UIScreens;
import magicbees.main.CommonProxy;
import magicbees.main.MagicBees;
import magicbees.itemInventories.InventoryBeeRing;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class ItemBeeRing extends Item implements IBauble {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    private final int DRONE_SLOT = 0;
    private final int QUEEN_SLOT = 1;
    public ItemBeeRing()
    {
        super();
        this.maxStackSize = 1;
        this.canRepair = false;
        this.setMaxDamage(0);
        this.setCreativeTab(Thaumcraft.tabTC);
        this.setUnlocalizedName("beeRing");
        GameRegistry.registerItem(this, "beeRing");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (null == itemStack.getTagCompound())
        {
            onCreated(itemStack, world, player);
        }

        if (!world.isRemote) {
            if(!player.isSneaking())
            {
                player.openGui(MagicBees.object, UIScreens.EFFECT_RING.ordinal(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
            else
            {
                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);

                for(int i = 0; i < baubles.getSizeInventory(); ++i) {
                    if (baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, itemStack)) {
                        baubles.setInventorySlotContents(i, itemStack.copy());
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
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
        if (entityLivingBase instanceof EntityPlayer)
        {
            if (hasQueen(itemStack, (EntityPlayer) entityLivingBase)) {
                tickQueen(itemStack);
            } else if (hasDrone(itemStack, (EntityPlayer) entityLivingBase)) {
                createQueenFromDrone(itemStack, (EntityPlayer) entityLivingBase);
            }
        }
    }

    private void createQueenFromDrone(ItemStack itemStack, EntityPlayer player) {
        IInventory IBR = getInventory(player, itemStack);
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
//            currentBeeHealth = (current * 100) / max;
//            currentBeeColour = bee.getGenome().getPrimary().getIconColour(0);
        }
    }

    private boolean hasDrone(ItemStack itemStack, EntityPlayer player) {
        return getInventory(player, itemStack).getStackInSlot(DRONE_SLOT) != null;
    }

    private void tickQueen(ItemStack itemStack) {
    }

    private boolean hasQueen(ItemStack itemStack, EntityPlayer player) {
        return getInventory(player, itemStack).getStackInSlot(QUEEN_SLOT) != null;
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

    public void onCreated(ItemStack itemStack, World world, EntityPlayer player)
    {
//        if (itemStack.getTagCompound() == null) {
//            itemStack.setTagCompound(new NBTTagCompound());
//        }
    }
    public static IInventory getInventory(EntityPlayer player, ItemStack itemStack)
    {
        return new InventoryBeeRing(itemStack, player);
    }
}
