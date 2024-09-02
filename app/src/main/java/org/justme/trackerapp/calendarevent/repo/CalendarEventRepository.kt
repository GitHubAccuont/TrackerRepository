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

        // Все повторяющиеся события
        val repeatingEvents = calendarEventDao.getEventsForDate(monthStart, monthEnd)

        // Событий проходящие в тот же день недели что и дата для поиска
        val intersectingEvents = mutableListOf<CalendarEvent>()

        repeatingEvents.forEach { event ->
            // Поиск по совпадению дня недели у повторяющихся событий
            if (event.repeatInterval == RepeatEnum.DAILY) {
                intersectingEvents.add(event)
            } else if (event.date.dayOfWeek == date.dayOfWeek) {
                intersectingEvents.add(event)
            }
        }

        intersectingEvents.addAll(calendarEventDao.getEventsForDate(date))

        return intersectingEvents
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

    suspend fun updateOutDatedEvents(currentDay: LocalDate) {

        calendarEventDao.deleteOutdatedEvents(currentDay)

        val currentDate = LocalDate.now()
        val events = calendarEventDao.getRepeatingEvents()

        //Обновление дат для уже прошедших повторяющихся событий
        events
            .filter { it.date < currentDate }
            .forEach { event ->
            event.date = when (event.repeatInterval) {
                RepeatEnum.DAILY -> currentDate

                RepeatEnum.WEEKLY -> {
                    val daysUntilNextOccurrence = (event.date.dayOfWeek.ordinal - currentDate.dayOfWeek.ordinal).toLong()
                    currentDate.plusDays(if (daysUntilNextOccurrence >= 0) daysUntilNextOccurrence else daysUntilNextOccurrence + 7)
                }

                RepeatEnum.MONTHLY -> {
                    var updatedDate = LocalDate.of(currentDate.year, currentDate.month, event.date.dayOfMonth)
                    if (updatedDate < currentDate) updatedDate = updatedDate.plusMonths(1)
                    updatedDate
                }

                RepeatEnum.YEARLY -> {
                    var updatedDate = event.date.withYear(currentDate.year)
                    if (updatedDate < currentDate) updatedDate = updatedDate.plusYears(1)
                    updatedDate
                }

                else -> event.date
            }
            calendarEventDao.updateEvent(event)
        }
    }
}
