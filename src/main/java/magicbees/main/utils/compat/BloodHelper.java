package magicbees.main.utils.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import magicbees.main.CommonProxy;
import magicbees.main.Config;
import magicbees.main.utils.compat.bloodmagic.ItemBloodBaseFrame;
import magicbees.main.utils.compat.bloodmagic.ItemBloodFrame;
import magicbees.main.utils.compat.bloodmagic.ItemFrenziedFrame;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigItems;

public class BloodHelper implements IModHelper {

    public static Item hiveFrameBlood;
    public static Item hiveFrameFrenzy;
    public static Item hiveFrameBloodBase;

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

    public void init() {
        if (isActive()) {
            getItems();
        }
    }

    public void postInit() {
        if (isActive()) {
            getRecipes();
            if (ThaumcraftHelper.isActive()) {
                thaumRecipes();
                thaumResearch();
            }
        }
    }

    public static void getBlocks() {}

    public static void getItems() {
        hiveFrameBloodBase = new ItemBloodBaseFrame();
        GameRegistry.registerItem(hiveFrameBloodBase, hiveFrameBloodBase.getUnlocalizedName(), CommonProxy.DOMAIN);

        hiveFrameBlood = new ItemBloodFrame();
        GameRegistry.registerItem(hiveFrameBlood, hiveFrameBlood.getUnlocalizedName(), CommonProxy.DOMAIN);

        hiveFrameFrenzy = new ItemFrenziedFrame();
        GameRegistry.registerItem(hiveFrameFrenzy, hiveFrameFrenzy.getUnlocalizedName(), CommonProxy.DOMAIN);
    }

    public static void getRecipes() {
        AltarRecipeRegistry.registerAltarRecipe(
                new ItemStack(hiveFrameBloodBase),
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
                new ItemStack(hiveFrameBlood),
                new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.EARTH, 50)
                        .add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50),
                new Object[] { "sbs", "bFb", "sbs", 's', ModItems.imbuedSlate, 'b', ModItems.bucketLife, 'F',
                        hiveFrameBloodBase });

        ThaumcraftHelper.frenziedFrame = ThaumcraftApi.addArcaneCraftingRecipe(
                "MB_FrenziedFrame",
                new ItemStack(hiveFrameFrenzy),
                new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.WATER, 50).add(Aspect.EARTH, 50)
                        .add(Aspect.ORDER, 50).add(Aspect.ENTROPY, 50),
                new Object[] { "sns", "nFn", "sns", 's', ModItems.imbuedSlate, 'n',
                        new ItemStack(ConfigItems.itemResource, 1, 1), 'F', hiveFrameBloodBase });

    }

    public static void thaumResearch() {
        if (ThaumcraftHelper.isActive()) {
            ThaumcraftHelper.bloodFrame1 = new ResearchPage("bloodFrame.1");
            ThaumcraftHelper.bloodFrame2 = new ResearchPage((IArcaneRecipe) ThaumcraftHelper.bloodFrame);

            ThaumcraftHelper.bloodFramePage = new ResearchItem(
                    "MB_BloodFrame",
                    "MAGICBEES",
                    new AspectList().add(Aspect.LIFE, 1).add(Aspect.ORDER, 1).add(Aspect.HEAL, 1)
                            .add(Aspect.EXCHANGE, 1).add(Aspect.GREED, 1),
                    -4,
                    3,
                    1,
                    new ItemStack(BloodHelper.hiveFrameBlood));

            ThaumcraftHelper.frenzyFrame1 = new ResearchPage("frenziedFrame.1");
            ThaumcraftHelper.frenzyFrame2 = new ResearchPage((IArcaneRecipe) ThaumcraftHelper.frenziedFrame);

            ThaumcraftHelper.frenzyFramePage = new ResearchItem(
                    "MB_FrenziedFrame",
                    "MAGICBEES",
                    new AspectList().add(Aspect.LIFE, 1).add(Aspect.ORDER, 1).add(Aspect.HEAL, 1)
                            .add(Aspect.EXCHANGE, 1).add(Aspect.GREED, 1),
                    -5,
                    3,
                    1,
                    new ItemStack(BloodHelper.hiveFrameFrenzy));

            ThaumcraftHelper.bloodFramePage.setPages(ThaumcraftHelper.bloodFrame1, ThaumcraftHelper.bloodFrame2);
            ThaumcraftHelper.bloodFramePage.setParents("MB_FrameMagic");

            ThaumcraftHelper.frenzyFramePage.setPages(ThaumcraftHelper.frenzyFrame1, ThaumcraftHelper.frenzyFrame2);
            ThaumcraftHelper.frenzyFramePage.setParents("MB_FrameMagic");

            ResearchCategories.addResearch(ThaumcraftHelper.bloodFramePage);
            ResearchCategories.addResearch(ThaumcraftHelper.frenzyFramePage);
        }
    }

}
