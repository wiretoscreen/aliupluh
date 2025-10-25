package com.wiretoscreen.plugins

import org.json.JSONObject
import org.json.JSONArray
import android.content.Context
import com.aliucord.Utils
import com.aliucord.Http
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType

@AliucordPlugin(requiresRestart = false)

class FriendInvites : Plugin() {
    override fun start(context: Context) {
        commands.registerCommand("friendinvites new", "Generates a new friend invite.") {
            
            val generateInvite = Http.Request.newDiscordRNRequest("/users/@me/invites", "POST").setHeader("Content-Type", "application/json").executeWithJson({})
            
            if (generateInvite.statusCode == 200){
                val res = JSONObject(generateInvite.text())
                CommandsAPI.CommandResult("Successfully generated invite:\nhttps://discord.gg/${res.optString("code")} *(invite rendering of this type may look buggy in Aliucord)*", null, false)
            } else {
                CommandsAPI.CommandResult("Failed to generate invite (status code: ${generateInvite.statusCode})", null, false)
            }
        }
        
        commands.registerCommand("friendinvites view", "View your friend invites.") {
            val generateInvite = Http.Request.newDiscordRNRequest("/users/@me/invites", "GET").setHeader("Content-Type", "application/json").execute()
            
            if (generateInvite.statusCode == 200){
                val res = JSONArray(generateInvite.text())
                
                if (res.length() == 0){
                    CommandsAPI.CommandResult("You currently have no active friend invites.", null, false)
                } else {
                    val invites = buildString {
                        for (i in 0 until res.length()) {
                            val code = res.getJSONObject(i).getString("code")
                            append("<https://discord.gg/$code>\n")
                        }
                    }
                    CommandsAPI.CommandResult("**Your current invites:**\n$invites", null, false)
                }
            } else {
                CommandsAPI.CommandResult("Failed to fetch your invites (status code: ${generateInvite.statusCode})", null, false)
            }
        }

        commands.registerCommand("friendinvites delete", "Deletes all your friend invites.") {
            val generateInvite = Http.Request.newDiscordRNRequest("/users/@me/invites", "DELETE").setHeader("Content-Type", "application/json").execute()
            
            if (generateInvite.statusCode == 200){
                CommandsAPI.CommandResult("Successfully deleted all your friend invites.", null, false)
            } else {
                CommandsAPI.CommandResult("Failed to delete your invites (status code: ${generateInvite.statusCode})", null, false)
            }
        }
    }

    override fun stop(context: Context) {
        // Unregister our commands
        commands.unregisterAll()
    }
}