package xyz.epicebic.ebiclib.util;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class EventUtils {
    private EventUtils() {
    }

    public static boolean isRightInteraction(@NotNull Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    public static boolean isLeftInteraction(@NotNull Action action) {
        return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    }
}
