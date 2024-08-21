package org.justme.trackerapp.calendarevent.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import java.time.LocalDate

@Dao
interface CalendarEventDao {

    @Insert
    suspend fun insertEvent(event: CalendarEvent)

    @Update
    suspend fun updateEvent(event: CalendarEvent)

    @Delete
    suspend fun deleteEvent(event: CalendarEvent)

    @Query("SELECT * FROM calendar_event WHERE date = :date AND repeat_interval = 'NONE'")
    fun getEventsForDate(date: LocalDate): List<CalendarEvent>

    @Query("SELECT DISTINCT strftime('%d', date) AS day FROM calendar_event WHERE strftime('%Y-%m', date) = :yearMonth AND repeat_interval = 'NONE'")
    fun getOneTimeEventDays(yearMonth: String): List<Int>

    @Query("SELECT DISTINCT strftime('%d', date) AS day FROM calendar_event WHERE repeat_interval = 'WEEKLY'")
    fun getWeeklyEventDays(): List<Int>

    @Query("SELECT DISTINCT strftime('%d', date) AS day FROM calendar_event WHERE repeat_interval IN ('DAILY', 'MONTHLY', 'YEARLY')")
    fun getOtherRepeatableEventDays(): List<Int>

    @Query("SELECT * FROM calendar_event WHERE (date BETWEEN :monthStart AND :monthEnd)")
    fun getEventsForMonth(monthStart: LocalDate, monthEnd: LocalDate): List<CalendarEvent>

    @Query("SELECT * FROM calendar_event WHERE (date BEFORE :currentDate)")
    fun getOutdatedEvents(date: LocalDate): List<CalendarEvent>

    @Query(
        "DELETE FROM calendar_event " +
                "WHERE date < current_date " +
                "AND repeat_interval = 'NONE'"
    )
    fun deleteOutdatedEvents(date: LocalDate): List<CalendarEvent>

    @Query(
        """
        UPDATE calendar_event
        SET date = :currentDate
        WHERE date < :currentDate
        AND repeat_interval = 'DAILY'
    """
    )
    fun updateDailyRepeatingEvents(currentDate: LocalDate)

    @Query(
        """
        UPDATE calendar_event
        SET date = date(date, '+' || ((julianday(:currentDate) - julianday(date)) / 7) || ' days')
        WHERE date < :currentDate
        AND repeat_interval = 'WEEKLY'
    """
    )
    fun updateWeeklyRepeatingEvents(currentDate: LocalDate)

    @Query(
        """
        UPDATE calendar_event
        SET date = date(date, '+' || (strftime('%Y', :currentDate) - strftime('%Y', date)) * 12 +
            (strftime('%m', :currentDate) - strftime('%m', date)) || ' months')
        WHERE date < :currentDate
        AND repeat_interval = 'MONTHLY'
    """
    )
    fun updateMonthlyRepeatingEvents(currentDate: LocalDate)

    @Query(
        """
        UPDATE calendar_event
        SET date = date(date, '+' || (strftime('%Y', :currentDate) - strftime('%Y', date)) || ' years')
        WHERE date < :currentDate
        AND repeat_interval = 'YEARLY'
    """
    )
    fun updateYearlyRepeatingEvents(currentDate: LocalDate)
}
