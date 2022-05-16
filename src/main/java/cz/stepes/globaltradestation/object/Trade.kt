package cz.stepes.globaltradestation.`object`

import cz.stepes.globaltradestation.util.YamlFile
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import java.util.UUID

data class Trade(
    private val storage: YamlFile,
    val tradeUUID: UUID,
    val seller: OfflinePlayer,
    val offeredItem: ItemStack,
    val price: Int,
    val createdAt: Long,
    val expirationTime: Long,
    val buyerUUID: UUID?
) {

    fun save() {
        val section = storage.config.createSection("trades.$tradeUUID")

        section.set("sellerUUID", seller.uniqueId.toString())
        section.set("offeredItem", offeredItem)
        section.set("price", price)
        section.set("createAt", createdAt)
        buyerUUID?.let { section.set("buyerUUID", it.toString()) }

        storage.saveConfig()
    }

    fun delete() {
        storage.config.set("trades.$tradeUUID", null)
        storage.saveConfig()
    }

    fun available() = System.currentTimeMillis() < createdAt + expirationTime && buyerUUID == null
}