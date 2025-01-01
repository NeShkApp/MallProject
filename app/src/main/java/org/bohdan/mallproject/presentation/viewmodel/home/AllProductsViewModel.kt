package org.bohdan.mallproject.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.usecase.home.GetShopItemsByFiltersUseCase
import org.bohdan.mallproject.domain.usecase.home.SortShopItemsByFiltersUseCase
import javax.inject.Inject

@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val sortShopItemsByFiltersUseCase: SortShopItemsByFiltersUseCase,
    private val getShopItemsByFiltersUseCase: GetShopItemsByFiltersUseCase,
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

    private val _currentCategory = MutableLiveData<Category?>()
    val currentCategory: LiveData<Category?>
        get() = _currentCategory

    private val _currentSubcategory = MutableLiveData<Subcategory?>()
    val currentSubcategory: LiveData<Subcategory?>
        get() = _currentSubcategory

    private val _currentSearchQuery = MutableLiveData<String?>()
    val currentSearchQuery: LiveData<String?>
        get() = _currentSearchQuery

    private val _currentOnlyDiscounts = MutableLiveData<Boolean>(false)
    val currentOnlyDiscounts: LiveData<Boolean>
        get() = _currentOnlyDiscounts


    private val _currentMinRating = MutableLiveData<Int>(0)
    val currentMinRating: LiveData<Int>
        get() = _currentMinRating

    init {
        loadShopItemsByFilters(category, subcategory, searchQuery)
        updateCurrentFilters(category, subcategory, searchQuery)
        Log.d(
            "AllProductsViewModel", "INIT was called"
        )

    }

    fun setOnlyDiscounts(isChecked: Boolean) {
        _currentOnlyDiscounts.value = isChecked
    }

    fun setMinRating(minRating: Int) {
        _currentMinRating.value = minRating
    }

    fun loadShopItemsByFilters(
        category: Category?,
        subcategory: Subcategory?,
        searchQuery: String?,
    ) {
        Log.d(
            "loadShopItemsByFilters",
            "Loading items with filters: category=$category, subcategory=$subcategory, searchQuery=$searchQuery"
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = getShopItemsByFiltersUseCase(category, subcategory, searchQuery)
                Log.d("loadShopItemsByFilters", "Loaded items: ${items.size}")

                val sortedItems = sortShopItemsByFiltersUseCase(items, _currentSortOrder.value)
                Log.d("loadShopItemsByFilters", "Sorted items: ${sortedItems.size}")


                val filteredByDiscount = filterByDiscount(sortedItems)
                val finalFilteredItems = filterByMinRating(filteredByDiscount)

                _shopItems.postValue(finalFilteredItems)

//                filterByDiscount()
//                filterByMinRating()
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

    fun updateCurrentFilters(category: Category?, subcategory: Subcategory?, searchQuery: String?) {
        _currentCategory.value = category
        _currentSubcategory.value = subcategory
        _currentSearchQuery.value = searchQuery
    }

    fun updateSearchQuery(query: String?) {
        _currentSearchQuery.value = query
    }

//    fun filterByDiscount() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val allItems = _shopItems.value
//                allItems?.let {
//                    val onlyDiscounts = _currentOnlyDiscounts.value ?: false
//                    if (onlyDiscounts) {
//                        val filteredItems = allItems.filter { it.discount > 0 }
//                        _shopItems.postValue(filteredItems)
//                    }
//                }
//            } catch (e: Exception) {
//                throw RuntimeException(e.message)
//            }
//        }
//    }
//
//    fun filterByMinRating() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val allItems = _shopItems.value
//                allItems?.let {
//                    val minRating = _currentMinRating.value ?: 0
//                    if (minRating > 0) {
//                        val filteredItems = allItems.filter { it.rating >= minRating }
//                        _shopItems.postValue(filteredItems)
//                    }
//                }
//            } catch (e: Exception) {
//                throw RuntimeException(e.message)
//            }
//        }
//    }

    fun filterByDiscount(items: List<ShopItem>): List<ShopItem> {
        val onlyDiscounts = _currentOnlyDiscounts.value ?: false
        return if (onlyDiscounts) {
            items.filter { it.discount > 0 }
        } else {
            items
        }
    }

    fun filterByMinRating(items: List<ShopItem>): List<ShopItem> {
        val minRating = _currentMinRating.value ?: 0
        return if (minRating > 0) {
            items.filter { it.rating >= minRating }
        } else {
            items
        }
    }



}