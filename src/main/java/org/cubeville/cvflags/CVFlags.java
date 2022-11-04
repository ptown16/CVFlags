package org.cubeville.cvflags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CVFlags extends JavaPlugin implements Listener {

    private final List<String> FLAG_NAMES = List.of(
            "dropper", // instakill if any fall dmg is taken and remove the death message
            "elytra-pvp"
    );

    private static final Map<String, StateFlag> CUSTOM_FLAGS = new HashMap<>();

    public static boolean shouldFlagApply(String flagName, Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return set.testState(localPlayer, CUSTOM_FLAGS.get(flagName));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // CODE TAKEN FROM WORLDGUARD API
        // https://worldguard.enginehub.org/en/latest/developer/regions/custom-flags/

        for (String flagName : FLAG_NAMES) {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            try {
                // create a new flag with flagName, defaulting to false
                StateFlag flag = new StateFlag(flagName, false);
                registry.register(flag);
                CUSTOM_FLAGS.put(flagName, flag);
            } catch (FlagConflictException e) {
                // some other plugin registered a flag by the same name already.
                // you can use the existing flag, but this may cause conflicts - be sure to check type
                Flag<?> existing = registry.get("my-custom-flag");
                if (existing instanceof StateFlag) {
                    CUSTOM_FLAGS.put(flagName, (StateFlag) existing);
                } else {
                    // types don't match - this is bad news! some other plugin conflicts with you
                    // hopefully this never actually happens
                }
            }
        }

        getServer().getPluginManager().registerEvents(new DropperFlag(), this);
        getServer().getPluginManager().registerEvents(new ElytraPVPFlag(), this);
    }
}
