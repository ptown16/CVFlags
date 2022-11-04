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
import org.cubeville.cvflags.events.EntityEventListener;
import org.cubeville.cvflags.events.PlayerEventListener;

import java.lang.reflect.Field;

public final class CVFlags extends JavaPlugin implements Listener {

    public static boolean isFlagTrue(StateFlag flag, Player player) {
        ApplicableRegionSet set = getRegionSet(player);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return set.testState(localPlayer, flag);
    }

    public static Object getFlagValue(Flag flag, Player player) {
        ApplicableRegionSet set = getRegionSet(player);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return set.queryValue(localPlayer, flag);
    }

    private static ApplicableRegionSet getRegionSet(Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
    }

    @Override
    public void onLoad() {

        // CODE TAKEN FROM WORLDGUARD API
        // https://worldguard.enginehub.org/en/latest/developer/regions/custom-flags/

        for (Field field : Flags.class.getFields()) {
            System.out.println(field.getType());
            if (!(Flag.class.isAssignableFrom(field.getType()))) continue;
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            try {
                // create a new flag defined in flags
                registry.register((Flag<?>) field.get(null));
            } catch (FlagConflictException e) {
                // some other plugin registered a flag by the same name already, just let it go
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
    }
}
