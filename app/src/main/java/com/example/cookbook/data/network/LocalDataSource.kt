package com.example.cookbook.data.network

import com.example.cookbook.database.FavoritesEntity
import com.example.cookbook.database.FoodEntity
import com.example.cookbook.database.FoodRoomDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val foodRoomDao: FoodRoomDao
) {
    fun readDatabase(): Flow<List<FoodEntity>> {
        return foodRoomDao.readFood()
    }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return foodRoomDao.readFavoriteRecipes()
    }

    suspend fun insertFoodRecipe(foodEntity: FoodEntity) {
        foodRoomDao.insertFood(foodEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        foodRoomDao.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) {
        foodRoomDao.deleteFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        foodRoomDao.deleteAllFavoriteRecipes()
    }
}