package com.lnight.material3clock.di

import android.content.Context
import androidx.room.Room
import com.lnight.material3clock.core.ClockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClockDatabase(@ApplicationContext context: Context): ClockDatabase {
        return Room.databaseBuilder(
            context,
            ClockDatabase::class.java,
            ClockDatabase.DATABASE_NAME
        ).build()
    }

}