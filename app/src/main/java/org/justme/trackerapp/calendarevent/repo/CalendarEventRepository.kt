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

        val monthStart = date.withDayOfMonth(1)
        val monthEnd = date.withDayOfMonth(date.lengthOfMonth())

        // Все события повотряющиеся каждую неделю
        val weeklyEvents = calendarEventDao.getWeeklyEventsForDate(monthStart, monthEnd)

        // Событий проходящие в тот же день недели что и дата для поиска
        val intersectingWeeklyEvents = mutableListOf<CalendarEvent>()
        weeklyEvents.forEach {event ->
            // Поиск по совпадению дня недели у повторяющихся событий
            if(event.date.dayOfWeek == date.dayOfWeek) {
                intersectingWeeklyEvents.add(event)
            }
    }
        intersectingWeeklyEvents.addAll(calendarEventDao.getEventsForDate(date))

        return intersectingWeeklyEvents
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

    suspend fun saveNewEvent(event: CalendarEvent) {
        return calendarEventDao.insertEvent(event)
    }

    suspend fun deleteEvent(event: CalendarEvent) {
        calendarEventDao.deleteEvent(event)
    }

    suspend fun updateOutDatedEvents(currentDay: LocalDate){

        calendarEventDao.deleteOutdatedEvents(currentDay)
        calendarEventDao.updateWeeklyRepeatingEvents(currentDay)
        calendarEventDao.updateMonthlyRepeatingEvents(currentDay)
        calendarEventDao.updateYearlyRepeatingEvents(currentDay)
    }
}
