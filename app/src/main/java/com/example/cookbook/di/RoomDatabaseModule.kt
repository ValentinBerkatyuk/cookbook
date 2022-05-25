package com.example.cookbook.di

import android.content.Context
import androidx.room.Room
import com.example.cookbook.database.FoodRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.example.cookbook.util.Constants
import com.example.cookbook.util.Constants.Companion.DATABASE_NAME

@Module
@InstallIn(ApplicationComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    )= Room.databaseBuilder(
        context,
        FoodRoomDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: FoodRoomDatabase)=database.foodRoomDao()
}