package magicbees.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.main.CommonProxy;
import magicbees.main.utils.TabMagicBees;
import magicbees.tileentity.TileEntityPhialingCabinet;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;

public class BlockPhialingCabinet extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockPhialingCabinet() {
        super(Material.wood);
        this.setCreativeTab(TabMagicBees.tabMagicBees);
        this.setBlockName("phialingCabinet");
        this.setHardness(1f);
        this.setResistance(1.5f);
        this.setHarvestLevel("axe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityPhialingCabinet();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
            float par8, float par9) {
        if (world.isRemote) {
            return false;
        } else {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileEntityPhialingCabinet) {
                ItemStack tItemStack = player.getHeldItem();
                if (tItemStack != null) {
                    Item tItem = tItemStack.getItem();
                    if (tItem instanceof IEssentiaContainerItem
                            && ((IEssentiaContainerItem) tItem).getAspects(player.getHeldItem()) != null
                            && ((IEssentiaContainerItem) tItem).getAspects(player.getHeldItem()).size() > 0) {
                        Aspect tLocked = ((IEssentiaContainerItem) tItem).getAspects(player.getHeldItem())
                                .getAspects()[0];
                        ((TileEntityPhialingCabinet) tile).setAspect(tLocked);

                        // TODO: improve text
                        player.addChatMessage(
                                new ChatComponentTranslation("Producing " + tLocked.getLocalizedDescription()));
                    }
                } else {
                    ((TileEntityPhialingCabinet) tile).setAspect(null);

                    // TODO: improve text
                    player.addChatMessage(new ChatComponentTranslation("Cleared production specifier"));
                }
                world.markBlockForUpdate(x, y, z);
                return true;
            } else return false;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (side == 0) {
            return icons[0];
        } else if (side == 1) {
            return icons[1];
        } else {
            return icons[2];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[4];

        icons[0] = register.registerIcon(CommonProxy.DOMAIN + ":phialingcabinet.0");
        icons[1] = register.registerIcon(CommonProxy.DOMAIN + ":phialingcabinet.1");
        icons[2] = register.registerIcon(CommonProxy.DOMAIN + ":phialingcabinet.2");
    }
}
