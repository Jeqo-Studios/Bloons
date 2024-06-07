package net.jeqo.bloons.gui.menus;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.logger.Logger;
import net.jeqo.bloons.utils.ColorManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class BalloonMenu {

    public ArrayList<Inventory> pages = new ArrayList<>();
    public UUID id;
    public int currpage = 0;
    public static HashMap<UUID, BalloonMenu> users = new HashMap<>();
    MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());
    private Inventory getBlankPage(String name){
        int pageSize = messageTranslations.getInt("menu-size");
        Inventory page = Bukkit.createInventory(null, pageSize, ColorManagement.fromHex(name));

        ItemStack nextPage = new ItemStack(Material.valueOf(messageTranslations.getString("buttons.next-page.material")));
        ItemMeta nextMeta = nextPage.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName(ColorManagement.fromHex(messageTranslations.getString("buttons.next-page.name")));;
        nextMeta.setCustomModelData(messageTranslations.getInt("buttons.next-page.custom-model-data"));
        nextPage.setItemMeta(nextMeta);

        ItemStack prevPage = new ItemStack(Material.valueOf(messageTranslations.getString("buttons.previous-page.material")));
        ItemMeta prevMeta = prevPage.getItemMeta();
        assert prevMeta != null;
        prevMeta.setDisplayName(ColorManagement.fromHex(messageTranslations.getString("buttons.previous-page.name")));;
        prevMeta.setCustomModelData(messageTranslations.getInt("buttons.previous-page.custom-model-data"));
        prevPage.setItemMeta(prevMeta);

        ItemStack removeBalloon = new ItemStack(Material.valueOf(messageTranslations.getString("buttons.unequip.material")));
        ItemMeta removeMeta = removeBalloon.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(ColorManagement.fromHex(messageTranslations.getString("buttons.unequip.name")));;
        removeMeta.setCustomModelData(messageTranslations.getInt("buttons.unequip.custom-model-data"));
        removeBalloon.setItemMeta(removeMeta);

        List<String> previousPageSlots = Bloons.getInstance().getConfig().getStringList("buttons.previous-page.slots");
        for (String previousPageSlot : previousPageSlots) {
            if (Integer.parseInt(previousPageSlot) < pageSize) {
                page.setItem(Integer.parseInt(previousPageSlot), prevPage);
            } else {
                Logger.logWarning("Previous page button slot(s) out of bounds!");
            }
        }

        List<String> unequipSlots = Bloons.getInstance().getConfig().getStringList("buttons.unequip.slots");
        for (String unequipSlot : unequipSlots) {
            if (Integer.parseInt(unequipSlot) < pageSize) {
                page.setItem(Integer.parseInt(unequipSlot), removeBalloon);
            } else {
                Logger.logWarning("Unequip button slot(s) out of bounds!");
            }
        }

        List<String> nextPageSlots = Bloons.getInstance().getConfig().getStringList("buttons.next-page.slots");
        for (String nextPageSlot : nextPageSlots) {
            if (Integer.parseInt(nextPageSlot) < pageSize) {
                page.setItem(Integer.parseInt(nextPageSlot), nextPage);
            } else {
                Logger.logWarning("Next page button slot(s) out of bounds!");
            }
        }
        return page;
    }

    public BalloonMenu(ArrayList<ItemStack> items, String name, Player p){
        this.id = UUID.randomUUID();
        Inventory page = getBlankPage(name);
        int slot = 0;
        for (int i = -1; i < items.size(); i++) {
            if (slot == 0 || slot == 8 || slot == 9 || slot == 17 || slot == 18 || slot == 26 || slot == 27 || slot == 35 || slot == 36 || slot == 44) {
                slot++;
            } else {
                page.setItem(slot, items.get(i));
                slot++;
            }
            if (slot == messageTranslations.getInt("balloon-slots")-1) {
                pages.add(page);
                page = getBlankPage(name);
                slot = 0;
            }
        }

        pages.add(page);
        p.openInventory(pages.get(currpage));
        users.put(p.getUniqueId(), this);
    }
}