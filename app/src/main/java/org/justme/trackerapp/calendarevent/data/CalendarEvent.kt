package org.justme.trackerapp.calendarevent.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "calendar_event")
@TypeConverters(CalendarEventConverter::class)
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date") var date: LocalDate,
    @ColumnInfo(name = "type") val name: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "repeat_interval") val repeatInterval: RepeatEnum,
    @ColumnInfo(name = "time") val time: LocalTime
)
