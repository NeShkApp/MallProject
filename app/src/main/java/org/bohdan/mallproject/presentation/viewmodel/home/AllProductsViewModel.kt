package org.bohdan.mallproject.presentation.viewmodel.home

import android.app.Application
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
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.repository.HomeRepository
import org.bohdan.mallproject.domain.usecase.home.GetAllCategoriesUseCase
import org.bohdan.mallproject.domain.usecase.home.GetAllShopItemsUseCase
import org.bohdan.mallproject.domain.usecase.home.GetShopItemsByFiltersUseCase
import org.bohdan.mallproject.domain.usecase.home.GetSubcategoriesByCategoryUseCase
import org.bohdan.mallproject.domain.usecase.home.SortShopItemsByFiltersUseCase
import javax.inject.Inject

@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val sortShopItemsByFiltersUseCase: SortShopItemsByFiltersUseCase,
    private val getShopItemsByFiltersUseCase: GetShopItemsByFiltersUseCase,
    private val getSubcategoriesByCategoryUseCase: GetSubcategoriesByCategoryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllShopItemsUseCase: GetAllShopItemsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val category: Category? = savedStateHandle.get<Category>("category")
    private val subcategory: Subcategory? = savedStateHandle.get<Subcategory>("subcategory")
    private val searchQuery: String? = savedStateHandle.get<String>("searchQuery")

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _shopItems = MutableLiveData<List<ShopItem>>()
    val shopItems: LiveData<List<ShopItem>>
        get() = _shopItems

    private val _currentSortOrder = MutableLiveData<SortBy?>()
    val currentSortOrder: LiveData<SortBy?>
        get() = _currentSortOrder

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _subcategories = MutableLiveData<List<Subcategory>>()
    val subcategories: LiveData<List<Subcategory>>
        get() = _subcategories

    private val _currentCategory = MutableLiveData<Category?>()
    val currentCategory: LiveData<Category?>
        get() = _currentCategory

    private val _currentSubcategory = MutableLiveData<Subcategory?>()
    val currentSubcategory: LiveData<Subcategory?>
        get() = _currentSubcategory

    private val _currentSearchQuery = MutableLiveData<String?>()
    val currentSearchQuery: LiveData<String?>
        get() = _currentSearchQuery

    init {
        loadShopItemsByFilters(category, subcategory, searchQuery)
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoriesList = getAllCategoriesUseCase()
                _categories.postValue(categoriesList)
            } catch (e: Exception) {
                _categories.postValue(emptyList())
            }
        }
    }

    private fun loadSubcategories(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val subcategoriesList = getSubcategoriesByCategoryUseCase(category)
                _subcategories.postValue(subcategoriesList)
            } catch (e: Exception) {
                _subcategories.postValue(emptyList())
            }
        }
    }

    private fun loadShopItemsByFilters(
        category: Category?,
        subcategory: Subcategory?,
        searchQuery: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = getShopItemsByFiltersUseCase(category, subcategory, searchQuery)
                val sortedItems = sortShopItemsByFiltersUseCase(items, _currentSortOrder.value)
                _shopItems.postValue(sortedItems)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun updateSortOrder(sortBy: SortBy) {
        _currentSortOrder.value = sortBy
        val items = _shopItems.value ?: return
        val sortedItems = sortShopItemsByFiltersUseCase(items, sortBy)
        _shopItems.postValue(sortedItems)
    }


}