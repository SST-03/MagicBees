package magicbees.tileentity;

import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.Loader;
import forestry.api.apiculture.*;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;

public class TileEntityApiamancersDrainerGT extends TileEntityApiamancersDrainerCommon {

    public static boolean isGTLoaded = Loader.isModLoaded("gregtech");

    @Override
    protected IBeeHousing beeHousing(TileEntity above) {
        IBeeHousing regularCheck = super.beeHousing(above);
        if (regularCheck != null) return regularCheck;

        BaseMetaTileEntity GTMetaTileEntity = GTTileEntity(above);

        if (GTMetaTileEntity != null) {
            IMetaTileEntity underlyingMetaTileEntity = GTMetaTileEntity.getMetaTileEntity();

            if (!(underlyingMetaTileEntity instanceof GT_MetaTileEntity_IndustrialApiary)) return null;

            return (IBeeHousing) underlyingMetaTileEntity;
        }

        return null;
    }

    @Override
    protected boolean canWork(IBeeHousing beeHousing, TileEntity te) {
        boolean canNormallyWork = super.canWork(beeHousing, te);
        BaseMetaTileEntity GTMetaTileEntity = GTTileEntity(te);

        return GTMetaTileEntity != null ? GTMetaTileEntity.isActive() : canNormallyWork;
    }

    private BaseMetaTileEntity GTTileEntity(TileEntity te) {
        boolean isGTMetaTileEntity = te instanceof BaseMetaTileEntity;

        return isGTMetaTileEntity ? (BaseMetaTileEntity) te : null;
    }
}
