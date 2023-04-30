package magicbees.main.utils.compat;

import net.minecraft.item.Item;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import magicbees.main.utils.compat.baubles.ItemBeeRing;

public class BaublesHelper implements IModHelper {

    private static final String Name = "Baubles";
    private static boolean isModActive = false;

    public static Item beeRing;

    public static boolean isActive() {
        return BaublesHelper.isModActive;
    }

    @Override
    public void preInit() {
        if (Loader.isModLoaded(Name)) {
            isModActive = true;
        }
    }

    @Override
    public void init() {
        if (isActive()) {
            getItems();
        }
    }

    @Override
    public void postInit() {}

    @Optional.Method(modid = Name)
    private static void getItems() {
        beeRing = new ItemBeeRing();
    }
}
