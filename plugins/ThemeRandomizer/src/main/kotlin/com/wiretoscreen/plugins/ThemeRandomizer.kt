package com.wiretoscreen.plugins

import android.content.Context
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.api.SettingsAPI
import com.aliucord.PluginManager

fun randomizeTheme() {
    val plugin = PluginManager.plugins["Themer"]
    
    if (plugin == null){
        Utils.showToast("[ThemeRandomizer]: Themer plugin is not installed!")
        return
    }

    val sets: SettingsAPI = plugin.settings
    val themeSwitch = sets.getAllKeys().filter { it.endsWith("-enabled") }

    if (themeSwitch.isEmpty()) {
        Utils.showToast("[ThemeRandomizer]: No themes found to randomize, try enabling them if you never enabled.")
        return
    }

    val current = themeSwitch.find { sets.getBool(it, false) }
    current?.let { sets.setBool(it, false) }

    val random = themeSwitch.filter { it != current }.random()
    sets.setBool(random, true)
}

@AliucordPlugin(requiresRestart = true)
class ThemeRandomizer : Plugin() {
    override fun start(context: Context) {
        randomizeTheme()
    }
    
    override fun stop(context: Context) {
    }
}