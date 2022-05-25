package com.example.cookbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cookbook.models.FoodRecipe
import com.example.cookbook.util.Constants.Companion.FOOD_TABLE

@Entity(tableName = FOOD_TABLE)
class FoodEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id:Int=0
}