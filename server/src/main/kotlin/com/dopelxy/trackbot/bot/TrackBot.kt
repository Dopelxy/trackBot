package com.dopelxy.trackbot.bot

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class TrackBot (
    token: String
) : LongPollingSingleThreadUpdateConsumer {
    private val telegramClient = OkHttpTelegramClient(token)
    override fun consume(update: Update) {
        if (!update.hasMessage()) return

        val message =  update.message
        val text = message.text ?: return
        val chatId = message.chatId

        when (text) {
            "/start" -> {

                telegramClient.execute(
                    SendMessage.builder()
                        .chatId(chatId)
                        .text(
                            """
                            🚚 TrackBot
                            
                            Добро пожаловать!
                            
                            Этот бот поможет отслеживать почтовые отправления.
                            """.trimIndent()
                        )
                        .build()
                )
            }
        }
    }
}