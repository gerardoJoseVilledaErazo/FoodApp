package com.example.foodapp.ui.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.repository.FoodRepository

class FoodViewModelProviderFactory(
    val app: Application,
    private val foodRepository: FoodRepository//se hizo private
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FoodViewModel(app, foodRepository) as T
    }

}