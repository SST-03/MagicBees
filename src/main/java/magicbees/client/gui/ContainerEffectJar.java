package magicbees.client.gui;

import magicbees.tileentity.TileEntityEffectJar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerEffectJar extends effectContainer {

    public int lastBeeHealth = 0;
    public int speciesColour = 0xffffff;

    TileEntityEffectJar jar;
    public ContainerEffectJar(TileEntityEffectJar tileEntityEffectJar, EntityPlayer player) {
        super(tileEntityEffectJar, player);
        jar = (TileEntityEffectJar) inventory;
    }

    @Override
    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);

        crafting.sendProgressBarUpdate(this, 0, this.jar.currentBeeHealth);
        crafting.sendProgressBarUpdate(this, 1, this.jar.currentBeeColour);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting crafting = (ICrafting) this.crafters.get(i);

            if (this.lastBeeHealth != this.jar.currentBeeHealth) {
                crafting.sendProgressBarUpdate(this, 0, this.jar.currentBeeHealth);
                this.lastBeeHealth = this.jar.currentBeeHealth;
            }
            if (this.speciesColour != this.jar.currentBeeColour) {
                crafting.sendProgressBarUpdate(this, 1, this.jar.currentBeeColour);
                this.speciesColour = this.jar.currentBeeColour;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int craftingId, int value) {
        if (craftingId == 0) {
            this.jar.currentBeeHealth = value;
        } else if (craftingId == 1) {
            this.jar.currentBeeColour = value;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return this.jar.isUseableByPlayer(entityplayer);
    }
}
