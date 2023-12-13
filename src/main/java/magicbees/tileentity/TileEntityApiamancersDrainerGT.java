package magicbees.tileentity;

import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.tileentity.TileEntity;

import org.apache.commons.lang3.ClassUtils;

import cpw.mods.fml.common.Loader;
import forestry.api.apiculture.*;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;
import magicbees.main.utils.LogHelper;

public class TileEntityApiamancersDrainerGT extends TileEntityApiamancersDrainerCommon {

    public static boolean isGTLoaded = Loader.isModLoaded("gregtech");

    @Override
    protected IBeeHousing beeHousing() {
        TileEntity above = worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
        boolean isGTMetaTileEntity = above instanceof BaseMetaTileEntity;

        if (isGTMetaTileEntity) {
            BaseMetaTileEntity metaTileEntity = (BaseMetaTileEntity) above;
            IMetaTileEntity underlyingMetaTileEntity = metaTileEntity.getMetaTileEntity();

            if (!(underlyingMetaTileEntity instanceof GT_MetaTileEntity_IndustrialApiary)) return null;

            Set<String> classes = ClassUtils.getAllInterfaces(underlyingMetaTileEntity.getClass()).stream()
                    .map(Class::getName).collect(Collectors.toSet());
            LogHelper.warn(classes);

            return (IBeeHousing) underlyingMetaTileEntity;
        }

        return null;
    }
}
