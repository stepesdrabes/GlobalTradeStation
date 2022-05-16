package cz.stepes.globaltradestation.util.extension

import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.playSound(sound: Sound) = playSound(location, sound, 1.0f, 1.0f)

fun Player.sendMessageSound(message: String, sound: Sound) {
    sendMessage(message)
    playSound(location, sound, 1.0f, 1.0f)
}