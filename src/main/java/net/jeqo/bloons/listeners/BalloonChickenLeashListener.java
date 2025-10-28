package net.jeqo.bloons.listeners;

import net.jeqo.bloons.Bloons;
import net.jeqo.bloons.balloon.single.SingleBalloon;
import net.jeqo.bloons.configuration.BalloonConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

/**
 * A class that listens for events related to balloon unleashing and leashing
 */
public class BalloonChickenLeashListener implements Listener {

    /**
     *              Used to check if player tries to unleash from their balloon, if they do then cancel it
     * @param event The event that is called when a player leashes an entity
     */
    @EventHandler
    public void onLeash(PlayerLeashEntityEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }

    /**
     *              Prevents unleashing of chicken with the internal ID
     * @param event The event that is called when a player unleashes an entity, type org.bukkit.event.entity.PlayerUnleashEntityEvent
     */
    @EventHandler
    public void onUnleash(PlayerUnleashEntityEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent event) {
        if (event.getEntity().getCustomName() == null || !event.getEntity().getCustomName().contains(BalloonConfiguration.BALLOON_CHICKEN_ID)) {
            return;
        }

        if (!(event.getEntity() instanceof org.bukkit.entity.LivingEntity living)) {
            return;
        }

        org.bukkit.entity.Entity holder = living.getLeashHolder();

        org.bukkit.Bukkit.getScheduler().runTask(Bloons.getInstance(), () -> {
            java.util.Optional<org.bukkit.entity.Item> lead = living.getNearbyEntities(15.0, 15.0, 15.0).stream()
                    .filter(e -> e instanceof org.bukkit.entity.Item)
                    .map(e -> (org.bukkit.entity.Item) e)
                    .filter(i -> i.getItemStack().getType() == org.bukkit.Material.LEAD)
                    .findFirst();

            lead.ifPresent(org.bukkit.entity.Item::remove);

            if (holder.isValid()) {
                try {
                    living.teleport(holder.getLocation());
                    living.setLeashHolder(holder);
                } catch (Exception ignored) {}
                return;
            }

            java.util.Optional<java.util.Map.Entry<java.util.UUID, SingleBalloon>> ownerEntry = Bloons.getPlayerSingleBalloons().entrySet().stream()
                    .filter(e -> e.getValue() != null && e.getValue().getChicken() == living)
                    .findFirst();

            if (ownerEntry.isPresent()) {
                org.bukkit.entity.Player owner = org.bukkit.Bukkit.getPlayer(ownerEntry.get().getKey());
                if (owner != null && owner.isOnline()) {
                    try {
                        living.teleport(owner.getLocation());
                        living.setLeashHolder(owner);
                    } catch (Exception ignored) {}
                }
            }
        });
    }
}
