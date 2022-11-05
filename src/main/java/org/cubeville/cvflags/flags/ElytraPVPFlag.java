package org.cubeville.cvflags.flags;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

import java.util.HashMap;
import java.util.Map;

public class ElytraPVPFlag implements Listener {

    private final int NON_GLIDING_DURATION = 10;

    private final Map<Player, BukkitTask> elytraCountdown = new HashMap<>();

    // This has some more complicated logic to prevent players from
    // going back and forth between gliding and not gliding
    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!CVFlags.isFlagTrue(Flags.ELYTRA_PVP, player)) return;
        // if they toggle from not gliding -> gliding
        if (event.isGliding()) {
            if (!elytraCountdown.containsKey(player)) return;
            elytraCountdown.get(player).cancel();
            player.sendMessage("§bYou are back in the air!");
            elytraCountdown.remove(player);
        } else {
            if (elytraCountdown.containsKey(player)) return;
            // if they toggle from gliding -> non gliding

            BukkitTask task = new BukkitRunnable() {
                int counter = NON_GLIDING_DURATION;
                @Override
                public void run() {
                    if (!CVFlags.isFlagTrue(Flags.ELYTRA_PVP, player) || player.isDead()) {
                        this.cancel();
                        elytraCountdown.remove(player);
                    } else if (counter > 0) {
                        if (counter % 5 == 0 || counter < 5) {
                            player.sendMessage("§cYou must be flying in the air in " + counter + " seconds!");
                            player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_OFF, 3.0F, 1.2F);
                        }
                        counter--;
                    } else {
                        player.setHealth(0);
                        player.sendMessage("§cYou were killed because you didn't fly again after " + NON_GLIDING_DURATION + " seconds of being on the ground!");
                        this.cancel();
                        elytraCountdown.remove(player);
                    }
                }
            }.runTaskTimer(CVFlags.getInstance(), 0L, 20L);
            elytraCountdown.put(player, task);
        }
    }
}
