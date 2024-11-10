package org.bohdan.mallproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.usecase.home.GetAllShopItemsUseCase
import org.bohdan.mallproject.domain.usecase.home.GetShopItemByAttributesUseCase
import org.bohdan.mallproject.domain.usecase.home.GetShopItemByIdUseCase
import org.bohdan.mallproject.presentation.ui.home.SortShopItemsFragment

class AllProductsViewModel : ViewModel() {
    private val homeRepository = HomeRepositoryImpl

    private val getAllShopItemsUseCase = GetAllShopItemsUseCase(homeRepository)
    private val getShopItemByAttributes = GetShopItemByAttributesUseCase(homeRepository)

    private val _shopItems = MutableLiveData<List<ShopItem>>()
    val shopItems: LiveData<List<ShopItem>>
        get() = _shopItems

    private val _currentSortOrder = MutableLiveData<SortBy?>()
    val currentSortOrder: LiveData<SortBy?>
        get() = _currentSortOrder

    init {
        loadItemsWithCurrentSortOrder()
    }

    fun loadItemsWithCurrentSortOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = try {
                val sortOrder = _currentSortOrder.value
                if (sortOrder == null) {
                    getAllShopItemsUseCase()
                } else {
                    getShopItemByAttributes(sortOrder)
                }
            } catch (e: Exception) {
                emptyList()
            }
            _shopItems.postValue(items)
        }
    }

    fun updateSortOrder(sortBy: SortBy) {
        _currentSortOrder.value = sortBy
        loadItemsWithCurrentSortOrder()
    }

//    fun getShopItemById(shopItemId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val item = homeRepository.getShopItemById(shopItemId)
//                _shopItemById.postValue(item)
//            } catch (e: Exception) {
//                Log.e("AllProductsViewModel", "Error getting item by ID", e)
//            }
//        }
//    }

}