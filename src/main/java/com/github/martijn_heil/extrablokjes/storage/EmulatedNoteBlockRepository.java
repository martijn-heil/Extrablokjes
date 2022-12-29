package com.github.martijn_heil.extrablokjes.storage;

import org.bukkit.Location;

import java.util.Optional;

public interface EmulatedNoteBlockRepository {
	void destroy(BlockLocation location);
	void create(BlockLocation location, EmulatedNoteBlock noteBlock);
	void update(BlockLocation location, EmulatedNoteBlock noteBlock);
	Optional<EmulatedNoteBlock> forBlockLocation(BlockLocation location);
	Optional<EmulatedNoteBlock> forLocation(Location location);
	boolean exists(BlockLocation location);
	boolean exists(Location location);
	int count();
	void saveAllDirty();
	void saveAll();
}
