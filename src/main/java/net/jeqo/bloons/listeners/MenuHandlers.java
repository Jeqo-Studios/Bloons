package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.gui.menus.BalloonMenu;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.ColorManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuHandlers implements Listener {

    /**
     * When a player interacts with the GUI menu, do the action required accordingly
     */
    @EventHandler
    public void onClick(InventoryClickEvent event){
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

        if (!event.getView().getTitle().equals(ColorManagement.fromHex(messageTranslations.getString("menu-title")))) return;
        if(!(event.getWhoClicked() instanceof Player player)) return;
        if(!BalloonMenu.users.containsKey(player.getUniqueId())) return;

        BalloonMenu inv = BalloonMenu.users.get(player.getUniqueId());

        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;

        int pageSize = messageTranslations.getInt("balloon-slots");
        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

        if (event.getRawSlot() <= pageSize) {
            if (displayName.equals(ColorManagement.fromHex(messageTranslations.getString("buttons.previous-page.name"))) || displayName.equals(ColorManagement.fromHex(messageTranslations.getString("buttons.next-page.name"))) || displayName.equals(ColorManagement.fromHex(messageTranslations.getString("buttons.unequip.name"))) ) {
                event.setCancelled(true);
            } else {
                if (event.isShiftClick()) {
                    event.setCancelled(true);
                } else {
                    BalloonManagement.removeBalloon(player, Bloons.playerBalloons.get(player.getUniqueId()));
                    String balloon = event.getCurrentItem().getItemMeta().getLocalizedName();
                    SingleBalloon.checkBalloonRemovalOrAdd(player, balloon);
                    player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
                    String balloonName = event.getCurrentItem().getItemMeta().getDisplayName();
                    player.sendMessage(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("equipped", balloonName));
                    if (messageTranslations.getString("close-on-equip").equals("true")) {
                        player.closeInventory();
                    }
                }
            }
        } else {
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }

        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorManagement.fromHex(messageTranslations.getString("buttons.next-page.name")))) {
            event.setCancelled(true);
            if (inv.currpage >= inv.pages.size()-1) {
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            } else {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inv.currpage += 1;
                player.openInventory(inv.pages.get(inv.currpage));
            }


        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorManagement.fromHex(messageTranslations.getString("buttons.previous-page.name")))) {
            event.setCancelled(true);
            if (inv.currpage > 0) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inv.currpage -= 1;
                player.openInventory(inv.pages.get(inv.currpage));
            } else {
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            }

        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ColorManagement.fromHex(messageTranslations.getString("buttons.unequip.name")))) {
            event.setCancelled(true);


            if (event.isShiftClick()) {
                event.setCancelled(true);
            } else {
                SingleBalloon balloonOwner1 = Bloons.playerBalloons.get(player.getUniqueId());
                if (balloonOwner1 == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
                    player.sendMessage(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("not-equipped"));
                } else {
                    if (messageTranslations.getString("close-on-unequip").equals("true")) {
                        player.closeInventory();
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                    player.sendMessage(messageTranslations.getMessage("prefix") + messageTranslations.getMessage("unequipped"));
                }
                BalloonManagement.removeBalloon(player, balloonOwner1);
            }
        } else {
            event.setCancelled(true);
        }
    }
}