package cz.stepes.globaltradestation.command

import cz.stepes.globaltradestation.gui.view.TradesView
import cz.stepes.globaltradestation.manager.ConfigManager
import cz.stepes.globaltradestation.manager.GuiManager
import cz.stepes.globaltradestation.manager.TradesManager
import cz.stepes.globaltradestation.util.extension.playSound
import org.apache.commons.lang.WordUtils
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GlobalTradeStation : KoinComponent, TabExecutor {

    private val configManager by inject<ConfigManager>()
    private val guiManager by inject<GuiManager>()
    private val tradeManager by inject<TradesManager>()

    private val actions = listOf("listings", "sell")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(configManager.getMessage("notPlayer"))
            return true
        }

        if (args.isEmpty()) {
            guiManager.open(TradesView(sender))
            return true
        }

        if (!actions.contains(args[0].lowercase())) {
            sender.sendMessage(configManager.getMessage("wrongUsage").replace("%usage%", command.usage))
            sender.playSound(configManager.getSound("error"))
            return true
        }

        when (args[0].lowercase()) {
            "listings" -> guiManager.open(TradesView(sender))
            "sell" -> sell(sender, args)
        }

        return true
    }

    private fun sell(player: Player, args: Array<String>) {
        if (args.size != 2 && args.size != 3) {
            player.sendMessage(configManager.getMessage("wrongUsage").replace("%usage%", "/gts sell <price> <amount>"))
            player.playSound(configManager.getSound("error"))
            return
        }

        val price = args[1].toIntOrNull()

        if (price == null || price <= 0) {
            player.sendMessage(configManager.getMessage("invalidPrice").replace("%price%", args[1]))
            player.playSound(configManager.getSound("error"))
            return
        }

        val item = player.inventory.itemInMainHand

        if (item.type == Material.AIR) {
            player.sendMessage(configManager.getMessage("holdItem"))
            player.playSound(configManager.getSound("error"))
            return
        }

        val amount = if (item.amount > item.maxStackSize) item.maxStackSize else item.amount
        val name = item.itemMeta?.displayName ?: WordUtils.capitalize(item.type.name.lowercase().replace("_", " "))

        tradeManager.createTrade(player, item, price)
        player.sendMessage(
            configManager.getMessage("tradeCreated")
                .replace("%amount%", amount.toString())
                .replace("%itemName%", name)
                .replace("%price%", price.toString())
        )
        player.playSound(configManager.getSound("success"))

        item.amount = item.amount - amount
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) return actions
        return emptyList()
    }
}