package org.cubeville.cvflags.flags;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;
import org.spigotmc.event.entity.EntityDismountEvent;

public class DismountFlag implements Listener {

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        // continue if the entity dismounting is a player
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        // continue if the dismount flag is set to false
        if (CVFlags.isFlagTrue(Flags.DISMOUNT, player, player.getLocation())) return;
        // cancel the event!
        event.setCancelled(true);
    }

}
