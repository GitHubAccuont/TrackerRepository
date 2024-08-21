package org.justme.trackerapp.calendarevent.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.justme.trackerapp.calendarevent.dao.CalendarEventDao
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.data.RepeatEnumConverter

@Database(entities = [CalendarEvent::class], version = 1)
@TypeConverters(RepeatEnumConverter::class)
abstract class CalendarEventDb : RoomDatabase() {
    abstract fun calendarEventDao(): CalendarEventDao
}