package com.example.cookbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cookbook.models.Result
import com.example.cookbook.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)