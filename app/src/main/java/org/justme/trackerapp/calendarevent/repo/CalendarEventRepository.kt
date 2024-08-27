package org.justme.trackerapp.calendarevent.repo

import org.justme.trackerapp.calendarevent.dao.CalendarEventDao
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.data.RepeatEnum
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarEventRepository @Inject constructor(
    private val calendarEventDao: CalendarEventDao
) {

    suspend fun getEventsForDate(date: LocalDate): List<CalendarEvent> {
        return calendarEventDao.getEventsForDate(date)
    }

    suspend fun getEventsForMonth(date: LocalDate): List<Int> {
        val monthStart = date.withDayOfMonth(1)
        val monthEnd = date.withDayOfMonth(date.lengthOfMonth())

        val events = calendarEventDao.getEventsForMonth(monthStart, monthEnd)
        val eventDays = mutableListOf<Int>()

        events.forEach { event ->
            eventDays.add(event.date.dayOfMonth)

            if (event.repeatInterval == RepeatEnum.WEEKLY) {
                var nextEventDate = event.date.plusWeeks(1)
                while (nextEventDate <= monthEnd) {
                    eventDays.add(nextEventDate.dayOfMonth)
                    nextEventDate = nextEventDate.plusWeeks(1)
                }
            }
        }

        return eventDays.distinct().sorted()
    }

    suspend fun updateOutdatedEvents(currentDate: LocalDate) {
        calendarEventDao.updateDailyRepeatingEvents(currentDate)
        calendarEventDao.updateWeeklyRepeatingEvents(currentDate)
        calendarEventDao.updateMonthlyRepeatingEvents(currentDate)
        calendarEventDao.updateYearlyRepeatingEvents(currentDate)
    }

    suspend fun saveNewEvent(event: CalendarEvent) {
        return calendarEventDao.insertEvent(event)
    }

    suspend fun deleteEvent(event: CalendarEvent) {
        calendarEventDao.deleteEvent(event)
    }

    suspend fun updateEvent(event: CalendarEvent) {
        calendarEventDao.updateEvent(event)
    }
}
