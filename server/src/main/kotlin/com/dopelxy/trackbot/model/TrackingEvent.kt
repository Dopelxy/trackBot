package com.dopelxy.trackbot.model

import java.time.LocalDateTime

data class TrackingEvent(

    val date: LocalDateTime,

    val operation: String,

    val attribute: String,

    val location: String
)

