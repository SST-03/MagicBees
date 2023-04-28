package magicbees.client.gui;

import magicbees.itemInventories.InventoryBeeRing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ContainerEffectRing extends effectContainer {

    public ContainerEffectRing(InventoryBeeRing IBR, EntityPlayer player) {
        super(IBR, player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
}
