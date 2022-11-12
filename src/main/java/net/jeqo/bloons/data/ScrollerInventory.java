package net.jeqo.bloons.data;

import net.jeqo.bloons.Bloons;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class ScrollerInventory{

    public ArrayList<Inventory> pages = new ArrayList<Inventory>();
    public UUID id;
    public int currpage = 0;
    public static HashMap<UUID, ScrollerInventory> users = new HashMap<UUID, ScrollerInventory>();
    public ScrollerInventory(ArrayList<ItemStack> items, String name, Player p){
        this.id = UUID.randomUUID();
        Inventory page = getBlankPage(name);
        for(int i = 0;i < items.size(); i++){
            if(page.firstEmpty() == 46){
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





    public static final String removeName = ChatColor.GRAY + "Unequip Balloon";
    public static final String nextPageName = ChatColor.GRAY + "Next Page";
    public static final String previousPageName = ChatColor.GRAY + "Previous Page";
    private Inventory getBlankPage(String name){
        Inventory page = Bukkit.createInventory(null, 54, name);

        ItemStack nextpage =  new ItemStack(Material.ARROW, 1, (byte) 5);
        ItemMeta nextMeta = nextpage.getItemMeta();
        assert nextMeta != null;
        nextMeta.setDisplayName(nextPageName);;
        nextpage.setItemMeta(nextMeta);

        page.setItem(53, nextpage);

        ItemStack prevpage = new ItemStack(Material.ARROW, 1, (byte) 2);
        ItemMeta prevMeta = prevpage.getItemMeta();
        assert prevMeta != null;
        prevMeta.setDisplayName(previousPageName);;
        prevpage.setItemMeta(prevMeta);


        page.setItem(45, prevpage);

        ItemStack removeBalloon = new ItemStack(Material.LAVA_BUCKET, 1, (byte) 2);
        ItemMeta removeMeta = prevpage.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(removeName);;
        removeBalloon.setItemMeta(removeMeta);


        page.setItem(49, removeBalloon);
        return page;
    }
}