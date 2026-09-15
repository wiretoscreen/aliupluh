version = "1.0.2"

description = "Randomizes your theme every Aliucord launch"

aliucord {
    changelog.set(
        """
        * **1.0.2** • exclude current theme from randomization, use toasts and improve error messages
        * **1.0.1** • Fix install & Adds error messages
        * **1.0.0** • Initial Release
        """
            .trimIndent()
    )
    deploy.set(true)
}
