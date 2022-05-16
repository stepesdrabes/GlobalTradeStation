package cz.stepes.globaltradestation.gui.abstraction

import cz.stepes.globaltradestation.manager.ConfigManager
import cz.stepes.globaltradestation.util.extension.playSound
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class GuiView(
    val player: Player
) : KoinComponent, InventoryHolder {

    protected val configManager by inject<ConfigManager>()

    private lateinit var inventory: Inventory
    private var oldGui: GuiView? = null
    private val itemPositionMap: HashMap<Int, GuiViewItem> = HashMap()

    abstract fun getTitle(): String
    abstract fun getSize(): Int

    protected open fun generate() {
        itemPositionMap.forEach { (index, item) ->
            inventory.setItem(index, item.itemStack)
        }
    }

    open fun createInventory() = generate()

    open fun onClose() {}

    private fun set(index: Int, item: GuiViewItem?) {
        itemPositionMap.remove(index)
        if (item?.itemStack != null) itemPositionMap[index] = item
    }

    fun set(index: Int, itemStack: ItemStack) {
        set(index, GuiViewItem(null, itemStack))
    }

    fun setIfEmpty(index: Int, itemStack: ItemStack) {
        if (itemPositionMap[index] != null) return
        set(index, GuiViewItem(null, itemStack))
    }

    fun set(index: Int, itemStack: ItemStack, onClick: ButtonCompletion?) {
        set(index, GuiViewItem(onClick, itemStack))
    }

    protected fun clear() {
        itemPositionMap.clear()
        inventory.clear()
    }

    fun open(oldGui: GuiView?) {
        if (this.oldGui == null) this.oldGui = oldGui

        inventory = Bukkit.createInventory(this, getSize(), getTitle())
        createInventory()
        player.openInventory(inventory)
    }

    fun handleOnClick(event: InventoryClickEvent) {
        val item = itemPositionMap[event.slot] ?: return
        event.isCancelled = true

        item.onClick?.let {
            player.playSound(configManager.getSound("guiClick"))

            if (event.click.isLeftClick) it.onLeftClick()
            if (event.click.isRightClick) it.onRightClick()
        }
    }

    override fun getInventory() = inventory
}