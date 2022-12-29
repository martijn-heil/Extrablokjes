package com.github.martijn_heil.extrablokjes.storage.yaml;

import com.github.martijn_heil.extrablokjes.Conversions;
import com.github.martijn_heil.extrablokjes.storage.BlockLocation;
import com.github.martijn_heil.extrablokjes.storage.EmulatedNoteBlock;
import com.github.martijn_heil.extrablokjes.storage.EmulatedNoteBlockRepository;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class YamlEmulatedNoteBlockRepository implements EmulatedNoteBlockRepository {
	final private File file;
	private HashMap<BlockLocation, EmulatedNoteBlock> map = new HashMap<>();

	public YamlEmulatedNoteBlockRepository(File file) {
		this.file = file;
		try {
			//noinspection ResultOfMethodCallIgnored
			this.file.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.loadAll();
	}

	@Override
	public void destroyAll() {
		map.clear();
	}

	@Override
	public void destroy(BlockLocation location) {
		map.remove(location);
	}

	@Override
	public void create(BlockLocation location, EmulatedNoteBlock noteBlock) {
		this.update(location, noteBlock);
	}

	@Override
	public void update(BlockLocation location, EmulatedNoteBlock noteBlock) {
		map.put(location, noteBlock);
	}

	@Override
	public Optional<EmulatedNoteBlock> forBlockLocation(BlockLocation location) {
		return Optional.ofNullable(map.get(location));
	}

	@Override
	public Optional<EmulatedNoteBlock> forLocation(Location location) {
		return this.forBlockLocation(Conversions.locationToBlockLocation(location));
	}

	@Override
	public Map<BlockLocation, EmulatedNoteBlock> all() {
		return map;
	}

	@Override
	public boolean exists(BlockLocation location) {
		return map.containsKey(location);
	}

	@Override
	public boolean exists(Location location) {
		return map.containsKey(Conversions.locationToBlockLocation(location));
	}

	@Override
	public int count() {
		return map.size();
	}

	@Override
	public void saveAllDirty() throws Exception {
		this.saveAll();
	}

	@Override
	public void saveAll() throws Exception {
		YamlConfiguration yamlConfiguration = new YamlConfiguration();

		for (Map.Entry<BlockLocation, EmulatedNoteBlock> entry : map.entrySet()) {
			BlockLocation location = entry.getKey();
			EmulatedNoteBlock noteBlock = entry.getValue();
			String key = getKey(location);
			yamlConfiguration.set(key + ".instrument", noteBlock.sound.name());
			yamlConfiguration.set(key + ".pitch", noteBlock.pitch);
		}

		yamlConfiguration.save(file);
	}

	private void loadAll() {
		HashMap<BlockLocation, EmulatedNoteBlock> everything = new HashMap<>();
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(this.file);

		for (String key : yaml.getKeys(false)) {
			EmulatedNoteBlock result = this.forKey(yaml, key).orElseThrow(IllegalStateException::new);
			everything.put(keyToBlockLocation(key), result);
		}

		this.map = everything;
	}

	private Optional<EmulatedNoteBlock> forKey(YamlConfiguration yaml, String key) {
		EmulatedNoteBlock noteBlock = new EmulatedNoteBlock();

		String instrumentName = Optional.ofNullable(yaml.getString(key + ".instrument"))
				.orElseThrow(() -> new IllegalStateException("Instrument name unknown"));
		noteBlock.sound = Instrument.valueOf(instrumentName);
		noteBlock.pitch = (byte) yaml.getInt(key + ".pitch");

		return Optional.of(noteBlock);
	}

	private static String getKey(BlockLocation location) {
		return location.x + "," + location.y + "," + location.z;
	}

	private static BlockLocation keyToBlockLocation(String key) {
		String[] split = key.split(",");

		int x = Integer.parseInt(split[0]);
		short y = Short.parseShort(split[1]);
		int z = Integer.parseInt(split[2]);

		BlockLocation location = new BlockLocation();
		location.x = x;
		location.y = y;
		location.z = z;

		return location;
	}
}
