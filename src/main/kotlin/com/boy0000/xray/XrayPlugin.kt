package com.boy0000.xray

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

val xray by lazy { Bukkit.getPluginManager().getPlugin("Xray") as XrayPlugin }

class XrayPlugin : JavaPlugin() {

    /*data class XrayPackets(
        val entityPacket: ClientboundAddEntityPacket,
        val metadataPacket: ClientboundSetEntityDataPacket
    )*/

    val toggledXray = mutableSetOf<UUID>()
    //val xrayCache = mutableMapOf<Location, XrayPackets>()
    override fun onEnable() {
        // Plugin startup logic

        XrayCommand()
        //XrayTask.startXrayTask()

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
