package magicbees.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.main.CommonProxy;
import magicbees.main.utils.TabMagicBees;
import magicbees.tileentity.TileEntityPhialingCabinet;

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
        boolean activate = false;

        return activate;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        if (side == 0) {
            return icons[0];
        } else if (side == 1) {
            return icons[1];
        } else if (side == 2 || side == 3) {
            return icons[2];
        } else {
            return icons[3];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[4];

        icons[0] = register.registerIcon(CommonProxy.DOMAIN + ":stripped_tainted_birch_log_top");
        icons[1] = register.registerIcon(CommonProxy.DOMAIN + ":stripped_tainted_birch_log_top");
        icons[2] = register.registerIcon(CommonProxy.DOMAIN + ":stripped_tainted_birch_log");
        icons[3] = register.registerIcon(CommonProxy.DOMAIN + ":stripped_tainted_birch_wood");
    }
}
