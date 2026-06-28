package com.dopelxy.trackbot

import com.dopelxy.trackbot.bot.BotLauncher
import com.dopelxy.trackbot.config.Config

fun main() {

    println("====================================")
    println("         TrackBot starting...")
    println("====================================")

    if (Config.useProxy) {

        System.setProperty("http.proxyHost", Config.proxyHost)
        System.setProperty("http.proxyPort", Config.proxyPort.toString())

        System.setProperty("https.proxyHost", Config.proxyHost)
        System.setProperty("https.proxyPort", Config.proxyPort.toString())

        println("Proxy enabled: ${Config.proxyHost}:${Config.proxyPort}")
    }

    BotLauncher.start(Config.botToken)
}