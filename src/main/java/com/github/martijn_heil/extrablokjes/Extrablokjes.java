/*
 * Extrablokjes more blocks!
 * Copyright (C) 2022  Extrablokjes contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.martijn_heil.extrablokjes;

import com.github.martijn_heil.extrablokjes.listeners.ExtrablokjesListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Extrablokjes extends JavaPlugin {
	@Override
	public void onEnable() {
		this.saveDefaultConfig();

		this.getServer().getPluginManager().registerEvents(new ExtrablokjesListener(), this);
	}

	@Override
	public void onDisable() {

	}
}
