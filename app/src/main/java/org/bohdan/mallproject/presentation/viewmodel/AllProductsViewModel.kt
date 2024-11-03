package org.bohdan.mallproject.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.home.GetAllShopItemsUseCase
import org.bohdan.mallproject.domain.usecase.home.GetShopItemByIdUseCase

class AllProductsViewModel : ViewModel() {
    private val homeRepository = HomeRepositoryImpl

    private val getAllShopItemsUseCase = GetAllShopItemsUseCase(homeRepository)

    private val _shopItems = MutableLiveData<List<ShopItem>>()
    val shopItems: LiveData<List<ShopItem>>
        get() = _shopItems

    private val _shopItemById = MutableLiveData<ShopItem>()
    val shopItemById: LiveData<ShopItem>
        get() = _shopItemById

    fun getAllShopItems() {
        viewModelScope.launch(Dispatchers.IO) { //for launch async method in IO thread,
            // which is optimal for internet requests and databases operations
            val items = getAllShopItemsUseCase()
            _shopItems.postValue(items)
        }
    }

    fun getShopItemById(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = homeRepository.getShopItemById(shopItemId)
            _shopItemById.postValue(item)
        }
    }

}