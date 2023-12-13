package magicbees.tileentity;

import java.util.stream.Collectors;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.common.Loader;
import forestry.api.apiculture.IBeeHousing;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;

public class TileEntityApiamancersDrainerGT extends TileEntityApiamancersDrainerCommon
        implements IGregTechDeviceInformation {

    public static boolean isGTLoaded = Loader.isModLoaded("gregtech");

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        String aspects = essentia.aspects.entrySet().stream()
                .map(
                        e -> EnumChatFormatting.LIGHT_PURPLE + e.getKey().getName()
                                + ": "
                                + EnumChatFormatting.GREEN
                                + e.getValue())
                .collect(Collectors.joining(EnumChatFormatting.RESET + ", " + EnumChatFormatting.RESET));

        return new String[] { //
                EnumChatFormatting.DARK_PURPLE + "Apiamancer's Drainer" + EnumChatFormatting.RESET, //
                aspect != null ? "Attuned: " + aspect.getName() : "Not attuned", //
                "Stored Essentia:", //
                aspects //
        };
    }

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
