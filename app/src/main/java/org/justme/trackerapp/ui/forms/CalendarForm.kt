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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.db.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate


//TODO: Добавить возможность корректной работы с событиями, просмотр и редактирование событий на определенную дату
class CalendarForm {

    private val daysOfWeekFull = DayOfWeek.entries.toTypedArray()

    @Composable
    fun DisplayMonth(
        viewModel: CalendarViewModel = viewModel()
    ) {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            MonthAndYearDisplay(selectedDate) { newDate ->
                selectedDate = newDate
            }
            DayOfWeekNamesDisplay()
            Box(){
                DayForMonthDisplay(selectedDate, viewModel) { newDate ->
                    selectedDate = newDate
                }

            EventsForDateDisplay(viewModel,selectedDate)}

        }
    }


    //Секция под отображение месяца и года
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
            BasicMonthSelector(selectedDate) { onDateChange(it) }
            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .weight(0.4f)
            )
            BasicYearSelector(selectedDate) { onDateChange(it) }
        }
    }

    @Composable
    fun BasicMonthSelector(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onDateChange(selectedDate.minusMonths(1)) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Предыдущий месяц"
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Следующий месяц"
                )
            }
        }
    }

    @Composable
    fun BasicYearSelector(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onDateChange(selectedDate.minusYears(1)) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Предыдущий год"
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Следующий год"
                )
            }
        }
    }

    //Отображение дней недели
    @Composable
    fun DayOfWeekNamesDisplay(){
        Row(modifier = Modifier.fillMaxWidth()) {
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
                        text = day.name,
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
    }

    @Composable
    fun DayForMonthDisplay(date: LocalDate, viewModel: CalendarViewModel, onDateChange: (LocalDate) -> Unit) {
        Column {
            val lengthOfMonth = date.lengthOfMonth()
            val firstWeekDay = date.withDayOfMonth(1).dayOfWeek.ordinal

            // Лист дней с событиями (нельзя выполнить напрямую чтобы не замедлять программу вычислениями из бд в ГИ)
            LaunchedEffect(date) {
                viewModel.loadEventDaysForMonth(date)
            }

            val eventDays by viewModel.eventDays.collectAsState()
            var currentDay = 1

            while (currentDay <= lengthOfMonth) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 50.dp)
                ) {
                    for (i in currentDay..currentDay + 6) {
                        if (i in firstWeekDay + 1..lengthOfMonth) {
                            val day = i - firstWeekDay

                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onPrimary,
                                        shape = CutCornerShape(10)
                                    )
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable {
                                        onDateChange(date.withDayOfMonth(day))
                                        viewModel.viewModelScope.launch {
                                            viewModel.addEvent(day, date)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                                // Выделение дней с событиями
                                if (day in eventDays) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .align(Alignment.BottomEnd)
                                            .background(Color.Red, shape = CircleShape)
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                currentDay += 7 - (if (currentDay == 1) firstWeekDay else 0)
            }
        }
    }

    /*
    // Секция под вывод событий на выбранную дату
    */

    @Composable
    fun EventsForDateDisplay(viewModel: CalendarViewModel, date: LocalDate)
    {
//        LaunchedEffect(date) {
//            viewModel.loadEventsForDate(date)
//        }
//
//        val events by viewModel.eventDays.collectAsState()

    }
}
