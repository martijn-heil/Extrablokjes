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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens
import java.net.URI
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    `java-gradle-plugin`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    idea
}

group = "com.github.martijn_heil.extrablokjes"
version = "1.0-SNAPSHOT"
description = "More blocks"

apply {
    plugin("java")
    plugin("com.github.johnrengelman.shadow")
    plugin("idea")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<ProcessResources> {
        filter(mapOf(Pair("tokens", mapOf(Pair("version", version)))), ReplaceTokens::class.java)
    }
    withType<ShadowJar> {
        this.classifier = null
        this.configurations = listOf(project.configurations.shadow.get())
    }
}

defaultTasks = mutableListOf("shadowJar")

repositories {
    maven { url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }

    mavenCentral()
    mavenLocal()
}

idea {
    project {
        languageLevel = IdeaLanguageLevel("1.8")
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT") { isChanging = true }
}
