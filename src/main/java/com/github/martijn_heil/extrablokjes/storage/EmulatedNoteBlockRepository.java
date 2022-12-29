package com.github.martijn_heil.extrablokjes.storage;

import org.bukkit.Location;

import java.util.Map;
import java.util.Optional;

public interface EmulatedNoteBlockRepository {
	void destroyAll();
	void destroy(BlockLocation location);
	void create(BlockLocation location, EmulatedNoteBlock noteBlock);
	void update(BlockLocation location, EmulatedNoteBlock noteBlock);
	Optional<EmulatedNoteBlock> forBlockLocation(BlockLocation location);
	Optional<EmulatedNoteBlock> forLocation(Location location);
	Map<BlockLocation, EmulatedNoteBlock> all();
	boolean exists(BlockLocation location);
	boolean exists(Location location);
	int count();
	void saveAllDirty() throws Exception;
	void saveAll() throws Exception;
}
