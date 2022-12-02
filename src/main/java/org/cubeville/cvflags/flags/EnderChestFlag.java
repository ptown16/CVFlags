package org.cubeville.cvflags.flags;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

import java.util.Objects;

public class EnderChestFlag implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // continue if the player is clicking on a block
        if (event.getClickedBlock() == null) return;
        // continue if the ender chest flag is set to false
        if (CVFlags.isFlagTrue(Flags.ENDER_CHEST, event.getPlayer(), event.getClickedBlock().getLocation())) return;
        // continue if the player is right clicking an ender chest
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) return;
        // cancel the event!
        event.setCancelled(true);
    }

}
