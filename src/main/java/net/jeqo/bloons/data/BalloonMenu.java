package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.utils.Utils;
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

    public ArrayList<Inventory> pages = new ArrayList<Inventory>();
    public UUID id;
    public int currpage = 0;
    public static HashMap<UUID, BalloonMenu> users = new HashMap<UUID, BalloonMenu>();
    private Inventory getBlankPage(String name){
        int pageSize = Bloons.getInt("menu-size");
        Inventory page = Bukkit.createInventory(null, pageSize, Utils.hex(name));

        ItemStack nextPage = new ItemStack(Material.valueOf(Bloons.getString("buttons.next-page.material")));
        ItemMeta nextMeta = nextPage.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName(Utils.hex(Bloons.getString("buttons.next-page.name")));;
        nextMeta.setCustomModelData(Bloons.getInt("buttons.next-page.custom-model-data"));
        nextPage.setItemMeta(nextMeta);

        ItemStack prevPage = new ItemStack(Material.valueOf(Bloons.getString("buttons.previous-page.material")));
        ItemMeta prevMeta = prevPage.getItemMeta();
        assert prevMeta != null;
        prevMeta.setDisplayName(Utils.hex(Bloons.getString("buttons.previous-page.name")));;
        prevMeta.setCustomModelData(Bloons.getInt("buttons.previous-page.custom-model-data"));
        prevPage.setItemMeta(prevMeta);

        ItemStack removeBalloon = new ItemStack(Material.valueOf(Bloons.getString("buttons.unequip.material")));
        ItemMeta removeMeta = removeBalloon.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(Utils.hex(Bloons.getString("buttons.unequip.name")));;
        removeMeta.setCustomModelData(Bloons.getInt("buttons.unequip.custom-model-data"));
        removeBalloon.setItemMeta(removeMeta);





        List<String> previousPageSlots = Bloons.getInstance().getConfig().getStringList("buttons.previous-page.slots");
        for (int i = 0; i < previousPageSlots.size(); i++) {
            if (Integer.parseInt(previousPageSlots.get(i)) < pageSize) {
                page.setItem(Integer.parseInt(previousPageSlots.get(i)), prevPage);
            } else {
                Utils.warn("Previous page button slot(s) out of bounds!");
            }
        }

        List<String> unequipSlots = Bloons.getInstance().getConfig().getStringList("buttons.unequip.slots");
        for (int i = 0; i < unequipSlots.size(); i++) {
            if (Integer.parseInt(unequipSlots.get(i)) < pageSize){
                page.setItem(Integer.parseInt(unequipSlots.get(i)), removeBalloon);
            } else {
                Utils.warn("Unequip button slot(s) out of bounds!");
            }
        }

        List<String> nextPageSlots = Bloons.getInstance().getConfig().getStringList("buttons.next-page.slots");
        for (int i = 0; i < nextPageSlots.size(); i++) {
            if (Integer.parseInt(nextPageSlots.get(i)) < pageSize) {
                page.setItem(Integer.parseInt(nextPageSlots.get(i)), nextPage);
            } else {
                Utils.warn("Next page button slot(s) out of bounds!");
            }
        }
        return page;
    }










    public BalloonMenu(ArrayList<ItemStack> items, String name, Player p){
        this.id = UUID.randomUUID();
        Inventory page = getBlankPage(name);
        for(int i = 0;i < items.size(); i++){
            if(page.firstEmpty() == Bloons.getInt("balloon-slots")){
                pages.add(page);
                page = getBlankPage(name);
                page.addItem(items.get(i));
            }else{
                page.addItem(items.get(i));
            }
        }
        pages.add(page);
        p.openInventory(pages.get(currpage));
        users.put(p.getUniqueId(), this);
    }
}