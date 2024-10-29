package org.bohdan.mallproject.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.home.GetAllShopItemsUseCase
import org.bohdan.mallproject.domain.usecase.home.GetShopItemByIdUseCase

class AllProductsViewModel: ViewModel() {
    private val homeRepository = HomeRepositoryImpl

    private val getAllShopItemsUseCase = GetAllShopItemsUseCase(homeRepository)

    private val _shopItems = MutableLiveData<List<ShopItem>>()
    val shopItems: LiveData<List<ShopItem>>
        get() = _shopItems

    fun getShopItems(){
        _shopItems.value = getAllShopItemsUseCase().value
    }


//    fun fetchItemsFromFirestore(){
//        viewModelScope.launch {
//            try{
//                val items = homeRepository.getAllShopItems()
//                _shopItems.postValue(items)
//            }catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//    }

}