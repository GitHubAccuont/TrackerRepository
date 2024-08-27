package org.justme.trackerapp.calendarevent.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.data.EventUpdateData
import org.justme.trackerapp.calendarevent.data.RepeatEnum
import org.justme.trackerapp.calendarevent.repo.CalendarEventRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarEventRepository: CalendarEventRepository
) : ViewModel() {

    private val _eventDays = MutableStateFlow<List<Int>>(emptyList())
    val eventDays: StateFlow<List<Int>> = _eventDays

    private val _eventsForDate = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val eventsForDate: StateFlow<List<CalendarEvent>> = _eventsForDate

    fun loadEventDaysForMonth(date: LocalDate) {
        viewModelScope.launch {
            val days = calendarEventRepository.getEventsForMonth(date)
            _eventDays.value = days
        }
    }

    fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            val events = calendarEventRepository.getEventsForDate(date)
            _eventsForDate.value = events
        }
    }

    fun updateEvent(oldEvent: CalendarEvent, newEventData: EventUpdateData) {
        viewModelScope.launch {

            calendarEventRepository.updateEvent(oldEvent)
        }
    }

    fun addEvent(day: Int, date: LocalDate) {
        viewModelScope.launch {
            val event = CalendarEvent(
                date = date.withDayOfMonth(day),
                repeatFrequency = 1,
                repeatInterval = RepeatEnum.NONE,
                name = "Test Event",
                details = "Test Data",
                time = LocalTime.now(),
                repeatEndDate = LocalDate.now()
            )
            calendarEventRepository.saveNewEvent(event)
            loadEventDaysForMonth(date)
        }
    }

    fun removeEvent(event: CalendarEvent) {
        viewModelScope.launch {
            calendarEventRepository.deleteEvent(event)
            loadEventsForDate(event.date)
        }
    }
}
