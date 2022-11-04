package org.cubeville.cvflags.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

public class EntityEventListener implements Listener {
    @EventHandler
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        if(event.getCause() != org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) return;
        if(event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!CVFlags.isFlagTrue(Flags.DROPPER, player)) return;
        player.setHealth(0);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
        //If the player is trying to stop gliding in an elytra pvp arena, cancel that event
        if (event.isGliding()) return;
        if(event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!CVFlags.isFlagTrue(Flags.FORCE_ELYTRA_GLIDE, player)) return;
        event.setCancelled(true);
    }
}
