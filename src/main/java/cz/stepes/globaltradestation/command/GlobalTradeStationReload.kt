package cz.stepes.globaltradestation.command

import cz.stepes.globaltradestation.Main
import cz.stepes.globaltradestation.manager.ConfigManager
import cz.stepes.globaltradestation.manager.TradesManager
import cz.stepes.globaltradestation.util.extension.playSound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GlobalTradeStationReload(
    private val plugin: Main
) : KoinComponent, CommandExecutor {

    private val configManager by inject<ConfigManager>()
    private val tradesManager by inject<TradesManager>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        plugin.reloadConfig()
        tradesManager.reloadData()

        sender.sendMessage(configManager.getMessage("configReloaded"))
        if (sender is Player) sender.playSound(configManager.getSound("success"))

        return true
    }
}