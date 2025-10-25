package com.wiretoscreen.plugins

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import com.discord.stores.StoreStream
import com.aliucord.api.SettingsAPI
import com.aliucord.api.NotificationsAPI
import com.aliucord.entities.NotificationData
import com.aliucord.PluginManager

fun showNotification(title: String, body: String) {
    Handler(Looper.getMainLooper()).postDelayed({ // If not delayed, notification won't show and I'm too stupid to figure out a better way to do it.
        val notification = NotificationData() // https://aliucord.github.io/dokka/html/-aliucord/com.aliucord.entities/-notification-data/index.html
        notification.title = title
        notification.body = body
        notification.autoDismissPeriodSecs = 30
        notification.onClick = {
            StoreStream.getMessagesLoader().jumpToMessage(811275162715553823L, 846165924708614194L) // #plugins-list - Themer
        }

        NotificationsAPI.display(notification)
    }, 10000)
}

fun randomizeTheme() {
    val plugin = PluginManager.plugins["Themer"]
    
    if (plugin == null){
        showNotification("ThemeRandomizer", "Themer must be installed for this plugin to work")
        return
    }
    
    val sets: SettingsAPI = plugin.settings

    val themeSwitch = sets.getAllKeys().filter { it.endsWith("-enabled") }
    if (themeSwitch.isEmpty()) {
        showNotification("ThemeRandomizer", "No themes found to randomize")
        return
    }

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
