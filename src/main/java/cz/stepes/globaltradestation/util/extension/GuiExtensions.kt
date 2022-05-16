package cz.stepes.globaltradestation.util.extension

import cz.stepes.globaltradestation.gui.abstraction.GuiView
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun GuiView.createBorder() {
    val borderItem = ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toByte().toShort())
    val itemMeta = borderItem.itemMeta

    itemMeta.displayName = "§r"
    borderItem.itemMeta = itemMeta

    for (i in 0 until getSize()) {
        if (i < 9 || i > getSize() - 9 - 1 || i % 9 == 0 || (i + 1) % 9 == 0) setIfEmpty(i, borderItem)
    }
}

object GuiUtil {

    fun getTotalPages(list: List<*>?, pageSize: Int): Int {
        var totalPages = 1
        if (list != null) {
            totalPages = list.size / pageSize
            if (list.size - totalPages * pageSize > 0) totalPages++
        }
        return if (totalPages == 0) 1 else totalPages
    }
}