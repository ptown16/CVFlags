package org.cubeville.cvflags.flags;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

public class ExplosionDamageFlag implements Listener {
    @EventHandler
        public void onPlayerDamage(EntityDamageEvent event) {
        // continue if the entity being damaged is a player
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        // continue if the cause is an explosion of some sort
        if (event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
        // continue if the explosion damage flag is set to false
        if (CVFlags.isFlagTrue(Flags.EXPLOSION_DAMAGE, player, player.getLocation())) return;
        // cancel the event!
        event.setCancelled(true);
    }
}
