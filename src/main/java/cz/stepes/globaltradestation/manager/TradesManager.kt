package cz.stepes.globaltradestation.manager

import cz.stepes.globaltradestation.Main
import cz.stepes.globaltradestation.`object`.Trade
import cz.stepes.globaltradestation.util.YamlFile
import cz.stepes.globaltradestation.util.extension.getIntSafe
import cz.stepes.globaltradestation.util.extension.getLongSafe
import cz.stepes.globaltradestation.util.extension.getStringSafe
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.collections.ArrayList

class TradesManager(
    private val plugin: Main
) {

    private val tradesData: YamlFile = YamlFile("data/trades", plugin)
    private var trades: MutableList<Trade> = ArrayList()

    init {
        loadTrades()
    }

    fun reloadData() {
        trades.clear()
        loadTrades()
    }

    private fun loadTrades() {
        tradesData.config.getConfigurationSection("trades")?.let { section ->
            section.getKeys(false).forEach { tradeID ->
                section.getConfigurationSection(tradeID)?.let {
                    try {
                        loadTrade(tradeID, it)
                    } catch (exception: Exception) {
                        plugin.logger.warning("Error while loading trade $tradeID: ${exception.message}")

                        section.set("trades.$tradeID", null)
                        tradesData.saveConfig()
                    }
                }
            }
        }
    }

    @Throws
    private fun loadTrade(tradeID: String, section: ConfigurationSection) {
        val tradeUUID = UUID.fromString(tradeID)
        val sellerUUID = UUID.fromString(section.getStringSafe("sellerUUID"))
        val offeredItem = section.getItemStack("offeredItem")
        val price = section.getIntSafe("price")
        val createdAt = section.getLongSafe("createdAt")

        var buyerUUID: UUID? = null
        section.getString("buyerUUID")?.let { buyerUUID = UUID.fromString(it) }

        trades.add(
            Trade(
                storage = tradesData,
                tradeUUID = tradeUUID,
                seller = Bukkit.getOfflinePlayer(sellerUUID),
                offeredItem = offeredItem,
                price = price,
                createdAt = createdAt,
                expirationTime = plugin.config.getLong("tradeExpiration"),
                buyerUUID = buyerUUID
            )
        )
    }

    fun createTrade(seller: Player, offeredItem: ItemStack, price: Int) {
        val trade = Trade(
            storage = tradesData,
            tradeUUID = UUID.randomUUID(),
            seller = seller,
            offeredItem = offeredItem.clone(),
            price = price,
            createdAt = System.currentTimeMillis(),
            expirationTime = plugin.config.getLong("tradeExpiration"),
            buyerUUID = null
        )

        trade.save()
        trades.add(trade)
    }

    fun getTrades() = trades.filter { it.available() }
}