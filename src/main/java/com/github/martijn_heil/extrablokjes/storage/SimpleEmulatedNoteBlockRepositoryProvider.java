package com.github.martijn_heil.extrablokjes.storage;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class SimpleEmulatedNoteBlockRepositoryProvider implements EmulatedNoteBlockRepositoryProvider {
	private final HashMap<UUID, EmulatedNoteBlockRepository> map = new HashMap<>();
	private final Function<World, EmulatedNoteBlockRepository> producer;

	public SimpleEmulatedNoteBlockRepositoryProvider(Server server, Function<World, EmulatedNoteBlockRepository> producer) {
		this.producer = producer;

		for (World world : server.getWorlds()) {
			this.loadWorld(world);
		}
	}

	@Override
	public EmulatedNoteBlockRepository getNoteBlockRepository(World world) {
		return null;
	}

	public void loadWorld(World world) {
		this.map.put(world.getUID(), this.producer.apply(world));
	}

	public void unloadWorld(World world) throws Exception {
		EmulatedNoteBlockRepository repo = this.map.remove(world.getUID());
		if (repo == null) return;

		repo.saveAllDirty();
	}

	public static class Listener implements org.bukkit.event.Listener {
		private final SimpleEmulatedNoteBlockRepositoryProvider provider;

		public Listener(SimpleEmulatedNoteBlockRepositoryProvider provider) {
			this.provider = provider;
		}

		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onWorldLoad(WorldLoadEvent e) {
				provider.loadWorld(e.getWorld());
		}

		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onWorldUnload(WorldUnloadEvent e) {
			try {
				provider.unloadWorld(e.getWorld());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
