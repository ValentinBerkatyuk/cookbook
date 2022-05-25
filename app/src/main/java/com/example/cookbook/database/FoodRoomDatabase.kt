package com.example.cookbook.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FoodEntity::class,FavoritesEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(FoodTypeConverter::class)
abstract class FoodRoomDatabase: RoomDatabase() {

    abstract fun foodRoomDao():FoodRoomDao
}