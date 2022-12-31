package com.github.martijn_heil.extrablokjes;

import com.github.martijn_heil.extrablokjes.storage.BlockLocation;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.block.data.type.NoteBlock;

public class Conversions {

	public static Sound instrumentToSound(Instrument instrument) {
		switch (instrument) {
			default: case PLING: return Sound.BLOCK_NOTE_BLOCK_PLING;
			case BIT: return Sound.BLOCK_NOTE_BLOCK_BIT;
			case BELL: return Sound.BLOCK_NOTE_BLOCK_BELL;
			case BANJO: return Sound.BLOCK_NOTE_BLOCK_BANJO;
			case CHIME: return Sound.BLOCK_NOTE_BLOCK_CHIME;
			case FLUTE: return Sound.BLOCK_NOTE_BLOCK_FLUTE;
			case PIANO: return Sound.BLOCK_NOTE_BLOCK_HARP;
			case GUITAR: return Sound.BLOCK_NOTE_BLOCK_GUITAR;
			case STICKS: return Sound.BLOCK_NOTE_BLOCK_HAT;
			case COW_BELL: return Sound.BLOCK_NOTE_BLOCK_COW_BELL;
			case XYLOPHONE: return Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
			case DIDGERIDOO: return Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
			case BASS_DRUM: return Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
			case SNARE_DRUM: return Sound.BLOCK_NOTE_BLOCK_SNARE;
			case BASS_GUITAR: return Sound.BLOCK_NOTE_BLOCK_BASS;
			case IRON_XYLOPHONE: return Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
		}
	}

	public static float notePitchToAbsolutePitch(byte pitch) {
		// Would be faster with a map, but I'm lazy so just use a formula for now
		return (float) Math.pow(2, ((float) (pitch - 12)) / 12);
	}

	public static BlockLocation locationToBlockLocation(Location location) {
		BlockLocation bLocation = new BlockLocation();
		bLocation.x = location.getBlockX();
		bLocation.y = (short) location.getBlockY();
		bLocation.z = location.getBlockZ();
		return bLocation;
	}

	public static byte convertNoteToNotePitch(Note note) {
		int octave = note.getOctave();
		Note.Tone tone = note.getTone();
		boolean sharped = note.isSharped();

		byte calculatedPitch;

		switch (tone) {
			default: case F: calculatedPitch = 0;
			case G: calculatedPitch = 2; break;
			case A: calculatedPitch = 4; break;
			case B: calculatedPitch = 6; break;
			case C: calculatedPitch = 7; break;
			case D: calculatedPitch = 9; break;
			case E: calculatedPitch = 11; break;
		}

		calculatedPitch += 12 * octave;
		calculatedPitch += sharped ? 1 : 0;

		return calculatedPitch;
	}
}
