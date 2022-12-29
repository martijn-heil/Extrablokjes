package com.github.martijn_heil.extrablokjes.listeners;

import com.github.martijn_heil.extrablokjes.storage.EmulatedNoteBlockRepositoryProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ExtrablokjesListener implements Listener {
	private EmulatedNoteBlockRepositoryProvider repoProvider;

	public ExtrablokjesListener(EmulatedNoteBlockRepositoryProvider repoProvider) {
		this.repoProvider = repoProvider;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		int count = repoProvider.getNoteBlockRepository(e.getPlayer().getWorld()).count();
		e.getPlayer().sendMessage("There are " + count + " locations stored in your world!");
	}
}
