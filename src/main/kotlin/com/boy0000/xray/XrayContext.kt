package com.boy0000.xray

import com.mineinabyss.idofront.di.DI

val xray by DI.observe<XrayContext>()
interface XrayContext {
    val plugin: XrayPlugin
    val config: XrayConfig
}