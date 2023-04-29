package magicbees.item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import magicbees.bees.BeeManager;
import magicbees.client.gui.UIScreens;
import magicbees.itemInventories.InventoryBeeRing;
import magicbees.main.CommonProxy;
import magicbees.main.MagicBees;
import magicbees.main.utils.TabMagicBees;
import magicbees.tileentity.RingHousing;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.IBee;
import forestry.api.genetics.IEffectData;

public class ItemBeeRing extends Item implements IBauble {

    private static final Map<UUID, IEffectData[][]> PLAYER_EFFECTS = new HashMap<>();

    public ItemBeeRing() {
        super();
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setMaxDamage(0);
        this.setTextureName(CommonProxy.DOMAIN + ":beeRing");
        this.setCreativeTab(TabMagicBees.tabMagicBees);
        this.setUnlocalizedName("beeRing");
        GameRegistry.registerItem(this, "beeRing");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
                for (int i = 0; i < baubles.getSizeInventory(); i++) {
                    if (baubles.isItemValidForSlot(i, itemStack)) {
                        ItemStack stackInSlot = baubles.getStackInSlot(i);
                        if (stackInSlot == null) {
                            baubles.setInventorySlotContents(i, itemStack.copy());
                            if (!player.capabilities.isCreativeMode)
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                            break;
                        }
                    }
                }
            } else {
                player.openGui(
                        MagicBees.object,
                        UIScreens.EFFECT_RING.ordinal(),
                        world,
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ);
            }
        }
        return itemStack;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        if (!(entityLivingBase instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entityLivingBase;
        InventoryBeeRing inventoryBeeRing = new InventoryBeeRing(itemStack, player);

        if (inventoryBeeRing.hasQueen()) {
            tickQueen(player, inventoryBeeRing);
        } else if (inventoryBeeRing.hasDrone()) {
            createQueenFromDrone(inventoryBeeRing);
        }
    }

    private void createQueenFromDrone(InventoryBeeRing inventoryBeeRing) {
        ItemStack droneStack = inventoryBeeRing.getDrone();
        if (BeeManager.beeRoot.isDrone(droneStack)) {
            IBee bee = BeeManager.beeRoot.getMember(droneStack);
            if (droneStack.stackSize == 1) {
                inventoryBeeRing.setDrone(null);
            } else {
                droneStack.stackSize--;
                inventoryBeeRing.setDrone(droneStack);
            }

            ItemStack queenStack = droneStack.copy();
            queenStack.stackSize = 1;
            inventoryBeeRing.setQueen(queenStack);

            int beeHealth = bee.getHealth();
            int maxBeeHealth = bee.getMaxHealth();
            inventoryBeeRing.setCurrentBeeHealth((beeHealth * 100) / maxBeeHealth);
            inventoryBeeRing.setCurrentBeeColour(bee.getGenome().getPrimary().getIconColour(0));
        }
    }

    private void tickQueen(EntityPlayer player, InventoryBeeRing inventoryBeeRing) {
        ItemStack queenStack = inventoryBeeRing.getQueen();
        IBee queen = BeeManager.beeRoot.getMember(queenStack);

        inventoryBeeRing.setCurrentBeeHealth((queen.getHealth() * 100) / queen.getMaxHealth());
        inventoryBeeRing.setCurrentBeeColour(queen.getGenome().getPrimary().getIconColour(0));

        IEffectData[][] effects = getPlayerEffects(player);
        int index = inventoryBeeRing.getRingSlotIndex();
        RingHousing housingLogic = new RingHousing(player, inventoryBeeRing);
        effects[index] = queen.doEffect(effects[index], housingLogic);
        if (player.worldObj.isRemote && player.worldObj.getWorldTime() % 5 == 0) {
            effects[index] = queen.doFX(effects[index], housingLogic);
        }
        setPlayerEffects(player, effects);

        int throttle = inventoryBeeRing.getThrottle();
        if (throttle > 550) {
            inventoryBeeRing.setThrottle(0);
            queen.age(player.worldObj, 0.26f);

            if (queen.getHealth() == 0) {
                inventoryBeeRing.setQueen(null);
                inventoryBeeRing.setCurrentBeeHealth(0);
                inventoryBeeRing.setCurrentBeeColour(0x0ffffff);
            } else {
                queen.writeToNBT(queenStack.getTagCompound());
                inventoryBeeRing.setQueen(queenStack);
            }
        } else {
            inventoryBeeRing.setThrottle(throttle + 1);
        }
    }

    private static IEffectData[][] getPlayerEffects(EntityPlayer player) {
        UUID id = player.getUniqueID();
        return PLAYER_EFFECTS.containsKey(id) ? PLAYER_EFFECTS.get(id)
                : new IEffectData[][] { new IEffectData[2], new IEffectData[2] };
    }

    private static void setPlayerEffects(EntityPlayer player, IEffectData[][] effects) {
        PLAYER_EFFECTS.put(player.getUniqueID(), effects);
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBase;
            InventoryBeeRing inventoryBeeRing = new InventoryBeeRing(itemStack, player);
            InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
            for (int i = 0; i < baubles.getSizeInventory(); i++) {
                if (baubles.getStackInSlot(i) == itemStack) {
                    inventoryBeeRing.setRingSlotIndex(i == 1 ? 0 : 1);
                    break;
                }
            }
        }
    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {}

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }
}
