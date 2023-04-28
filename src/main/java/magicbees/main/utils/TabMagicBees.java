package magicbees.main.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import magicbees.item.types.ResourceType;
import magicbees.main.Config;

public class TabMagicBees extends CreativeTabs {

    public static TabMagicBees tabMagicBees = new TabMagicBees();

    public TabMagicBees() {
        super(getNextID(), "magicBees");
    }

    public Item getTabIconItem() {
        return Config.miscResources.getStackForType(ResourceType.RESEARCH_BEEINFUSION).getItem();
    }
}
