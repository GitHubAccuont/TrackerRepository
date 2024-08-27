package org.justme.trackerapp.calendarevent.data

import java.time.LocalDate
import java.time.LocalTime

data class EventUpdateData(
    val id: Long = 0,
    val date: LocalDate,
    val name: String,
    val details: String,
    val repeatInterval: RepeatEnum,
    val time: LocalTime,
    val repeatEndDate: LocalDate? = null,
    val repeatFrequency: Long = 1
)