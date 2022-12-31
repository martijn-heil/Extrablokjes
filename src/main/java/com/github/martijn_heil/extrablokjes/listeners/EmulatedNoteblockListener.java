package com.github.martijn_heil.extrablokjes.listeners;

import com.github.martijn_heil.extrablokjes.Conversions;
import com.github.martijn_heil.extrablokjes.storage.BlockLocation;
import com.github.martijn_heil.extrablokjes.storage.EmulatedNoteBlock;
import com.github.martijn_heil.extrablokjes.storage.EmulatedNoteBlockRepository;
import com.github.martijn_heil.extrablokjes.storage.EmulatedNoteBlockRepositoryProvider;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class EmulatedNoteblockListener implements Listener {
	private EmulatedNoteBlockRepositoryProvider repoProvider;

	public EmulatedNoteblockListener(EmulatedNoteBlockRepositoryProvider repoProvider) {
		this.repoProvider = repoProvider;
	}

//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//	public void onPlayerJoin(PlayerJoinEvent e) {
//		int count = repoProvider.getNoteBlockRepository(e.getPlayer().getWorld()).count();
//		e.getPlayer().sendMessage("There are " + count + " locations stored in your world!");

	private void playEmulatedNoteblock(World world, Location location, EmulatedNoteBlock emulatedNoteBlock) {
		world.playSound(
				location,
				Conversions.instrumentToSound(emulatedNoteBlock.sound),
				3,
				Conversions.notePitchToAbsolutePitch(emulatedNoteBlock.pitch));
		world.spawnParticle(Particle.NOTE, location, 1);
	}


	private EmulatedNoteBlock initializeBlock(EmulatedNoteBlockRepository worldRepo, Block block) {
		BlockLocation blockLocation = Conversions.locationToBlockLocation(block.getLocation());

		EmulatedNoteBlock emulatedNoteBlock = new EmulatedNoteBlock();
		emulatedNoteBlock.pitch = 0;
		emulatedNoteBlock.sound = ((NoteBlock) block.getBlockData()).getInstrument();

		worldRepo.create(blockLocation, emulatedNoteBlock);
		return emulatedNoteBlock;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerClicksNoteblock(PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		if (event.getClickedBlock().getType() != Material.NOTE_BLOCK) {
			return;
		}
		event.setCancelled(true);

		World world = event.getClickedBlock().getLocation().getWorld();
		EmulatedNoteBlockRepository worldRepo = repoProvider.getNoteBlockRepository(world);

		BlockLocation blockLocation = Conversions.locationToBlockLocation(event.getClickedBlock().getLocation());
		Optional<EmulatedNoteBlock> potentiallyEmulatedNoteBlock = worldRepo.forBlockLocation(blockLocation);
		EmulatedNoteBlock emulatedNoteBlock;

		if (potentiallyEmulatedNoteBlock.isPresent()) {
			// initialized
			emulatedNoteBlock = potentiallyEmulatedNoteBlock.get();
		} else {
			// uninitialized, but maybe is custom?
			NoteBlock data = (NoteBlock) event.getClickedBlock().getBlockData();
			if (data.getInstrument() == Instrument.PIANO && !data.isPowered() &&
					Conversions.convertNoteToNotePitch(data.getNote()) == 0) {

				event.setCancelled(true);
				event.getClickedBlock().getState().update();

				emulatedNoteBlock = initializeBlock(worldRepo, event.getClickedBlock());

				event.getClickedBlock().getState().setBlockData(data); // nothing ever happened
				event.getClickedBlock().getState().update();
			}
			else {
				// custom block, ignore
				//TODO: Call custom event
				return;
			}
		}

		emulatedNoteBlock.pitch++;
		playEmulatedNoteblock(world, event.getClickedBlock().getLocation(), emulatedNoteBlock);
	}

	@EventHandler(ignoreCancelled = true)
	public void onNoteblockPlays(NotePlayEvent event) {

		World world = event.getBlock().getWorld();
		EmulatedNoteBlockRepository worldRepo = repoProvider.getNoteBlockRepository(world);

		BlockLocation blockLocation = Conversions.locationToBlockLocation(event.getBlock().getLocation());
		Optional<EmulatedNoteBlock> potentiallyEmulatedNoteBlock = worldRepo.forBlockLocation(blockLocation);

		if (potentiallyEmulatedNoteBlock.isPresent()) {
			// initialized, ok
			event.setCancelled(true);
			EmulatedNoteBlock emulatedNoteBlock = potentiallyEmulatedNoteBlock.get();
			playEmulatedNoteblock(world, event.getBlock().getLocation(), emulatedNoteBlock);
			//TODO: call custom event

		} else {
			// uninitialized, but MAYBE not custom block
			NoteBlock data = (NoteBlock) event.getBlock().getBlockData();
			if (data.getInstrument() == Instrument.PIANO && !data.isPowered() &&
					Conversions.convertNoteToNotePitch(data.getNote()) == 0) {

				event.setCancelled(true);
				event.getBlock().getState().update();

				EmulatedNoteBlock emulatedNoteBlock = initializeBlock(worldRepo, event.getBlock());
				playEmulatedNoteblock(world, event.getBlock().getLocation(), emulatedNoteBlock);

				//TODO: call custom event

				event.getBlock().getState().setBlockData(data); // nothing ever happened
				event.getBlock().getState().update();

			}
		}
	}

}
