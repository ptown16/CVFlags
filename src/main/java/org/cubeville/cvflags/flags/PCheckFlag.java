package org.cubeville.cvflags.flags;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cubeville.cvflags.CVFlags;
import org.cubeville.cvflags.Flags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PCheckFlag implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!(sender instanceof Player) || !command.getName().equalsIgnoreCase("ps")) return true;
                Player player = (Player) sender;
                if (!player.hasPermission("cvflags.pcheck")) {
                        player.sendMessage("§cYou do not have permission to run this command");
                        return true;
                }

                // okay now the real work begins
                Collection<String> flagResults = (Collection<String>) CVFlags.getFlagValue(Flags.PLAYER_CHECK, player);
                if (flagResults.size() == 0) {
                        player.sendMessage("§cYou must be standing in a region with a valid pcheck flag!");
                        return true;
                }
                String checkingRgName = flagResults.stream().findFirst().get();
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                ProtectedRegion region = Objects.requireNonNull(container.get(BukkitAdapter.adapt(player.getWorld()))).getRegion(checkingRgName);
                if (region == null) {
                        player.sendMessage("§cThe pcheck flag does not point to a valid region in this world (it points to " + checkingRgName + " instead)");
                        return true;
                }

                // loop over players and determine which ones are in the region
                List<String> playerNames = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.isFlying()) continue;
                        if (List.of(GameMode.CREATIVE, GameMode.SPECTATOR).contains(p.getGameMode())) continue;
                        if (player.equals(p) || !p.getWorld().equals(player.getWorld())) continue;
                        Location loc = BukkitAdapter.adapt(p.getLocation());
                        RegionQuery query = container.createQuery();
                        ApplicableRegionSet set = query.getApplicableRegions(loc);
                        for (ProtectedRegion rg : set) {
                                if (!rg.equals(region)) continue;
                                playerNames.add(p.getDisplayName());
                                break;
                        }
                }

                player.sendMessage("§bPlayers left: §e§l" + playerNames.size());
                if (playerNames.size() == 0) return true;
                StringBuilder usernameMsg = new StringBuilder("§3");
                for (int i = 0; i < playerNames.size(); i++) {
                        if (i == (playerNames.size() - 1) && i != 0) {
                                usernameMsg.append(" and ");
                        } else if (i != 0) {
                                usernameMsg.append(", ");
                        }
                        usernameMsg.append(playerNames.get(i));
                }
                player.sendMessage(usernameMsg.toString());
                return true;
        }
}
