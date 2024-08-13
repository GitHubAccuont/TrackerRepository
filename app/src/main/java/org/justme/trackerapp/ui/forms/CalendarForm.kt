package org.justme.trackerapp.ui.forms


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year

class CalendarForm {

    private val daysOfWeekFull =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    @Preview
    @Composable
    private fun DayOfWeekGrid() {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
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
                            text = day.take(3),
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
            MonthAndDaysBox(Month.JULY, Year.of(2024))
        }
    }

    @Composable
    private fun MonthNameDisplay(month: Month) {
        Box(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .background(
                    color = Color.Transparent,
                    shape = RectangleShape
                )
        ) {
            Text(
                text = month.toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
        }
    }

    @Composable
    private fun MonthAndDaysBox(month: Month, year: Year) {
        MonthNameDisplay(month)
        DayForMonthDisplay(getMonthData(month, year))
    }

    private fun getMonthData(month: Month, year: Year): Pair<DayOfWeek, Int> {
        val date = LocalDate.of(year.value, month, 1)
        return Pair<DayOfWeek, Int>(
            first = date.dayOfWeek,
            second = date.lengthOfMonth()
        )
    }

    @Composable
    private fun DayForMonthDisplay(data: Pair<DayOfWeek, Int>) {
        val totalDaysInWeek = 7
        val daysInMonth = data.second
        val firstDayOffset = data.first.ordinal

        Column {
            // First row with leading spaces and days of the first week
            Row(modifier = Modifier.fillMaxWidth()) {
                // Add leading spaces for days before the first day of the month
                repeat(firstDayOffset) {
                    Spacer(modifier = Modifier.size(40.dp))
                }

                // Add days of the first week
                for (day in 1..(totalDaysInWeek - firstDayOffset)) {
                    if (day <= daysInMonth) {
                        DayBox(dayNumber = day)
                    } else {
                        Spacer(modifier = Modifier.size(40.dp)) // Fill remaining space
                    }
                }
            }

            // Subsequent rows
            var currentDay = totalDaysInWeek - firstDayOffset + 1
            while (currentDay <= daysInMonth) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayOfWeek in 0 until totalDaysInWeek) {
                        if (currentDay <= daysInMonth) {
                            DayBox(dayNumber = currentDay)
                            currentDay++
                        } else {
                            Spacer(modifier = Modifier.size(40.dp)) // Fill remaining space
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayBox(
    dayNumber: Int,
    isTransparent: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(
                color = if (isTransparent) Color.Transparent else Color.LightGray,
                shape = CutCornerShape(10)
            )
            .size(45.dp),  // Fixed size to prevent expanding
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayNumber.toString(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}