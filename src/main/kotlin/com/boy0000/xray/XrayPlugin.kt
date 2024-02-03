package com.boy0000.xray

import com.mineinabyss.idofront.config.config
import com.mineinabyss.idofront.di.DI
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class XrayPlugin : JavaPlugin() {

    override fun onEnable() {
        createXrayContext()

        XrayCommand()
        XrayTask.startXrayTask()
    }

    override fun onDisable() {
    }

    fun createXrayContext() {
        DI.remove<XrayContext>()
        val xrayContext = object : XrayContext {
            override val plugin = this@XrayPlugin
            override val config: XrayConfig by config("config", dataFolder.toPath(), XrayConfig())
        }
        DI.add<XrayContext>(xrayContext)
    }
}
