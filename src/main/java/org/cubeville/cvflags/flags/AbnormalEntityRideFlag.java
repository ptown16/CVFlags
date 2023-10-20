package org.cubeville.cvflags.flags;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

public class AbnormalEntityRideFlag implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // continue if the player is right clicking a living entity
        if (!(event.getRightClicked() instanceof LivingEntity)) return;
        if (!event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        // continue if the abnormal entity ride flag is set to true
        if (!CVFlags.isFlagTrue(Flags.ABNORMAL_ENTITY_RIDE, event.getPlayer(), event.getRightClicked().getLocation())) return;
        // mount the entity!
        event.getRightClicked().addPassenger(event.getPlayer());
    }
}
