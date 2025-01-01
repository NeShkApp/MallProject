package org.bohdan.mallproject.presentation.viewmodel.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.usecase.home.GetAllCategoriesUseCase
import org.bohdan.mallproject.domain.usecase.home.GetSubcategoriesByCategoryUseCase
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getSubcategoriesByCategoryUseCase: GetSubcategoriesByCategoryUseCase,
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _subcategories = MutableLiveData<List<Subcategory>>()
    val subcategories: LiveData<List<Subcategory>> = _subcategories

    fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoriesList = getAllCategoriesUseCase()
                _categories.postValue(categoriesList)
            } catch (e: Exception) {
                _categories.postValue(emptyList())
            }
        }
    }

    fun loadSubcategories(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val subcategoriesList = getSubcategoriesByCategoryUseCase(category)
                _subcategories.postValue(subcategoriesList)
            } catch (e: Exception) {
                _subcategories.postValue(emptyList())
            }
        }
    }

}
