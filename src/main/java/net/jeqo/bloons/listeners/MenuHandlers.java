package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.data.BalloonMenu;
import net.jeqo.bloons.data.BalloonOwner;
import net.jeqo.bloons.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuHandlers implements Listener {

    @EventHandler
    /**
     * When a player interacts with the GUI menu, do the action required accordingly
     */
    public void onClick(InventoryClickEvent e){
        if (!e.getView().getTitle().equals(Utils.hex(Bloons.getString("menu-title")))) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if(!BalloonMenu.users.containsKey(p.getUniqueId())) return;
        BalloonMenu inv = BalloonMenu.users.get(p.getUniqueId());
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;

        int pageSize = Bloons.getInt("balloon-slots");

        if (e.getRawSlot() <= pageSize) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.hex(Bloons.getString("buttons.previous-page.name"))) || e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.hex(Bloons.getString("buttons.next-page.name"))) || e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.hex(Bloons.getString("buttons.unequip.name"))) ) {
                e.setCancelled(true);
            } else {
                if (e.isShiftClick()) {
                    e.setCancelled(true);
                } else {
                    Utils.removeBalloon(p, Bloons.playerBalloons.get(p.getUniqueId()));
                    Player player = (Player) e.getWhoClicked();
                    String balloon = e.getCurrentItem().getItemMeta().getLocalizedName();
                    BalloonOwner.checkBalloonRemovalOrAdd(player, balloon);
                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                    String balloonName = e.getCurrentItem().getItemMeta().getDisplayName();
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("equipped", balloonName));
                    if (Bloons.getString("close-on-equip").equals("true")) {
                        player.closeInventory();
                    }
                }
            }
        } else {
            if (e.isShiftClick()) {
                e.setCancelled(true);
            }
            e.setCancelled(true);
        }

        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.hex(Bloons.getString("buttons.next-page.name")))) {
            e.setCancelled(true);
            if (inv.currpage >= inv.pages.size()-1) {
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            } else {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inv.currpage += 1;
                p.openInventory(inv.pages.get(inv.currpage));
            }


        } else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.hex(Bloons.getString("buttons.previous-page.name")))) {
            e.setCancelled(true);
            if (inv.currpage > 0) {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inv.currpage -= 1;
                p.openInventory(inv.pages.get(inv.currpage));
            } else {
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            }

        } else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(Utils.hex(Bloons.getString("buttons.unequip.name")))) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();


            if (e.isShiftClick()) {
                e.setCancelled(true);
            } else {
                BalloonOwner balloonOwner1 = Bloons.playerBalloons.get(player.getUniqueId());
                if (balloonOwner1 == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("not-equipped"));
                } else {
                    if (Bloons.getString("close-on-unequip").equals("true")) {
                        player.closeInventory();
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                    player.sendMessage(Bloons.getMessage("prefix") + Bloons.getMessage("unequipped"));
                }
                Utils.removeBalloon(player, balloonOwner1);
            }
        } else {
            e.setCancelled(true);
        }
    }
}