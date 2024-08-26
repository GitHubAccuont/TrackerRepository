package org.justme.trackerapp.calendarevent.db

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.data.RepeatEnum
import org.justme.trackerapp.calendarevent.repo.CalendarEventRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarEventRepository: CalendarEventRepository
) : ViewModel() {
    fun getEventDaysForMonth(date: LocalDate): List<Int> {
        return calendarEventRepository.getEventsForMonth(date)
    }

    suspend fun addEvent(day: Int, date: LocalDate) {
        val event: CalendarEvent = CalendarEvent(
            date = date.withDayOfMonth(day),
            repeatFrequency = 1,
            repeatInterval = RepeatEnum.NONE,
            name = "Тестовое значение",
            details = "Тестовые данные",
            time = LocalTime.now(),
            repeatEndDate = LocalDate.now()
        )
        calendarEventRepository.uploadEvent(event)
    }

    suspend fun removeEvent(event: CalendarEvent) {
        calendarEventRepository.deleteEvent(event)
    }

    fun updateEvent(oldEvent: CalendarEvent, newEvent: CalendarEvent) {
        // Обновление позде
    }
}
