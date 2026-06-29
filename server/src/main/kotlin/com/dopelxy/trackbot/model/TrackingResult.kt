package com.dopelxy.trackbot.model

data class TrackingResult(

    val success: Boolean,

    val events: List<TrackingEvent> = emptyList(),

    val errorMessage: String? = null,

    val errorType: ErrorType? = null
)