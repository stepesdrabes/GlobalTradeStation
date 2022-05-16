package cz.stepes.globaltradestation.gui.abstraction

import org.bukkit.inventory.ItemStack

data class GuiViewItem(
    val onClick: ButtonCompletion?,
    val itemStack: ItemStack
)