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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.cubeville.cvflags.flags.*;

import java.lang.reflect.Field;

public final class CVFlags extends JavaPlugin implements Listener {

        private static CVFlags instance;

        public static boolean isFlagTrue(StateFlag flag, Player player) {
                return isFlagTrue(flag, player, player.getLocation());
        }

        public static boolean isFlagTrue(StateFlag flag, Player player, Location location) {
                RegionQuery query = getRegionQuery();
                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                return query.testState(BukkitAdapter.adapt(location), localPlayer, flag);
        }

        public static Object getFlagValue(Flag flag, Player player) {
                return getFlagValue(flag, player, player.getLocation());
        }

        public static Object getFlagValue(Flag flag, Player player, Location location) {
                RegionQuery query = getRegionQuery();
                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                return query.queryAllValues(BukkitAdapter.adapt(location), localPlayer, flag);
        }

        public static RegionQuery getRegionQuery() {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                return container.createQuery();
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
                instance = this;
                this.getCommand("ps").setExecutor(new PCheckFlag());
                getServer().getPluginManager().registerEvents(new DropperFlag(), this);
                getServer().getPluginManager().registerEvents(new ElytraPVPFlag(), this);
                getServer().getPluginManager().registerEvents(new EnderChestFlag(), this);
                getServer().getPluginManager().registerEvents(new LocalDeathMessageFlag(), this);
                getServer().getPluginManager().registerEvents(new DismountFlag(), this);
                getServer().getPluginManager().registerEvents(new ExplosionDamageFlag(), this);
        }

        public static CVFlags getInstance() {
                return instance;
        }
}
