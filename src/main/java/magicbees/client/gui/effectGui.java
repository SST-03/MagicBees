package magicbees.client.gui;

import magicbees.itemInventories.InventoryBeeRing;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public abstract class effectGui extends GuiContainer {

    protected static final int WIDTH = 176;
    protected static final int HEIGHT = 156;

    protected static final int BAR_DEST_X = 117;
    protected static final int BAR_DEST_Y = 10;

    protected static final int BAR_SRC_X = 176;
    protected static final int BAR_SRC_Y = 0;

    protected static final int BAR_WIDTH = 10;
    protected static final int BAR_HEIGHT = 40;

    public effectGui(Container cont) {
        super(cont);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Inventory", 9, 63, 0);
    }

}
