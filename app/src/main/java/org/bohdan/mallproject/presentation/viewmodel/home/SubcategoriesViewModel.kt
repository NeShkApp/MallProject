package org.bohdan.mallproject.presentation.viewmodel.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.repository.HomeRepository
import org.bohdan.mallproject.domain.usecase.home.GetSubcategoriesByCategoryUseCase
import javax.inject.Inject

@HiltViewModel
class SubcategoriesViewModel @Inject constructor(
    private val getSubcategoriesByCategoryUseCase: GetSubcategoriesByCategoryUseCase,
    savedStateHandle: SavedStateHandle,
//    private val category: Category
): ViewModel() {
    private val category: Category = savedStateHandle.get<Category>("category")
        ?: throw IllegalArgumentException("Category is missing")

    private val _subcategories = MutableLiveData<List<Subcategory>>()
    val subcategories: LiveData<List<Subcategory>>
        get() = _subcategories

    init{
        loadSubcategoriesByCategory(category)
    }

    private fun loadSubcategoriesByCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            val subcategories = try{
                getSubcategoriesByCategoryUseCase(category)
            }catch (e: Exception){
                emptyList()
            }
            subcategories.forEach { subcategory ->
                Log.d("SubcategoriesViewModel", "Subcategory: $subcategory")
            }
            _subcategories.postValue(subcategories)
        }
    }


}