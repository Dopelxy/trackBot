plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    id("com.github.bjornvester.wsdl2java") version "2.0.2"
}

group = "com.dopelxy.trackbot"
version = "1.0.0"
application {
    mainClass = "com.dopelxy.trackbot.ApplicationKt"
}

dependencies {
    
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation(libs.telegrambots)
    implementation(libs.telegrambots.client)
    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientCio)
    implementation(libs.ktor.clientContentNegotiation)
}