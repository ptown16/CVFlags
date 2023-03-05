package org.cubeville.cvflags.flags;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

public class DropperFlag implements Listener {
        @EventHandler
        public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {
                if(event.getCause() != org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) return;
                if(event.getEntityType() != EntityType.PLAYER) return;
                Player player = (Player) event.getEntity();
                if (!CVFlags.isFlagTrue(Flags.DROPPER, player)) return;
                player.setHealth(0);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
                Player player = event.getEntity();
                if (!CVFlags.isFlagTrue(Flags.DROPPER, player)) return;
                event.setDeathMessage(null);
        }
}
