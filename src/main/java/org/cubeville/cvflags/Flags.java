package org.cubeville.cvflags;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;

public final class Flags {
    public final static StateFlag DROPPER = new StateFlag("dropper", false);
    public final static StateFlag ELYTRA_PVP = new StateFlag("elytra-pvp", false);
    public final static StateFlag ENDER_CHEST = new StateFlag("ender-chest", true);
    public final static StringFlag PLAYER_CHECK = new StringFlag("pcheck");
}
