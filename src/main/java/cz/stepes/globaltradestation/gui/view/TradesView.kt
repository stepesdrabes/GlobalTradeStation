package cz.stepes.globaltradestation.gui.view

import cz.stepes.globaltradestation.gui.abstraction.ButtonCompletion
import cz.stepes.globaltradestation.gui.abstraction.GuiView
import cz.stepes.globaltradestation.manager.TradesManager
import cz.stepes.globaltradestation.util.extension.GuiUtil
import cz.stepes.globaltradestation.util.extension.createBorder
import org.bukkit.entity.Player
import org.koin.core.component.inject

class TradesView(player: Player) : GuiView(player) {

    private val tradesManager by inject<TradesManager>()
    private val trades = tradesManager.getTrades()

    private val pageSize = 45
    private var page = 0

    override fun createInventory() {
        clear()
        createBorder()

        val pageTrades = trades.drop(page * pageSize).take(pageSize)

        // Trades
        pageTrades.forEachIndexed { index, trade ->
            val item = trade.offeredItem.clone();
            val itemMeta = item.itemMeta
            itemMeta.lore.addAll(configManager.getMessageList("tradeLoreAddition"))

            if (trade.seller.uniqueId == player.uniqueId) {
                itemMeta.lore.addAll(configManager.getMessageList("ownTradeLoreAddition"))
            }

            itemMeta.lore = itemMeta.lore.map {
                it.replace("%price%", trade.price.toString())
                    .replace("%timeLeft%", "N/A")
                    .replace("%seller%", trade.seller.name)
            }

            item.itemMeta = itemMeta
            set(index, item, object : ButtonCompletion {
                override fun onLeftClick() {

                }

                override fun onRightClick() {

                }
            })
        }

        // Previous page
        if (page > 0) {

        }

        // Next page
        if (trades.size - ((page + 1) * pageSize) > 0) {

        }

        super.createInventory()
    }

    override fun getTitle(): String = configManager.getMessage("guis.listings.title", false)
        .replace("%page%", page.toString())
        .replace("%totalPages%", GuiUtil.getTotalPages(trades, pageSize).toString())

    override fun getSize(): Int = 54
}