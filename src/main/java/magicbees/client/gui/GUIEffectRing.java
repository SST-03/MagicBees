package magicbees.client.gui;

import magicbees.item.ItemBeeRing;
import magicbees.itemInventories.InventoryBeeRing;
import magicbees.main.CommonProxy;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GUIEffectRing extends GuiContainer {

    public static final String BACKGROUND_FILE = "ringScreen.png";
    public static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(
            CommonProxy.DOMAIN,
            CommonProxy.GUI_TEXTURE + BACKGROUND_FILE);

    private static final int WIDTH = 176;
    private static final int HEIGHT = 156;

    private static final int BAR_DEST_X = 117;
    private static final int BAR_DEST_Y = 10;

    private static final int BAR_SRC_X = 176;
    private static final int BAR_SRC_Y = 0;

    private static final int BAR_WIDTH = 10;
    private static final int BAR_HEIGHT = 40;

    public GUIEffectRing(ItemStack itemStack, EntityPlayer player) {
        super(
                new ContainerEffectRing(
                        ItemBeeRing.getInventory(player, itemStack, player.inventory.currentItem, false),
                        player));
        this.xSize = WIDTH;
        this.ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Inventory", 9, 63, 0);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_SRC_ALPHA);
        this.mc.getTextureManager().bindTexture(BACKGROUND_LOCATION);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);

        InventoryBeeRing IBR = ((ContainerEffectRing) this.inventorySlots).IBR;
        float r = ((IBR.currentBeeColour >> 16) & 255) / 255f;
        float g = ((IBR.currentBeeColour >> 8) & 255) / 255f;
        float b = (IBR.currentBeeColour & 255) / 255f;

        GL11.glColor3f(r, g, b);

        int value = BAR_HEIGHT - (IBR.currentBeeHealth * BAR_HEIGHT) / 100;
        this.drawTexturedModalRect(
                this.guiLeft + BAR_DEST_X,
                this.guiTop + value + BAR_DEST_Y,
                BAR_SRC_X,
                BAR_SRC_Y,
                BAR_WIDTH,
                BAR_HEIGHT - value);
    }
}
