package org.justme.trackerapp.ui.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import org.justme.trackerapp.calendarevent.data.CalendarEvent
import org.justme.trackerapp.calendarevent.data.RepeatEnum
import org.justme.trackerapp.calendarevent.db.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime


class CalendarForm(private val viewModel: CalendarViewModel) {

    private val daysOfWeekFull = DayOfWeek.entries.toTypedArray()

    private val repeatEnumValues = RepeatEnum.entries.toTypedArray()

    @Composable
    fun DisplayMonth() {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        viewModel.selectDate(selectedDate)

        // Структура календарика
        Column(modifier = Modifier.fillMaxSize()) {

            // Выбор даты и название дней недели
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .wrapContentHeight()
            ) {
                // Отображение месяца и года и их выбор
                MonthAndYearDisplay(selectedDate) { newDate ->
                    selectedDate = newDate
                }
                // Названия дней недели
                DayOfWeekNamesDisplay()
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                // Дни месяца
                DaysForMonthDisplay(selectedDate) { newDate ->
                    selectedDate = newDate
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    // События на выбранный день
                    EventsForDateDisplay(selectedDate)
                }
            }

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
                color = MaterialTheme.colorScheme.onBackground,
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
    fun DayOfWeekNamesDisplay() {
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeekFull.forEach { day ->
                Box(
                    Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
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
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1
                    )
                }
            }
        }
    }

    //Отображение дней месяца
    @Composable
    fun DaysForMonthDisplay(
        date: LocalDate,
        onDateChange: (LocalDate) -> Unit
    ) {
        //Колонка с неделями в виде строк
        Column {
            val lengthOfMonth = date.lengthOfMonth()
            val firstWeekDay = date.withDayOfMonth(1).dayOfWeek.ordinal
            val totalDays =
                lengthOfMonth + firstWeekDay // Длина месяца с учетом разницы в начало первой недели
            val rows = (totalDays + 6) / 7 //Число неделей в месяце

            val eventDays by viewModel.eventDays.collectAsState()
            val selectedDay = date.dayOfMonth

            // Строка с неделей
            for (week in 0 until rows) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp, max = 50.dp)
                ) {
                    // Проходимся по дням для каждой неделе
                    for (dayOfWeek in 0 until 7) {
                        val dayIndex = week * 7 + dayOfWeek
                        val day = dayIndex - firstWeekDay + 1 // Расчет отображаемого дня

                        if (day in 1..lengthOfMonth) { // Ограничение по числу дней
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        color = if (day == selectedDay) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                        shape = CutCornerShape(10)
                                    )
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable {
                                        onDateChange(date.withDayOfMonth(day))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                // Подсветка дней в которых есть события
                                if (day in eventDays) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .align(Alignment.TopEnd)
                                            .background(
                                                MaterialTheme.colorScheme.secondary,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    /*
    // Секция под вывод событий на выбранную дату
    */
    @Composable
    fun EventsForDateDisplay(date: LocalDate) {

        val events by viewModel.eventsForDate.collectAsState()
        val sortedEvents = events.sortedBy { it.time }
        // Видимость формы для создания событий
        var showForm by remember { mutableStateOf(false) }


        Box {
            if (showForm) {
                EventCreationForm(
                    selectedDate = date,
                    onSave = { event ->
                        viewModel.addEvent(event)
                        showForm = false
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(sortedEvents) { event ->
                        EventRow(event)
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    showForm = !showForm
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                if (showForm)
                    Icon(Icons.Default.Close, contentDescription = "Скрыть")
                else
                    Icon(Icons.Default.Add, contentDescription = "Добавить событие")
            }
        }
    }


    // Шаблон для столбика с событием
    @Composable
    fun EventRow(event: CalendarEvent) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp)
                .fillMaxHeight(0.2f)
        ) {
            Text(
                text = event.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
                    .fillMaxHeight(0.2f),
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(4.dp)
                        .fillMaxHeight(0.8f)
                ) {
                    Text(
                        text = "${event.time.hour}:${event.time.minute}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Text(
                    text = event.details,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
                FloatingActionButton(
                    onClick = {
                        viewModel.removeEvent(event)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.Transparent)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Добавить событие")
                }
            }
        }
    }

    //Форма для создания новых событий
    @Composable
    fun EventCreationForm(
        selectedDate: LocalDate,
        onSave: (CalendarEvent) -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var details by remember { mutableStateOf("") }
        var time by remember { mutableStateOf(LocalTime.now()) }
        var repeatInterval by remember { mutableStateOf(RepeatEnum.NONE) }
        var repeatEndDate by remember { mutableStateOf<LocalDate?>(null) }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                "Данные события",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = details,
                onValueChange = { details = it },
                label = { Text("Описание") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TimePicker(
                time = time,
                onTimeChange = { time = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            RepeatIntervalPicker() { repeatInterval = it }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val newEvent = CalendarEvent(
                    date = selectedDate,
                    name = name,
                    details = details,
                    time = time,
                    repeatInterval = repeatInterval,
                    repeatEndDate = repeatEndDate
                )
                onSave(newEvent)
            }) {
                Text(
                    "Добавить",
                    modifier = Modifier.align(Alignment.Bottom),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    // Выбор времени событий
    @Composable
    fun TimePicker(time: LocalTime, onTimeChange: (LocalTime) -> Unit) {

    }

    // Выбор интервала повторений у события
    @Composable
    fun RepeatIntervalPicker(onSelectionChange: (RepeatEnum) -> Unit) {
        var selectedIndex by remember { mutableStateOf(0) }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(repeatEnumValues) { item ->
                Text(
                    text = item.name,
                    fontSize = 20.sp,
                    fontWeight = if (selectedIndex == repeatEnumValues.indexOf(item)) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedIndex == repeatEnumValues.indexOf(item)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            selectedIndex = repeatEnumValues.indexOf(item)
                            onSelectionChange(item)
                        }
                )
            }
        }
    }
}
