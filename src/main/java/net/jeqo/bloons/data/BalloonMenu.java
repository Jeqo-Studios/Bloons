package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class BalloonMenu {

    public ArrayList<Inventory> pages = new ArrayList<Inventory>();
    public UUID id;
    public int currpage = 0;
    public static HashMap<UUID, BalloonMenu> users = new HashMap<UUID, BalloonMenu>();
    public BalloonMenu(ArrayList<ItemStack> items, String name, Player p){
        this.id = UUID.randomUUID();
        Inventory page = getBlankPage(name);
        for(int i = 0;i < items.size(); i++){
            if(page.firstEmpty() == 45){
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





    /*public static final String previousPageName = ChatColor.GRAY + "ᴘʀᴇᴠɪᴏᴜs ᴘᴀɢᴇ";
    public static final String removeName = ChatColor.GRAY + "ᴜɴᴇǫᴜɪᴘ ʙᴀʟʟᴏᴏɴ";
    public static final String nextPageName = ChatColor.GRAY + "ɴᴇxᴛ ᴘᴀɢᴇ";*/

    private Inventory getBlankPage(String name){
        Inventory page = Bukkit.createInventory(null, 54, Utils.hex(name));

        ItemStack nextPage = new ItemStack(Material.valueOf(Bloons.getString("buttons.next-page.material")));
        ItemMeta nextMeta = nextPage.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName(Utils.hex(Bloons.getString("buttons.next-page.name")));;
        nextMeta.setCustomModelData(Bloons.getInt("custom-model-data"));
        nextPage.setItemMeta(nextMeta);

        page.setItem(51, nextPage);

        ItemStack prevPage = new ItemStack(Material.valueOf(Bloons.getString("buttons.previous-page.material")));
        ItemMeta prevMeta = prevPage.getItemMeta();
        assert prevMeta != null;
        prevMeta.setDisplayName(Utils.hex(Bloons.getString("buttons.previous-page.name")));;
        prevMeta.setCustomModelData(Bloons.getInt("custom-model-data"));
        prevPage.setItemMeta(prevMeta);


        page.setItem(47, prevPage);

        ItemStack removeBalloon = new ItemStack(Material.valueOf(Bloons.getString("buttons.unequip.material")));
        ItemMeta removeMeta = removeBalloon.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(Utils.hex(Bloons.getString("buttons.unequip.name")));;
        removeMeta.setCustomModelData(Bloons.getInt("custom-model-data"));
        removeBalloon.setItemMeta(removeMeta);


        page.setItem(49, removeBalloon);
        return page;
    }
}