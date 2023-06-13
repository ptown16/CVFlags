package org.cubeville.cvflags.flags;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.UUID;

public class DismountFlag implements Listener {

    // BECAUSE PROBLEMS, THIS CODE IS NOT CURRENTLY IMPLEMENTED.
    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        // continue if the entity dismounting is a player
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        // continue if the dismount flag is set to false
        // if (CVFlags.isFlagTrue(Flags.DISMOUNT, player, player.getLocation())) return;
        // cancel the event!
        event.setCancelled(true);
        // add the player back in 1 tick
        Entity dismounted = event.getDismounted();
        UUID pUUID = player.getUniqueId();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(CVFlags.getInstance(), () -> {
            Player p = Bukkit.getPlayer(pUUID);
            if (p != null && p.isOnline()) {
                dismounted.addPassenger(p);
            }
        }, 1L);
    }


}
