package magicbees.main.utils.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magicbees.main.CommonProxy;
import magicbees.main.Config;
import magicbees.main.utils.compat.bloodmagic.ItemBloodBaseFrame;
import magicbees.main.utils.compat.bloodmagic.ItemBloodFrame;
import magicbees.main.utils.compat.bloodmagic.ItemFrenziedFrame;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;

public class BloodHelper implements IModHelper {

    @SideOnly(Side.CLIENT)
    public static IIcon subtileIcons[];

    private static boolean isBloodMagicActive = false;
    public static final String Name = "AWWayofTime";

    public static boolean isActive() {
        return isBloodMagicActive;
    }

    public void preInit() {
        if (Loader.isModLoaded(Name) && Config.bloodMagicActive) {
            isBloodMagicActive = true;
        }
    }

    public static void bloodMagicInstalled() {
        isBloodMagicActive = true;
    }

    public void init() {}

    public void postInit() {}

    public static void getBlocks() {}

    public static void getItems() {
        Config.hiveFrameBloodBase = new ItemBloodBaseFrame();
        GameRegistry.registerItem(
                Config.hiveFrameBloodBase,
                Config.hiveFrameBloodBase.getUnlocalizedName(),
                CommonProxy.DOMAIN);

        Config.hiveFrameBlood = new ItemBloodFrame();
        GameRegistry
                .registerItem(Config.hiveFrameBlood, Config.hiveFrameBlood.getUnlocalizedName(), CommonProxy.DOMAIN);

        Config.hiveFrameFrenzy = new ItemFrenziedFrame();
        GameRegistry
                .registerItem(Config.hiveFrameFrenzy, Config.hiveFrameFrenzy.getUnlocalizedName(), CommonProxy.DOMAIN);
    }

    public static void getRecipes() {
        AltarRecipeRegistry.registerAltarRecipe(
                new ItemStack(Config.hiveFrameBloodBase),
                new ItemStack(Config.hiveFrameMagic),
                1,
                5000,
                20,
                20,
                false);
    }

    public static void thaumRecipes() {
        ThaumcraftHelper.bloodFrame = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_BloodFrame",
                new ItemStack(Config.hiveFrameBlood),
                new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.EARTH, 50)
                        .add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50),
                new Object[] { "sbs", "bFb", "sbs", 's', ModItems.imbuedSlate, 'b', ModItems.bucketLife, 'F',
                        Config.hiveFrameBloodBase });

        ThaumcraftHelper.frenziedFrame = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_FrenziedFrame",
                new ItemStack(Config.hiveFrameFrenzy),
                new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.EARTH, 50)
                        .add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50),
                new Object[] { "sns", "nFn", "sns", 's', ModItems.imbuedSlate, 'n',
                        new ItemStack(ConfigItems.itemResource, 1, 1), 'F', Config.hiveFrameBloodBase });

    }

    public static void doBloodMagicModuleConfigs(Configuration configuration) {
        Property p;
    }

}
