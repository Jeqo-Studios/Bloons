package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloonBuilder;
import net.jeqo.bloons.balloon.multipart.MultipartBalloonType;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonEquipEvent;
import net.jeqo.bloons.events.balloon.multipart.MultipartBalloonUnequipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonEquipEvent;
import net.jeqo.bloons.events.balloon.single.SingleBalloonUnequipEvent;
import net.jeqo.bloons.gui.menus.BalloonMenu;
import net.jeqo.bloons.utils.*;
import net.jeqo.bloons.utils.management.MultipartBalloonManagement;
import net.jeqo.bloons.utils.management.SingleBalloonManagement;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BalloonMenuListener implements Listener {

    /**
     *              When a player interacts with the GUI menu, do the action required accordingly
     * @param event The event that is called when a player interacts with the GUI menu, type org.bukkit.event.inventory.InventoryClickEvent
     */
    @EventHandler
    public void onClick(InventoryClickEvent event){
        MessageTranslations messageTranslations = new MessageTranslations(Bloons.getInstance());

        if (!event.getView().getTitle().equals(ColorManagement.fromHex(messageTranslations.getString("menu-title")))) return;
        if(!(event.getWhoClicked() instanceof Player player)) return;
        if(!BalloonMenu.getUsers().containsKey(player.getUniqueId())) return;

        // The users inside the GUI
        BalloonMenu inventory = BalloonMenu.getUsers().get(player.getUniqueId());

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

            // Spawn the proper type of balloon (either single or multipart)
            MultipartBalloonType type = Bloons.getBalloonCore().getMultipartBalloonByID(localizedName);
            MultipartBalloon previousBalloon = MultipartBalloonManagement.getPlayerBalloon(player.getUniqueId());
            if (previousBalloon != null) {
                MultipartBalloonUnequipEvent multipartBalloonEquipEvent = new MultipartBalloonUnequipEvent(player, previousBalloon);
                multipartBalloonEquipEvent.callEvent();

                if (multipartBalloonEquipEvent.isCancelled()) return;

                previousBalloon.destroy();
                MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());
            }
            if (type != null) {
                MultipartBalloonBuilder builder = new MultipartBalloonBuilder(type, player);
                SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
                MultipartBalloon balloon = builder.build();

                // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
                MultipartBalloonEquipEvent multipartBalloonEquipEvent = new MultipartBalloonEquipEvent(player);
                multipartBalloonEquipEvent.callEvent();

                if (multipartBalloonEquipEvent.isCancelled()) return;

                balloon.initialize();
                balloon.run();

                MultipartBalloonManagement.setPlayerBalloon(player.getUniqueId(), balloon);
            } else {
                // Call the equip event and check if it's cancelled, if it is, don't spawn the balloon or do anything
                SingleBalloonEquipEvent singleBalloonEquipEvent = new SingleBalloonEquipEvent(player);
                singleBalloonEquipEvent.callEvent();

                if (singleBalloonEquipEvent.isCancelled()) return;

                // Check if a balloon needs to be added or removed
                SingleBalloonManagement.removeBalloon(player, Bloons.getPlayerSingleBalloons().get(player.getUniqueId()));
                SingleBalloon.checkBalloonRemovalOrAdd(player, localizedName);
            }

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

            if (inventory.getCurrentPageIndex() >= inventory.getPages().size()-1) {
                // If they're at the last page, play error sound
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            } else {
                // If they're within page bounds that can change, go to the next page
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inventory.currentPageIndex += 1;
                player.openInventory(inventory.getPages().get(inventory.getCurrentPageIndex()));
            }
        }

        /* Previous page functionality **/
        else if(displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.previous-page.name")))) {
            event.setCancelled(true);

            if (inventory.getCurrentPageIndex() > 0) {
                // If they're within page bounds that can change, go to the previous page
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
                inventory.currentPageIndex -= 1;
                player.openInventory(inventory.getPages().get(inventory.getCurrentPageIndex()));
            } else {
                // If there's no pages to go to, play error sound
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
            }

        }

        /* Unequip button functionality **/
        else if(displayName.equals(ColorCodeConverter.adventureToColorCode(messageTranslations.getString("buttons.unequip.name")))) {
            event.setCancelled(true);

            if (!event.isShiftClick()) {
                SingleBalloon singleBalloon = Bloons.getPlayerSingleBalloons().get(player.getUniqueId());
                MultipartBalloon multipartBalloon = Bloons.getPlayerMultipartBalloons().get(player.getUniqueId());

                if (singleBalloon == null && multipartBalloon == null) {
                    // If no balloon equipped, play sound and send message notifying them
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);
                    player.sendMessage(messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("not-equipped")));
                } else {
                    if (singleBalloon != null) {
                        if (messageTranslations.getString("close-on-unequip").equals("true")) player.closeInventory();

                        SingleBalloonUnequipEvent singleBalloonUnequipEvent = new SingleBalloonUnequipEvent(player, singleBalloon);
                        singleBalloonUnequipEvent.callEvent();

                        if (singleBalloonUnequipEvent.isCancelled()) return;

                        SingleBalloonManagement.removeBalloon(player, singleBalloon);

                        // Play sound and send message saying the balloon is unequipped
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                        player.sendMessage(messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("unequipped")));
                    }

                    if (multipartBalloon != null) {
                        if (messageTranslations.getString("close-on-unequip").equals("true")) player.closeInventory();

                        MultipartBalloonUnequipEvent multipartBalloonEquipEvent = new MultipartBalloonUnequipEvent(player, multipartBalloon);
                        multipartBalloonEquipEvent.callEvent();

                        if (multipartBalloonEquipEvent.isCancelled()) return;

                        multipartBalloon.destroy();
                        MultipartBalloonManagement.removePlayerBalloon(player.getUniqueId());

                        // Play sound and send message saying the balloon is unequipped
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1);
                        player.sendMessage(messageTranslations.getSerializedString(messageTranslations.getMessage("prefix"), messageTranslations.getMessage("unequipped")));
                    }
                }
            }

        } else {
            event.setCancelled(true);
        }
    }
}