package magicbees.client.gui;

import cpw.mods.fml.common.registry.GameRegistry;
import magicbees.itemInventories.InventoryBeeRing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEffectRing extends Container {

    InventoryBeeRing IBR;

    public ContainerEffectRing(InventoryBeeRing IBR, EntityPlayer player) {
        this.IBR = IBR;
        this.addSlotToContainer(
            new SlotCustomItems(this.IBR, 0, 80, 22, GameRegistry.findItemStack("Forestry", "beeDroneGE", 1)));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 74 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 132));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        Slot itemSlot = this.getSlot(slot);
        boolean clearSlot = false;

        if (itemSlot != null && itemSlot.getHasStack()) {
            ItemStack srcStack = itemSlot.getStack();
            if (slot == 0 && srcStack != null) {
                clearSlot = this.mergeItemStack(srcStack, 1, 36 + 1, false);
            } else {
                if (this.getSlot(0).isItemValid(srcStack)) {
                    clearSlot = this.mergeItemStack(srcStack, 0, 1, false);
                }
            }
        }

        if (clearSlot) {
            itemSlot.putStack(null);
        }
        itemSlot.onSlotChanged();
        player.inventory.markDirty();

        return null;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
}
