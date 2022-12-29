package com.github.martijn_heil.extrablokjes.storage;

import org.bukkit.World;

public interface EmulatedNoteBlockRepositoryProvider {
	EmulatedNoteBlockRepository getNoteBlockRepository(World world);
}
