package magicbees.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import forestry.core.gui.GuiForestry;
import magicbees.itemInventories.InventoryBeeRing;
import magicbees.main.CommonProxy;

public class GUIEffectRing extends GuiForestry<ContainerEffectRing, IInventory> {

    public static final String BACKGROUND_FILE = "ringScreen.png";
    public static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(
            CommonProxy.DOMAIN,
            CommonProxy.GUI_TEXTURE + BACKGROUND_FILE);

    private static final int BAR_DEST_X = 117;
    private static final int BAR_DEST_Y = 10;

    private static final int BAR_SRC_X = 176;
    private static final int BAR_SRC_Y = 0;

    private static final int BAR_WIDTH = 10;
    private static final int BAR_HEIGHT = 40;

    private final InventoryBeeRing inventoryBeeRing;

    protected GUIEffectRing(InventoryBeeRing inventory, InventoryPlayer playerInventory) {
        super(BACKGROUND_LOCATION, new ContainerEffectRing(inventory, playerInventory), inventory);
        this.inventoryBeeRing = inventory;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_SRC_ALPHA);
        this.mc.getTextureManager().bindTexture(BACKGROUND_LOCATION);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);

        int currentBeeColour = this.inventoryBeeRing.getCurrentBeeColour();
        float r = ((currentBeeColour >> 16) & 255) / 255f;
        float g = ((currentBeeColour >> 8) & 255) / 255f;
        float b = (currentBeeColour & 255) / 255f;

        GL11.glColor3f(r, g, b);

        int value = BAR_HEIGHT - (this.inventoryBeeRing.getCurrentBeeHealth() * BAR_HEIGHT) / 100;
        this.drawTexturedModalRect(
                this.guiLeft + BAR_DEST_X,
                this.guiTop + value + BAR_DEST_Y,
                BAR_SRC_X,
                BAR_SRC_Y,
                BAR_WIDTH,
                BAR_HEIGHT - value);
    }
}
