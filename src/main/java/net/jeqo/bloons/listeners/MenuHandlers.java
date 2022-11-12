package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.BalloonRunner;
import net.jeqo.bloons.data.ScrollerInventory;
import net.jeqo.bloons.data.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class MenuHandlers implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player p = (Player) event.getWhoClicked();
        if(!ScrollerInventory.users.containsKey(p.getUniqueId())) return;
        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;

        if (event.getSlot() < 45) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            String balloon = event.getCurrentItem().getItemMeta().getDisplayName();
            Utils.checkBalloonRemovalOrAdd(player, balloon);
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloon));
            player.closeInventory();
        }

        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.nextPageName)) {
            event.setCancelled(true);
            if (inv.currpage >= inv.pages.size()-1) {
                return;
            } else {
                inv.currpage += 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }


        }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.previousPageName)) {
            event.setCancelled(true);
            if (inv.currpage > 0) {
                inv.currpage -= 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }

        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.removeName)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            BalloonRunner balloonRunner1 = (BalloonRunner) Bloons.playerBalloons.get(player.getUniqueId());
            if (balloonRunner1 == null) {
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
            } else {
                player.closeInventory();
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
            }
            Utils.removeBalloon(player, balloonRunner1);
        } else {
            event.setCancelled(true);
        }
    }
}