package net.jeqo.bloons.listeners;

import jdk.jshell.execution.Util;
import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.BalloonRunner;
import net.jeqo.bloons.data.ScrollerInventory;
import net.jeqo.bloons.data.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuHandlers implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player p = (Player) event.getWhoClicked();
        if(!ScrollerInventory.users.containsKey(p.getUniqueId())) return;
        event.setCancelled(true);
        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        event.setCancelled(true);

        if (event.getSlot() < 45) {
            event.setCancelled(true);
            Utils.removeBalloon(p, (BalloonRunner) Bloons.playerBalloons.get(p.getUniqueId()));
            Player player = (Player) event.getWhoClicked();
            String balloon = event.getCurrentItem().getItemMeta().getLocalizedName();


            Utils.checkBalloonRemovalOrAdd(player, balloon);
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
            String balloonName = event.getCurrentItem().getItemMeta().getDisplayName();
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName));
            player.closeInventory();
        }

        /*if (event.getSlot() < 45) {
            event.setCancelled(true);
            Utils.removeBalloon(p, (BalloonRunner) Bloons.playerBalloons.get(p.getUniqueId()));
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            String balloon = event.getCurrentItem().getItemMeta().getDisplayName();
            Utils.checkBalloonRemovalOrAdd(player, balloon);
            player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloon));
            player.closeInventory();
        }*/

        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.nextPageName)) {
            event.setCancelled(true);
            if (inv.currpage >= inv.pages.size()-1) {
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1);
            } else {
                inv.currpage += 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }


        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.previousPageName)) {
            event.setCancelled(true);
            if (inv.currpage > 0) {
                inv.currpage -= 1;
                p.openInventory(inv.pages.get(inv.currpage));
            } else {
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1);
            }

        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.removeName)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            BalloonRunner balloonRunner1 = (BalloonRunner) Bloons.playerBalloons.get(player.getUniqueId());
            if (balloonRunner1 == null) {
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
            } else {
                player.closeInventory();
                player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
            }
            Utils.removeBalloon(player, balloonRunner1);
        } else {
            event.setCancelled(true);
        }
    }
}