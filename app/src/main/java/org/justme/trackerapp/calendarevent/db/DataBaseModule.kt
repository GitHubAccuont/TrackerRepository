package org.justme.trackerapp.calendarevent.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.justme.trackerapp.calendarevent.dao.CalendarEventDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): CalendarEventDb {
        return Room.databaseBuilder(
            appContext,
            CalendarEventDb::class.java,
            "tracker_db"
        ).build()
    }

    @Provides
    fun provideCalendarEventDao(database: CalendarEventDb): CalendarEventDao {
        return database.calendarEventDao()
    }
}
