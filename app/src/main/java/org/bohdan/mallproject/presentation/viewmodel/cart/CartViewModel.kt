package org.bohdan.mallproject.presentation.viewmodel.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.CalculateTotalPriceUseCase
import org.bohdan.mallproject.domain.usecase.cart.GetCartItemsUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.UpdateProductQuantityInStockUseCase
import org.bohdan.mallproject.domain.usecase.cart.UpdateSelectedQuantityUseCase
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val updateSelectedQuantityUseCase: UpdateSelectedQuantityUseCase,
    private val calculateTotalPriceUseCase: CalculateTotalPriceUseCase,
    private val updateProductQuantityInStockUseCase: UpdateProductQuantityInStockUseCase
) : ViewModel() {
    var isRestored: Boolean = false

    private val _cartItems = MutableLiveData<List<ShopItem>>()
    val cartItems: LiveData<List<ShopItem>>
        get() = _cartItems

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    private val _navigateToCheckout = MutableLiveData<Boolean>(false)
    val navigateToCheckout: LiveData<Boolean> = _navigateToCheckout

    private val _isCartEmpty = MutableLiveData<Boolean>(true)
    val isCartEmpty: LiveData<Boolean> get() = _isCartEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                val items = getCartItemsUseCase()
                val updatedItems = updateItemsQuantities(items)

                _cartItems.postValue(updatedItems)
                _error.postValue(null)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateItemsQuantities(items: List<ShopItem>): List<ShopItem> {
        return items.map { shopItem ->
            when {
                shopItem.quantityInStock == 0 && shopItem.selectedQuantity > 0 -> {
                    updateSelectedQuantity(shopItem.id, 0)
                    _toastMessage.postValue("${shopItem.name} is no longer available. Quantity set to 0.")
                    shopItem.copy(selectedQuantity = 0)
                }
                shopItem.selectedQuantity > shopItem.quantityInStock -> {
                    updateSelectedQuantity(shopItem.id, shopItem.quantityInStock)
                    _toastMessage.postValue("Only ${shopItem.quantityInStock} units of ${shopItem.name} are available. Quantity adjusted.")
                    shopItem.copy(selectedQuantity = shopItem.quantityInStock)
                }
                shopItem.quantityInStock > 0 && shopItem.selectedQuantity == 0 -> {
                    updateSelectedQuantity(shopItem.id, 1)
                    _toastMessage.postValue("${shopItem.name} is now back in stock! Quantity set to 1.")
                    shopItem.copy(selectedQuantity = 1)
                }
                else -> shopItem
            }
        }
    }

    fun checkAndStartCheckout(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = getCartItemsUseCase()
                val updatedItems = updateItemsQuantities(items)

                if (items == updatedItems) {
                    _navigateToCheckout.postValue(true)
                } else {
                    _cartItems.postValue(updatedItems)
                }
            } catch (e: Exception) {
                _error.postValue("An error occurred: ${e.message}")
            }
        }
    }

    fun getAvailableCartItems(items: List<ShopItem>): List<ShopItem> {
        val updatedItems = items.filter {
            it.selectedQuantity > 0
        }
        return updatedItems
    }

    fun resetNavigateToCheckout() {
        _navigateToCheckout.value = false
    }

    fun updateSelectedQuantity(shopItemId: String, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateSelectedQuantityUseCase(shopItemId, quantity)

                _cartItems.postValue(
                    _cartItems.value?.map {
                        if (it.id == shopItemId) it.copy(selectedQuantity = quantity) else it
                    }
                )

            } catch (e: Exception) {
                Log.e("ViewModelDebug", "Error updating item quantity", e)
            }
        }
    }

    fun removeCartItem(shopItemId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                removeItemFromCartUseCase(shopItemId)
                getCartItems()
            }catch (e: Exception){
                throw RuntimeException("Item was not removed by: ${e.message}")
            }
        }
    }

    fun calculateTotalPrice(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val total = calculateTotalPriceUseCase()
                _totalPrice.postValue(total)
            }catch (e: Exception){
                throw RuntimeException("Price was not calculated: ${e.message}")
            }
        }
    }

    fun resetToastMessage(){
        _toastMessage.postValue(null)
    }

    fun reserveCartItems(items: List<ShopItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                items.forEach { item ->
                    val updatedQuantity = item.quantityInStock - item.selectedQuantity
                    updateProductQuantityInStockUseCase(item.id, updatedQuantity)
                }
            } catch (e: Exception) {
                _error.postValue("Error reserving items: ${e.message}")
            }
        }
    }

    fun releaseCartItems(items: List<ShopItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                items.forEach { item ->
                    val updatedQuantity = item.quantityInStock + item.selectedQuantity
                    updateProductQuantityInStockUseCase(item.id, updatedQuantity)
                }
            } catch (e: Exception) {
                _error.postValue("Error releasing items: ${e.message}")
            }
        }
    }

    suspend fun releaseCartItemsBlocking(items: List<ShopItem>) {
        try {
            items.forEach { item ->
                val updatedQuantity = item.quantityInStock + item.selectedQuantity
                updateProductQuantityInStockUseCase(item.id, updatedQuantity)
            }
        } catch (e: Exception) {
            _error.postValue("Error releasing items: ${e.message}")
            Log.e("CartFragment", "releaseCartItemsBlocking: ${e.message}")
        }
    }

    fun updateCartEmptyState() {
        _isCartEmpty.postValue(_cartItems.value.isNullOrEmpty())
    }

}