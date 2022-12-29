package com.github.martijn_heil.extrablokjes;

import com.github.martijn_heil.extrablokjes.storage.BlockLocation;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Sound;

public class Conversions {

	public static Sound instrumentToSound(Instrument instrument) {
		throw new UnsupportedOperationException();
	}

	public static float notePitchToAbsolutePitch(byte pitch) {
		throw new UnsupportedOperationException();
	}

	public static BlockLocation locationToBlockLocation(Location location) {
		BlockLocation bLocation = new BlockLocation();
		bLocation.x = location.getBlockX();
		bLocation.y = (short) location.getBlockY();
		bLocation.z = location.getBlockZ();
		return bLocation;
	}
}
