package magicbees.client.gui;

import magicbees.item.ItemBeeRing;
import magicbees.main.CommonProxy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GUIEffectRing extends GuiContainer {


    public static final String BACKGROUND_FILE = "jarScreen.png";
    public static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation(
        CommonProxy.DOMAIN,
        CommonProxy.GUI_TEXTURE + "jarScreen.png");

    private static final int WIDTH = 176;
    private static final int HEIGHT = 156;

    public GUIEffectRing(ItemStack itemStack, EntityPlayer player) {
        super(new ContainerEffectRing(ItemBeeRing.getInventory(player, itemStack), player));
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
    }
}
