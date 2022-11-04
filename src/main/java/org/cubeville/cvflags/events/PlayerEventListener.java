package org.cubeville.cvflags.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

public class PlayerEventListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!CVFlags.isFlagTrue(Flags.DROPPER, player)) return;
        event.setDeathMessage(null);
    }
}
