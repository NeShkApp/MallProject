package org.bohdan.mallproject.presentation.viewmodel.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.data.HomeRepositoryImpl.sortShopItems
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.usecase.home.GetAllShopItemsUseCase
import org.bohdan.mallproject.domain.usecase.home.GetShopItemsByFiltersUseCase

class AllProductsViewModel(
    private val application: Application,
    private val category: Category?,
    private val subcategory: Subcategory?,
    private val searchQuery: String?
) : ViewModel() {
    private val homeRepository = HomeRepositoryImpl

    private val getAllShopItemsUseCase = GetAllShopItemsUseCase(homeRepository)
    private val getShopItemsByFilters = GetShopItemsByFiltersUseCase(homeRepository)

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

    //new
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
//        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoriesList = homeRepository.getAllCategories()
                _categories.postValue(categoriesList)
            } catch (e: Exception) {
                _categories.postValue(emptyList())
            }
        }
    }

    private fun loadSubcategories(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val subcategoriesList = homeRepository.getSubcategoriesByCategory(category)
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
    ){
        viewModelScope.launch(Dispatchers.IO){
            val items = try {
                getShopItemsByFilters(category, subcategory, searchQuery)
            } catch (e: Exception) {
                emptyList()
            }
            val sortedItems = sortShopItems(items, _currentSortOrder.value)
            _shopItems.postValue(sortedItems)
        }
    }

    // TODO: change to only sort the list and not to find by filters for second time!
    fun updateSortOrder(sortBy: SortBy) {
        _currentSortOrder.value = sortBy
        loadShopItemsByFilters(
            category, subcategory, searchQuery
        )
        //sort
    }

}