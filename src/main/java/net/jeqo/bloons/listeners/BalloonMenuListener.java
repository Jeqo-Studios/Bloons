package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.SingleBalloon;
import net.jeqo.bloons.gui.menus.BalloonMenu;
import net.jeqo.bloons.utils.BalloonManagement;
import net.jeqo.bloons.utils.ColorCodeConverter;
import net.jeqo.bloons.utils.ColorManagement;
import net.jeqo.bloons.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BalloonMenuListener implements Listener {

    /**
     * When a player interacts with the GUI menu, do the action required accordingly
     */
    @EventHandler
    public void onClick(InventoryClickEvent event){
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

        if (!event.getView().getTitle().equals(ColorManagement.fromHex(messageTranslations.getString("menu-title")))) return;
        if(!(event.getWhoClicked() instanceof Player player)) return;
        if(!BalloonMenu.users.containsKey(player.getUniqueId())) return;

        // The users inside the GUI
        BalloonMenu inventory = BalloonMenu.users.get(player.getUniqueId());

        // If the current item is null and has no meta, return
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;

        int pageSize = messageTranslations.getInt("balloon-slots");

        // Get the display name of the item clicked and the converted balloon name
        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
        String localizedName = event.getCurrentItem().getItemMeta().getLocalizedName();
        String convertedColourBalloonName = ColorCodeConverter.colorCodeToAdventure(displayName); // Weird parsing is needed for this because of the usage of minimessage

        // Always check for shift clicks
        if (event.isShiftClick()) event.setCancelled(true);

        if (event.getRawSlot() <= pageSize) {
            // Do checks on if they're using the other buttons
            if (displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.previous-page.name")))) event.setCancelled(true);
            if (displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.next-page.name")))) event.setCancelled(true);
            if (displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.unequip.name")))) event.setCancelled(true);

            // Check if a balloon needs to be added or removed
            BalloonManagement.removeBalloon(player, Bloons.getPlayerBalloons().get(player.getUniqueId()));
            SingleBalloon.checkBalloonRemovalOrAdd(player, localizedName);

            // Send equipped message and play sound
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
            Component equippedMessage = messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("equipped", convertedColourBalloonName));
            player.sendMessage(equippedMessage);

            // Close inventory if the config is set to true
            if (messageTranslations.getString("close-on-equip").equals("true")) player.closeInventory();
        } else {
            event.setCancelled(true);
        }

        /* Next page functionality **/
        if(displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.next-page.name")))) {
            event.setCancelled(true);

            if (inventory.currpage >= inventory.pages.size()-1) {
                // If they're at the last page, play error sound
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            } else {
                // If they're within page bounds that can change, go to the next page
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inventory.currpage += 1;
                player.openInventory(inventory.pages.get(inventory.currpage));
            }
        }

        /* Previous page functionality **/
        else if(displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.previous-page.name")))) {
            event.setCancelled(true);

            if (inventory.currpage > 0) {
                // If they're within page bounds that can change, go to the previous page
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inventory.currpage -= 1;
                player.openInventory(inventory.pages.get(inventory.currpage));
            } else {
                // If there's no pages to go to, play error sound
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            }

        }

        /* Unequip button functionality **/
        else if(displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.unequip.name")))) {
            event.setCancelled(true);

            if (!event.isShiftClick()) {
                SingleBalloon balloon = Bloons.getPlayerBalloons().get(player.getUniqueId());

                if (balloon == null) {
                    // If no balloon equipped, play sound and send message notifying them
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
                    player.sendMessage(messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("not-equipped")));
                } else {
                    if (messageTranslations.getString("close-on-unequip").equals("true")) player.closeInventory();

                    // Play sound and send message saying the balloon is unequipped
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                    player.sendMessage(messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("unequipped")));
                }

                // Remove the balloon
                BalloonManagement.removeBalloon(player, balloon);
            }

        } else {
            event.setCancelled(true);
        }
    }
}