package magicbees.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.client.gui.UIScreens;
import magicbees.main.CommonProxy;
import magicbees.main.MagicBees;
import magicbees.itemInventories.InventoryBeeRing;
import magicbees.main.utils.MoonPhase;
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

        if (world.isRemote) {
            player.openGui(MagicBees.object, UIScreens.EFFECT_RING.ordinal(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
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

    public static IInventory getInventory(EntityPlayer player, ItemStack itemStack)
    {
        return new InventoryBeeRing(itemStack, player);
    }
}
