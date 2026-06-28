package com.dopelxy.trackbot.config


import java.io.FileInputStream
import java.util.Properties

object Config {
    private val properties = Properties().apply {
        load(FileInputStream("local.properties"))

    }
    val botToken: String
        get() = properties.getProperty("BOT_TOKEN")
            ?: error("BOT_TOKEN not found in local.properties")

    val russianPostLogin: String
        get() = properties.getProperty("RUSSIAN_POST_LOGIN")
            ?: error("RUSSIAN_POST_LOGIN not found in local.properties")

    val russianPostPassword: String
        get() = properties.getProperty("RUSSIAN_POST_PASSWORD")
            ?: error("RUSSIAN_POST_PASSWORD not found in local.properties")
    val useProxy: Boolean
        get() = properties.getProperty("USE_PROXY")?.toBoolean()
            ?: false

    val proxyHost: String
        get() = properties.getProperty("PROXY_HOST")
            ?: error("PROXY_HOST not found in local.properties")

    val proxyPort: Int
        get() = properties.getProperty("PROXY_PORT")?.toInt()
            ?: error("PROXY_PORT not found in local.properties")

}