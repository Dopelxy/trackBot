package com.dopelxy.trackbot.bot

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import com.dopelxy.trackbot.service.TrackingService
import com.dopelxy.trackbot.service.TrackingNumberValidator
import java.time.format.DateTimeFormatter
import com.dopelxy.trackbot.service.OperationIcon
import com.dopelxy.trackbot.model.ErrorType
import org.slf4j.LoggerFactory

class TrackBot (
    token: String
) : LongPollingSingleThreadUpdateConsumer {

    private val logger =
        LoggerFactory.getLogger(TrackBot::class.java)
    private val telegramClient = OkHttpTelegramClient(token)
    private val trackingService = TrackingService()

    private val dateFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    override fun consume(update: Update) {
        if (!update.hasMessage()) return

        val message =  update.message
        val text = message.text ?: return
        val chatId = message.chatId
        logger.info("User {} -> {}", chatId, text)

        when {

            text == "/start" -> {


                telegramClient.execute(
                    SendMessage.builder()
                        .chatId(chatId)
                        .text(

                            """
                    🚚 TrackBot (Версия 0.1 в разработке. Есть лимиты)
                  
                    
                    Добро пожаловать!
                    
                    Отправьте трек-номер Почты России.
                    """.trimIndent()
                        )
                        .build()
                )
            }

            else -> {
                val trackNumber = text.trim().uppercase()

                if (!TrackingNumberValidator.isValid(trackNumber)) {

                    telegramClient.execute(
                        SendMessage.builder()
                            .chatId(chatId)
                            .text(
                                """
                ⚠️ Неверный формат трек-номера.

                Поддерживаются:

                📦 RA123456789RU
                📦 EE123456789CN
                📦 12345678901234
                """.trimIndent()
                            )
                            .build()
                    )
                    logger.warn(
                        "Invalid tracking number from {} : {}",
                        chatId,
                        trackNumber
                    )

                    return
                }

                val result = trackingService.track(trackNumber)

                val answer = if (!result.success) {

                    when (result.errorType) {

                        ErrorType.NOT_FOUND ->
                            """
            📭 Отправление не найдено.

            Проверьте правильность трек-номера.
            """.trimIndent()

                        ErrorType.DAILY_LIMIT ->
                            """
            🚫 Суточный лимит запросов к Почте России исчерпан.

            Попробуйте повторить запрос завтра.
            """.trimIndent()

                        ErrorType.NETWORK ->
                            """
            🌐 Не удалось подключиться к серверу Почты России.

            Проверьте подключение к Интернету.
            """.trimIndent()

                        ErrorType.TIMEOUT ->
                            """
            ⏳ Сервер Почты России долго отвечает.

            Попробуйте через несколько минут.
            """.trimIndent()

                        else ->
                            """
            ⚠️ Произошла неизвестная ошибка.

            ${result.errorMessage ?: "Без описания"}
            """.trimIndent()
                    }

                } else {

                    buildString {

                        appendLine("📦 Отслеживание отправления")
                        appendLine("━━━━━━━━━━━━━━━━━━━━")
                        appendLine("📦 Найдено событий: ${result.events.size}")
                        appendLine()

                        result.events.forEachIndexed { index, event ->

                            appendLine("🚚 Этап ${index + 1} из ${result.events.size}")

                            val progress = buildString {

                                repeat(index + 1) { append("🟩") }

                                repeat(result.events.size - index - 1) {
                                    append("⬜")
                                }
                            }

                            appendLine(progress)
                            appendLine()

                            appendLine("📅 ${event.date.format(dateFormatter)}")
                            appendLine("📍 Отделение: ${event.location}")
                            appendLine("${OperationIcon.get(event.operation)} ${event.operation}")

                            if (
                                event.attribute != "Единичный" &&
                                event.attribute != "Массовый" &&
                                event.attribute != "Партионный"
                            ) {
                                appendLine("🏷 Статус: ${event.attribute}")
                            }

                            appendLine()
                        }
                    }
                }
                if (result.success) {

                    logger.info(
                        "Track {} -> {} events for user {}",
                        trackNumber,
                        result.events.size,
                        chatId
                    )

                } else {

                    logger.warn(
                        "Track {} failed for user {} ({})",
                        trackNumber,
                        chatId,
                        result.errorType
                    )
                }

                telegramClient.execute(
                    SendMessage.builder()
                        .chatId(chatId)
                        .text(answer)
                        .build()
                )
            }
        }
    }
}