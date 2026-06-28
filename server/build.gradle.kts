plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
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
}