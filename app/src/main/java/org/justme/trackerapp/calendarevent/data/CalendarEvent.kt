package org.justme.trackerapp.calendarevent.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "calendar_event")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "type") val name: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "repeat_interval") val repeatInterval: RepeatEnum,
    @ColumnInfo(name = "time") val time: LocalTime,
    @ColumnInfo(name = "repeat_end_date") val repeatEndDate: LocalDate? = null,
    @ColumnInfo(name = "repeat_frequency") val repeatFrequency: Int = 1
)

enum class RepeatEnum {
    DAILY, WEEKLY, MONTHLY, YEARLY, NONE
}

//TODO : Исправить конвертацию нумератора
class RepeatEnumConverter {

    @TypeConverter
    fun fromRepeatEnum(repeatEnum: RepeatEnum): String {
        return repeatEnum.name
    }

    @TypeConverter
    fun toRepeatEnum(repeatString: String): RepeatEnum {
        return RepeatEnum.valueOf(repeatString)
    }
}
