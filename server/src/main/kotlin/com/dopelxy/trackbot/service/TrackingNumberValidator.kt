package com.dopelxy.trackbot.service



object TrackingNumberValidator {

    private val internationalRegex =
        Regex("^[A-Z]{2}\\d{9}[A-Z]{2}$")

    private val russianRegex =
        Regex("^\\d{14}$")

    fun isValid(trackNumber: String): Boolean {

        val number = trackNumber.trim().uppercase()

        return internationalRegex.matches(number)
                || russianRegex.matches(number)
    }
}