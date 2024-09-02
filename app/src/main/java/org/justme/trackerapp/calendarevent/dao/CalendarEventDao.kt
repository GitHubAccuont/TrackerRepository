package org.justme.trackerapp.calendarevent.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import java.time.LocalDate

@Dao
interface CalendarEventDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: CalendarEvent)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvent(event: CalendarEvent)

    @Delete
    suspend fun deleteEvent(event: CalendarEvent)

    @Query("SELECT * FROM calendar_event WHERE date = :date AND repeat_interval IN ('NONE', 'MONTHLY', 'YEARLY')")
    suspend fun getEventsForDate(date: LocalDate): List<CalendarEvent>

    @Query("SELECT * FROM calendar_event WHERE (date BETWEEN :monthStart AND :monthEnd) AND (repeat_interval = 'WEEKLY' OR repeat_interval = 'DAILY')")
    suspend fun getEventsForDate(
        monthStart: LocalDate,
        monthEnd: LocalDate
    ): List<CalendarEvent>

    @Query("SELECT * FROM calendar_event WHERE (date BETWEEN :monthStart AND :monthEnd)")
    suspend fun getEventsForMonth(monthStart: LocalDate, monthEnd: LocalDate): List<CalendarEvent>

    @Query("DELETE FROM calendar_event WHERE date < :date AND repeat_interval = 'NONE'")
    suspend fun deleteOutdatedEvents(date: LocalDate)


    @Query("SELECT * FROM calendar_event WHERE repeat_interval IS NOT 'NONE'")
    suspend fun getRepeatingEvents(): List<CalendarEvent>
}
