package cz.stepes.globaltradestation.manager

import cz.stepes.globaltradestation.Main
import org.bukkit.ChatColor
import org.bukkit.Sound

class ConfigManager(
    private val plugin: Main
) {

    fun getMessage(path: String, withPrefix: Boolean = true): String = ChatColor.translateAlternateColorCodes(
        '&',
        (if (withPrefix) plugin.config.getString("messages.prefix") + " " else "") + plugin.config.getString("messages.$path")
    )

    fun getMessageList(path: String) = plugin.config.getStringList("messages.$path").map {
        ChatColor.translateAlternateColorCodes('&', it)
    }

    fun getSound(path: String): Sound {
        plugin.config.getString("sounds.$path")?.let { return Sound.valueOf(it) }
        return Sound.ENTITY_ITEM_BREAK
    }
}