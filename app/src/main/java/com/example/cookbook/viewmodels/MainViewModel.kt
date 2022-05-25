package com.example.cookbook.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.cookbook.data.network.Repository
import com.example.cookbook.database.FavoritesEntity
import com.example.cookbook.database.FoodEntity
import com.example.cookbook.models.FoodRecipe
import com.example.cookbook.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    //Room
    val readRecipes: LiveData<List<FoodEntity>> = repository.local.readDatabase().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()


    private fun insertRecipes(foodEntity: FoodEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodRecipe(foodEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }


    //retrofit
    var recipesGet: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesCall(searchQuery)
    }

    private suspend fun getRecipesCall(queries: Map<String, String>) {
        recipesGet.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesGet.value = handleFoodRecipesResponse(response)
                val recipe = recipesGet.value!!.data
                if (recipe != null) {
                    cacheFood(recipe)
                }
            } catch (e: Exception) {
                recipesGet.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipesGet.value = NetworkResult.Error("No internet Connection")
        }
    }

    private fun cacheFood(foodRecipe: FoodRecipe) {
        val foodEntity = FoodEntity(foodRecipe)
        insertRecipes(foodEntity)
    }

    private suspend fun searchRecipesCall(searchQuery: Map<String, String>) {
        searchResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            searchResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }


    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("error with API key")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

