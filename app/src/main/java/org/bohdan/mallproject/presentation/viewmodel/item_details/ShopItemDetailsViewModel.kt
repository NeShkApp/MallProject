package org.bohdan.mallproject.presentation.viewmodel.item_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.AddItemToCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.CheckIfItemInCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.favorite.AddItemToFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.favorite.IsItemInFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.favorite.RemoveItemFromFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.item_details.GetShopItemDetailsByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ShopItemDetailsViewModel @Inject constructor(
    private val getShopItemDetailsByIdUseCase: GetShopItemDetailsByIdUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val checkIfItemInCartUseCase: CheckIfItemInCartUseCase,
    private val addItemToFavoriteUseCase: AddItemToFavoriteUseCase,
    private val isItemInFavoriteUseCase: IsItemInFavoriteUseCase,
    private val removeItemFromFavoriteUseCase: RemoveItemFromFavoriteUseCase
) : ViewModel() {

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem> = _shopItem

    private val _isInCart = MutableLiveData<Boolean?>()
    val isInCart: LiveData<Boolean?> = _isInCart

    private val _isInFavorite = MutableLiveData<Boolean>(false)
    val isInFavorite: LiveData<Boolean> = _isInFavorite

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isAddToCartEnabled = MutableLiveData<Boolean>()
    val isAddToCartEnabled: LiveData<Boolean> get() = _isAddToCartEnabled

    private val _isAddToFavoriteEnabled = MutableLiveData<Boolean>()
    val isAddToFavoriteEnabled: LiveData<Boolean> get() = _isAddToFavoriteEnabled

    fun checkStockAvailability(shopItem: ShopItem) {
        _isAddToCartEnabled.value = shopItem.quantityInStock > 0
    }


    fun loadShopItemById(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val item = getShopItemDetailsByIdUseCase(shopItemId)
                _shopItem.postValue(item)
                val isFavorite = isItemInFavoriteUseCase(shopItemId)
                _isInFavorite.postValue(isFavorite)
                val isCart = checkIfItemInCartUseCase(shopItemId)
                _isInCart.postValue(isCart)
                checkStockAvailability(item)
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching item to cart: ${e.message}")
            }finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun addItemToCart(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addItemToCartUseCase(shopItemId)
                _isInCart.postValue(true)
            } catch (e: Exception) {
                _errorMessage.postValue("Error adding item to cart: ${e.message}")
            }
        }
    }

    fun removeItemFromCart(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                removeItemFromCartUseCase(shopItemId)
                _isInCart.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Error removing item from cart: ${e.message}")
            }
        }
    }

    fun addItemToFavorite(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addItemToFavoriteUseCase(shopItemId)
                _isInFavorite.postValue(true)
            } catch (e: Exception) {
                _errorMessage.postValue("Error adding item to favorite: ${e.message}")
            }
        }
    }

    fun removeItemFromFavorite(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                removeItemFromFavoriteUseCase(shopItemId)
                _isInFavorite.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue("Error removing item from favorite: ${e.message}")
            }
        }
    }



}