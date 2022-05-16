package cz.stepes.globaltradestation.util

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

class YamlFile(
    name: String,
    plugin: Plugin
) {

    private val file: File
    var config: FileConfiguration

    init {
        file = File(plugin.dataFolder.toString() + "/" + name + ".yml")
        config = YamlConfiguration.loadConfiguration(file)

        saveConfig()
    }

    fun saveConfig() {
        try {
            config.save(file)
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}