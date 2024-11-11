package org.bohdan.mallproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.usecase.home.GetAllCategoriesUseCase

class CategoriesViewModel: ViewModel() {
    private val homeRepository = HomeRepositoryImpl

    private val getAllCategories = GetAllCategoriesUseCase(homeRepository)

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    init{
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = try {
                getAllCategories()
            } catch (e: Exception) {
                emptyList()
            }
            _categories.postValue(categories)
        }
    }

}