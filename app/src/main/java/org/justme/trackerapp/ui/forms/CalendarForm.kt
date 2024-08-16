package org.justme.trackerapp.ui.forms


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.justme.trackerapp.ui.theme.TrackerAppTheme
import java.time.DayOfWeek
import java.time.LocalDate

class CalendarForm {

    private var selectedDate: LocalDate = LocalDate.now()
    private val daysOfWeekFull = DayOfWeek.entries.toTypedArray()


    @Composable
    fun DisplayMonth() {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center) {
            MonthAndYearDisplay()
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
            DayForMonthDisplay(getMonthData())
        }
    }

    @Composable
    fun MonthAndYearDisplay() {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .background(color = Color.Transparent, shape = RectangleShape),
            verticalAlignment = Alignment.CenterVertically, // Ensure alignment in the Row
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            MonthPlaceholder()
            Spacer(modifier = Modifier.width(16.dp))
            YearPlaceholder()
        }
    }

    @Composable
    fun MonthPlaceholder() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount > 0) {
                            val newDate = selectedDate.minusMonths(1)
                        } else if (dragAmount < 0) {
                            val newDate = selectedDate.plusMonths(1)
                        }
                    }
                }
        ) {
            IconButton(
                onClick = { selectedDate = selectedDate.minusMonths(1) },
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
                onClick = { selectedDate = selectedDate.plusMonths(1) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month"
                )
            }
        }
    }

    @Composable
    fun YearPlaceholder() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { selectedDate = selectedDate.minusYears(1) },
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
                onClick = { selectedDate = selectedDate.plusYears(1) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Year"
                )
            }
        }
    }


    private fun getMonthData(): Pair<DayOfWeek, Int> {
        val date = LocalDate.of(selectedDate.year, selectedDate.month, 1)
        return Pair<DayOfWeek, Int>(
            first = date.dayOfWeek,
            second = date.lengthOfMonth()
        )
    }

    @Composable
    private fun DayForMonthDisplay(data: Pair<DayOfWeek, Int>) {

        val daysInMonth = data.second
        val firstDayOffset = data.first.ordinal

        Column {

            var currentDay = 1
            //Отображение первой недели с пустыми полями, при наличии
            WeekRow(
                currentDay = currentDay,
                monthTotalDays = daysInMonth,
                firstWeekDay = firstDayOffset
            )
            currentDay += firstDayOffset + 1

            //Остальные недели
            while (currentDay <= daysInMonth) {
                WeekRow(currentDay = currentDay, monthTotalDays = daysInMonth)
                currentDay += 7
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
    private fun WeekRow(
        currentDay: Int,
        monthTotalDays: Int,
        //Только для первой недели, чтобы пропустить пустые ячейки
        firstWeekDay: Int = 0
    ) {
        TrackerAppTheme {
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 30.dp, max = 50.dp)
            ) {
                for (i in currentDay..currentDay + 6) {
                    if (i in firstWeekDay + 1..monthTotalDays) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    shape = CutCornerShape(10)
                                )
                                .fillMaxHeight()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (i - firstWeekDay).toString(),
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
}
