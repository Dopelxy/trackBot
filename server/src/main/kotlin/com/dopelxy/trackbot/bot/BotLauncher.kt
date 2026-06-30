
package com.dopelxy.trackbot.bot

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication

object BotLauncher {

    private val logger =
        LoggerFactory.getLogger(BotLauncher::class.java)

    private var application: TelegramBotsLongPollingApplication? = null

    fun start(token: String) {

        if (application != null) {
            logger.warn("TrackBot is already running.")
            return
        }

        application = TelegramBotsLongPollingApplication()

        application!!.registerBot(
            token,
            TrackBot(token)
        )

        logger.info("TrackBot started successfully.")
    }

    fun stop() {

        logger.info("Stopping TrackBot...")

        application?.close()

        application = null

        logger.info("TrackBot stopped.")
    }
}

