package org.justme.trackerapp.calendarevent.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class CalendarEventConverter {

    //Конвертер для нумератора
    @TypeConverter
    fun fromRepeatEnum(repeatEnum: RepeatEnum): String {
        return repeatEnum.name
    }

    @TypeConverter
    fun toRepeatEnum(repeatString: String): RepeatEnum {
        return RepeatEnum.valueOf(repeatString)
    }

    //Время
    @TypeConverter
    fun fromLocalTime(time: LocalTime): Long {
        return time.toSecondOfDay().toLong()
    }

    @TypeConverter
    fun toLocalTime(seconds: Long): LocalTime {
        return LocalTime.ofSecondOfDay(seconds)
    }

    //День
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDays: Long): LocalDate {
        return LocalDate.ofEpochDay(epochDays)
    }
}