package com.wiretoscreen.plugins

import android.content.Context
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import com.aliucord.api.SettingsAPI
import com.aliucord.PluginManager

fun randomizeTheme() {
    val plugin = PluginManager.plugins["Themer"] ?: return
    val sets: SettingsAPI = plugin.settings

    val themeSwitch = sets.getAllKeys().filter { it.endsWith("-enabled") }
    if (themeSwitch.isEmpty()) return

    val current = themeSwitch.find { sets.getBool(it, false) }
    current?.let { sets.setBool(it, false) }

    val random = themeSwitch.random()
    sets.setBool(random, true)
}

@AliucordPlugin(requiresRestart = false)

class ThemeRandomizer : Plugin() {
    override fun start(context: Context) {
        randomizeTheme()
    }

    override fun stop(context: Context) {
    }
}
