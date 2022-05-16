package cz.stepes.globaltradestation.util.extension

import org.bukkit.configuration.ConfigurationSection

@Throws
fun ConfigurationSection.getIntSafe(path: String): Int {
    if (!contains(path)) throw Exception("$path can't be null!")
    return getInt(path)
}

@Throws
fun ConfigurationSection.getStringSafe(path: String): String {
    if (!contains(path)) throw Exception("$path can't be null!")
    return getString(path)
}

@Throws
fun ConfigurationSection.getLongSafe(path: String): Long {
    if (!contains(path)) throw Exception("$path can't be null!")
    return getLong(path)
}