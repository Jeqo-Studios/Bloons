package net.jeqo.bloons.events.balloon.multipart;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.bloons.balloon.multipart.balloon.MultipartBalloon;
import net.jeqo.bloons.events.BloonsEvent;
import org.bukkit.entity.Player;

@Getter
@Setter
public class MultipartBalloonStoreEvent extends BloonsEvent {
    private Player player;
    private MultipartBalloon balloon;

    public MultipartBalloonStoreEvent(Player player, MultipartBalloon balloon) {
        this.player = player;
        this.balloon = balloon;
    }
}
