package magicbees.client.gui;

import magicbees.item.ItemBeeRing;
import magicbees.itemInventories.InventoryBeeRing;
import magicbees.tileentity.TileEntityEffectJar;
import magicbees.tileentity.TileEntityMagicApiary;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import scala.swing.Applet;

public class GUIHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Object value = null;

        if (ID == UIScreens.EFFECT_JAR.ordinal()) {
            value = new ContainerEffectJar((TileEntityEffectJar) world.getTileEntity(x, y, z), player);
        } else if (ID == UIScreens.THAUMIC_APIARY.ordinal()) {
            TileEntityMagicApiary tileEntityThaumicApiary = (TileEntityMagicApiary) world.getTileEntity(x, y, z);
            value = new ContainerMagicApiary(player.inventory, tileEntityThaumicApiary);
        } else if (ID == UIScreens.EFFECT_RING.ordinal()) {
            value = new ContainerEffectRing((InventoryBeeRing) ItemBeeRing.getInventory(player, player.getHeldItem()),player);
        }

        return value;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == UIScreens.EFFECT_JAR.ordinal()) {
            return new GUIEffectJar((TileEntityEffectJar) world.getTileEntity(x, y, z), player);
        } else if (ID == UIScreens.THAUMIC_APIARY.ordinal()) {
            TileEntityMagicApiary tileEntityThaumicApiary = (TileEntityMagicApiary) world.getTileEntity(x, y, z);
            return new GuiMagicApiary(player.inventory, tileEntityThaumicApiary);
        } else if (ID == UIScreens.EFFECT_RING.ordinal()) {
            return new GUIEffectRing(player.getHeldItem() ,player);
        }
        return null;
    }
}
