package com.dopelxy.trackbot.integration.russianpost

import com.dopelxy.trackbot.config.Config
import com.dopelxy.trackbot.model.TrackingResult
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import com.dopelxy.trackbot.model.ErrorType
import java.net.ConnectException
import java.net.UnknownHostException
import java.net.http.HttpTimeoutException
import org.slf4j.LoggerFactory


class RussianPostClient {
    private val logger = LoggerFactory.getLogger(RussianPostClient::class.java)

    private val client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(20))
        .build()

    fun track(trackNumber: String): TrackingResult {

        return try {

            val xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope
                    xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
                    xmlns:oper="http://russianpost.org/operationhistory"
                    xmlns:data="http://russianpost.org/operationhistory/data"
                    xmlns:ns1="http://schemas.xmlsoap.org/soap/envelope/">

                    <soap:Header/>

                    <soap:Body>

                        <oper:getOperationHistory>

                            <data:OperationHistoryRequest>

                                <data:Barcode>$trackNumber</data:Barcode>
                                <data:MessageType>0</data:MessageType>
                                <data:Language>RUS</data:Language>

                            </data:OperationHistoryRequest>

                            <data:AuthorizationHeader ns1:mustUnderstand="?">

                                <data:login>${Config.russianPostLogin}</data:login>
                                <data:password>${Config.russianPostPassword}</data:password>

                            </data:AuthorizationHeader>

                        </oper:getOperationHistory>

                    </soap:Body>

                </soap:Envelope>
            """.trimIndent()

            val request = HttpRequest.newBuilder()
                .uri(URI.create(Config.russianPostUrl))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/soap+xml; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(xml))
                .build()

            val response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(Charsets.UTF_8)
            )

            val body = response.body()
            logger.debug("SOAP response:\n{}", body)


            if (body.contains(":Fault>")) {

                return when {

                    body.contains("лимит", ignoreCase = true) ->

                        TrackingResult(
                            success = false,
                            errorType = ErrorType.DAILY_LIMIT,
                            errorMessage = "Превышен суточный лимит запросов."
                        )

                    body.contains("не найден", ignoreCase = true) ->

                        TrackingResult(
                            success = false,
                            errorType = ErrorType.NOT_FOUND,
                            errorMessage = "Отправление не найдено."
                        )

                    else ->

                        TrackingResult(
                            success = false,
                            errorType = ErrorType.UNKNOWN,
                            errorMessage = "Ошибка сервиса Почты России."
                        )
                }
            }

            val tempFile = File.createTempFile(
                "trackbot-",
                ".xml"
            )

            tempFile.writeText(body)

            val events = XmlParser().parse(tempFile)
            if (events.isEmpty()) {

                tempFile.delete()

                return TrackingResult(
                    success = false,
                    errorType = ErrorType.NOT_FOUND,
                    errorMessage = "Отправление не найдено."
                )
            }

            tempFile.delete()

            TrackingResult(
                success = true,
                events = events
            )

        } catch (e: Exception) {

            logger.error("Tracking failed", e)

            val errorType = when (e) {

                is java.net.http.HttpTimeoutException ->
                    ErrorType.TIMEOUT

                is java.net.ConnectException,
                is java.net.UnknownHostException ->
                    ErrorType.NETWORK

                else ->
                    ErrorType.UNKNOWN
            }

            TrackingResult(
                success = false,
                errorType = errorType,
                errorMessage = e.message
            )
        }
    }
}