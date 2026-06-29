package com.dopelxy.trackbot.service

import com.dopelxy.trackbot.integration.russianpost.RussianPostClient
import com.dopelxy.trackbot.model.TrackingResult

class TrackingService(
    private val russianPostClient: RussianPostClient = RussianPostClient()
) {

    fun track(trackNumber: String): TrackingResult {
        return russianPostClient.track(trackNumber)
    }
}