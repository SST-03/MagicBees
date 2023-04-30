package magicbees.main.utils.compat;

import net.minecraft.item.Item;

import cpw.mods.fml.common.Loader;
import magicbees.main.Config;
import magicbees.main.utils.ItemInterface;

public class EquivalentExchangeHelper implements IModHelper {

    public static Item minuimShard;

    private static boolean isEquivalentExchangePresent = false;

    public static boolean isActive() {
        return isEquivalentExchangePresent;
    }

    public void preInit() {
        if (Loader.isModLoaded("EE3") && Config.equivalentExchangeActive) {
            isEquivalentExchangePresent = true;
        }
    }

    public void init() {
        if (isActive()) {
            getBlocks();
            getItems();
        }
    }

    public void postInit() {}

    private static void getBlocks() {}

    private static void getItems() {
        minuimShard = ItemInterface.getItem("EE3", "shardMinium");
    }
}
