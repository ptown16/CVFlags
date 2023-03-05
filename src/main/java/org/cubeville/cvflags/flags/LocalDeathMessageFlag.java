package org.cubeville.cvflags.flags;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.cubeville.cvflags.CVFlags.getRegionQuery;

public class LocalDeathMessageFlag implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!CVFlags.isFlagTrue(Flags.LOCAL_DEATH_MESSAGE, event.getEntity())) return;
        List<Player> hasSentDeathTo = new ArrayList<>();
        String deathMessage = event.getDeathMessage();
        event.setDeathMessage(null);

        Location playerLocation = event.getEntity().getLocation();

        RegionQuery query = getRegionQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(playerLocation));
        set.forEach(protectedRegion -> {
            if (Objects.equals(protectedRegion.getFlag(Flags.LOCAL_DEATH_MESSAGE), StateFlag.State.ALLOW)) {
                for (Player player : Objects.requireNonNull(playerLocation.getWorld()).getPlayers()) {
                    if (!hasSentDeathTo.contains(player) && protectedRegion.contains(
                        player.getLocation().getBlockX(),
                        player.getLocation().getBlockY(),
                        player.getLocation().getBlockZ()
                    )) {
                        hasSentDeathTo.add(player);
                        player.sendMessage(deathMessage);
                        System.out.println("[RUN LOCAL DEATH]");
                    }
                }
            }
        });
    }
}
