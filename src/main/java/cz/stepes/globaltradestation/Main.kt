package cz.stepes.globaltradestation

import cz.stepes.globaltradestation.command.GlobalTradeStation
import cz.stepes.globaltradestation.command.GlobalTradeStationReload
import cz.stepes.globaltradestation.manager.ConfigManager
import cz.stepes.globaltradestation.manager.GuiManager
import cz.stepes.globaltradestation.manager.TradesManager
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Main : JavaPlugin() {

    private val instance: Main = this

    override fun onEnable() {
        startKoin {
            modules(
                module {
                    single { instance }
                    single { ConfigManager(get()) }
                    single { GuiManager(get()) }
                    single { TradesManager(get()) }
                },
            )
            printLogger()
        }

        saveDefaultConfig()

        getCommand("gts")?.executor = GlobalTradeStation()
        getCommand("gts")?.tabCompleter = GlobalTradeStation()
        getCommand("gtsreload")?.executor = GlobalTradeStationReload(instance)

        logger.info("Plugin has been successfully enabled!")
    }

    override fun onDisable() = logger.info("Plugin has been disabled!")
}