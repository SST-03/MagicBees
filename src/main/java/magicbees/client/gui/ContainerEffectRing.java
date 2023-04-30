package magicbees.client.gui;

import net.minecraft.entity.player.InventoryPlayer;

import cpw.mods.fml.common.registry.GameRegistry;
import forestry.core.gui.ContainerItemInventory;
import magicbees.itemInventories.InventoryBeeRing;

public class ContainerEffectRing extends ContainerItemInventory<InventoryBeeRing> {

    public ContainerEffectRing(InventoryBeeRing inventory, InventoryPlayer playerInventory) {
        super(inventory, playerInventory, 8, 74);
        addSlotToContainer(
                new SlotCustomItems(inventory, 0, 80, 22, GameRegistry.findItemStack("Forestry", "beeDroneGE", 1)));
    }
}
