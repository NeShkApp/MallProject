package org.bohdan.mallproject.presentation.viewmodel.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.favorite.GetFavoriteItemsUseCase
import org.bohdan.mallproject.domain.usecase.favorite.IsItemInFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.favorite.RemoveItemFromFavoriteUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteItemsUseCase: GetFavoriteItemsUseCase,
    private val removeItemFromFavoriteUseCase: RemoveItemFromFavoriteUseCase
): ViewModel(){
    private val _favoriteItems = MutableLiveData<List<ShopItem>>()
    val favoriteItems: LiveData<List<ShopItem>>
        get() = _favoriteItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    init{
        loadFavoriteItems()
    }

    private fun loadFavoriteItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try{
                val items = getFavoriteItemsUseCase()
                Log.d("FavoriteViewModel", "Loaded items: $items")
                _favoriteItems.postValue(items)
            }catch (e: Exception){
                _errorMessage.postValue("Items downloading failed: ${e.message}")
            }finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun removeFavoriteItem(shopItemId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                removeItemFromFavoriteUseCase(shopItemId)
                loadFavoriteItems()
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to remove an item $shopItemId: ${e.message}")
            }
        }
    }

    fun resetErrorMessage(){
        _errorMessage.postValue(null)
    }
}