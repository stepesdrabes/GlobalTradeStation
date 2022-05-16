package cz.stepes.globaltradestation.manager

import cz.stepes.globaltradestation.Main
import cz.stepes.globaltradestation.gui.abstraction.GuiView
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class GuiManager(
    plugin: Main
) : Listener {

    private var playerGuiMap: MutableMap<Player, GuiView> = HashMap()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    fun open(guiView: GuiView, popOld: Boolean = false) {
        if (popOld) playerGuiMap.remove(guiView.player)
        val oldGui = playerGuiMap.getOrDefault(guiView.player, null)
        playerGuiMap.remove(guiView.player)
        playerGuiMap[guiView.player] = guiView

        guiView.open(oldGui)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory == null) return
        if (event.whoClicked !is Player) return
        val guiView = getPlayerGui(event.whoClicked as Player) ?: return
        if (event.view.topInventory != guiView.inventory) return

        event.isCancelled = true
        guiView.handleOnClick(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.player !is Player) return
        val guiView = getPlayerGui(event.player as Player) ?: return
        if (event.player.openInventory.topInventory != guiView.inventory) return

        guiView.onClose()
    }

    private fun getPlayerGui(player: Player): GuiView? {
        return playerGuiMap.getOrDefault(player, null)
    }
}