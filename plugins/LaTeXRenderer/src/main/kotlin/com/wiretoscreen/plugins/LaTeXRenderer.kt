package com.wiretoscreen.plugins

import android.content.Context
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import java.util.StringTokenizer

@AliucordPlugin(requiresRestart = false)
class LaTeXRenderer : Plugin() {
    override fun start(context: Context) {
        commands.registerCommand(
            "latex",
            "Render a LaTeX formula",
            listOf(
                Utils.createCommandOption(
                    ApplicationCommandType.STRING,
                    "formula",
                    "LaTeX formula",
                    null,
                    true
                ),
                Utils.createCommandOption(
                    ApplicationCommandType.BOOLEAN,
                    "send",
                    "Send the result in the channel"
                )
            )
        ) { ctx ->
            val formula = ctx.getRequiredString("formula")
            val send = ctx.getBoolOrDefault("send", false)

            val response = Http.Request(
                "https://quicklatex.com/latex3.f",
                "POST"
            ).executeWithUrlEncodedForm(
                hashMapOf<String, Any>(
                    "formula" to formula,
                    "fsize" to "24px",
                    "fcolor" to "ffffff",
                    "mode" to "0",
                    "out" to "1",
                    "remhost" to "quicklatex.com",
                    "preamble" to "\\usepackage{amsmath}\n\\usepackage{amsfonts}\n\\usepackage{amssymb}"
                )
            ).text()

            val url = parseResponse(response)
                ?: return@registerCommand CommandsAPI.CommandResult(
                    "Failed to render LaTeX.",
                    null,
                    false
                )

            CommandsAPI.CommandResult(url, null, send)
        }
    }

    private fun parseResponse(response: String): String? {
        val tokens = StringTokenizer(response)

        if (!tokens.hasMoreTokens() || tokens.nextToken() != "0")
            return null

        return if (tokens.hasMoreTokens()) tokens.nextToken() else null
    }

    override fun stop(context: Context) {
        commands.unregisterAll()
    }
}