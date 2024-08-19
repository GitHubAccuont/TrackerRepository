package org.justme.trackerapp.ui.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.justme.trackerapp.ui.theme.TrackerAppTheme
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarForm {

    private val daysOfWeekFull = DayOfWeek.entries.toTypedArray()

    @Composable
    fun DisplayMonth() {

        var selectedDate by remember { mutableStateOf(LocalDate.of(LocalDate.now().year,LocalDate.now().month,1)) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            MonthAndYearDisplay(selectedDate) { newDate ->
                selectedDate = newDate
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                daysOfWeekFull.forEach { day ->
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .background(
                                color = Color(0xFFBB86FC),
                                shape = CutCornerShape(10)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }
            DayForMonthDisplay(
                selectedDate
            )
        }
    }

    data class CalendarEvent(
        val name: String,
        val date: LocalDate,
        val color: Color,
        val description: String = "",
        val category: String = ""
    )

    val eventsByYearAndMonth: Map<Int, Map<Int, List<CalendarEvent>>> = mapOf(
        2024 to mapOf(
            8 to listOf(
                CalendarEvent("Meeting", LocalDate.of(2024, 8, 5), Color.Red),
                CalendarEvent("Conference", LocalDate.of(2024, 8, 12), Color.Blue)
            ),
            9 to listOf(
                CalendarEvent("Workshop", LocalDate.of(2024, 9, 18), Color.Green)
            )
        )
    )

    fun getEventsForMonth(year: Int, month: Int): List<CalendarEvent> {
        return eventsByYearAndMonth[year]?.get(month).orEmpty()
    }

    fun getEventsGroupedByDay(year: Int, month: Int): Map<Int, List<CalendarEvent>> {
        return getEventsForMonth(year, month).groupBy { it.date.dayOfMonth }
    }


    @Composable
    fun MonthAndYearDisplay(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .background(color = Color.Transparent, shape = RectangleShape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            //TODO: Добавить возможность свайпа к месяцу и году, при нажатии вызвать нормальные инструменты для выбора
            MonthPlaceholder(selectedDate) { onDateChange(it) }
            Spacer(modifier = Modifier.width(16.dp).weight(0.4f))
            YearPlaceholder(selectedDate) { onDateChange(it) }
        }
    }

    @Composable
    fun MonthPlaceholder(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onDateChange(selectedDate.minusMonths(1)) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month"
                )
            }
            Text(
                text = "${selectedDate.month}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(
                onClick = { onDateChange(selectedDate.plusMonths(1)) },
                modifier = Modifier.padding(end = 8.dp),

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month"
                )
            }
        }
    }

    @Composable
    fun YearPlaceholder(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onDateChange(selectedDate.minusYears(1)) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Year"
                )
            }

            Text(
                text = "${selectedDate.year}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = { onDateChange(selectedDate.plusYears(1)) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Year"
                )
            }
        }
    }

    @Composable
    fun DayForMonthDisplay(date: LocalDate) {
        //TODO: Нужно переделать, чтобы события обновлялись соответствующе с выбранным месяцем
        val (events, setEvents) = rememberSaveable {
            mutableStateOf(getEventsForMonth(date.year, date.monthValue).toMutableList())
        }

        fun addEvent(day: Int) {
            val newEvent = CalendarEvent(
                name = "New Event",
                date = LocalDate.of(date.year, date.month, day),
                color = Color.Black
            )
            setEvents(events.apply { add(newEvent) })
        }

        Column {
            val lengthOfMonth = date.lengthOfMonth()
            val firstWeekDay = date.withDayOfMonth(1).dayOfWeek.ordinal
            var currentDay = 1
            while (currentDay <= lengthOfMonth) {
                WeekRow(
                    currentDay = currentDay,
                    monthTotalDays = lengthOfMonth,
                    firstWeekDay = if (currentDay == 1) firstWeekDay else 0,
                    events = events,
                    onDayClick = ::addEvent
                )
                currentDay += 7 - (if (currentDay == 1) firstWeekDay else 0)
            }
        }
    }

    @Preview
    @Composable
    private fun PreviewMonth() {
        TrackerAppTheme {
            DisplayMonth()
        }
    }

    @Composable
    fun WeekRow(
        currentDay: Int,
        monthTotalDays: Int,
        firstWeekDay: Int = 0,
        events: List<CalendarEvent>,
        onDayClick: (Int) -> Unit
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 30.dp, max = 50.dp)
        ) {
            for (i in currentDay..currentDay + 6) {
                if (i in firstWeekDay + 1..monthTotalDays) {
                    val day = i - firstWeekDay
                    val event = events.find { it.date.dayOfMonth == day }

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = event?.color ?: MaterialTheme.colorScheme.onPrimary,
                                shape = CutCornerShape(10)
                            )
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                onDayClick(day)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.toString(),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
