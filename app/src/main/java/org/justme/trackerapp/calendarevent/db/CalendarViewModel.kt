package org.justme.trackerapp.calendarevent.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.data.EventUpdateData
import org.justme.trackerapp.calendarevent.repo.CalendarEventRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val calendarEventRepository: CalendarEventRepository
) : ViewModel() {

    //Номера дней с событиями для выбранного месяца
    private val _eventDays = MutableStateFlow<List<Int>>(emptyList())
    val eventDays: StateFlow<List<Int>> = _eventDays

    //Список событий для выбранного дня
    private val _eventsForDate = MutableStateFlow<List<CalendarEvent>>(emptyList())
    val eventsForDate: StateFlow<List<CalendarEvent>> = _eventsForDate

    //Внутренняя переменная для сохранения/изменения даты
    private val _selectedDate = MutableStateFlow<LocalDate?>(LocalDate.now())

    //TODO: Убрать если не будет нужно для работы
    val selectedDate: StateFlow<LocalDate?> = _selectedDate

    private fun loadEventDaysForMonth() {
        viewModelScope.launch {
            _selectedDate.value?.let { date ->
                val days = calendarEventRepository.getEventsForMonth(date)
                _eventDays.value = days
            }
        }
    }

    private fun loadEventsForDay() {
        viewModelScope.launch {
            _selectedDate.value?.let { date ->
                val events = calendarEventRepository.getEventsForDate(date)
                _eventsForDate.value = events
            }
        }
    }

    fun updateEvent(oldEvent: CalendarEvent, newEventData: EventUpdateData) {
        viewModelScope.launch {
            calendarEventRepository.updateEvent(oldEvent)
        }
    }

    fun addEvent(event: CalendarEvent) {
        viewModelScope.launch {
            calendarEventRepository.saveNewEvent(event)
        }
    }

    fun removeEvent(event: CalendarEvent) {
        viewModelScope.launch {
            calendarEventRepository.deleteEvent(event)
            loadEventsForDay()
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadEventsForDay()
        loadEventDaysForMonth()
    }
}