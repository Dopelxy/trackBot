package com.dopelxy.trackbot.bot


import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication

object BotLauncher {

    fun start(token: String) {

        val application = TelegramBotsLongPollingApplication()

        application.registerBot(
            token,
            TrackBot(token)
        )

        println("TrackBot started successfully.")

        Thread.currentThread().join()
    }
}