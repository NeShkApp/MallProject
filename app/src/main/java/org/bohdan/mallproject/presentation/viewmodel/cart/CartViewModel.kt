package org.bohdan.mallproject.presentation.viewmodel.cart

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.CalculateTotalPriceUseCase
import org.bohdan.mallproject.domain.usecase.cart.GetCartItemsUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.UpdateSelectedQuantityUseCase
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val updateSelectedQuantityUseCase: UpdateSelectedQuantityUseCase,
    private val calculateTotalPriceUseCase: CalculateTotalPriceUseCase
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<ShopItem>>()
    val cartItems: LiveData<List<ShopItem>>
        get() = _cartItems

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> get() = _totalPrice

    // TODO: add message to user, if its selectedQuantity of product was changed
//    fun getCartItems(){
//        viewModelScope.launch(Dispatchers.IO) {
//            try{
//                val items = getCartItemsUseCase()
//                _cartItems.postValue(items)
//                _error.postValue(null)
//            }catch (e: Exception){
//                Log.e("ViewModelDebug", "Error fetching cart items", e)
//                _error.postValue(e.message)
//            }
//
//        }
//    }

    fun getCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = getCartItemsUseCase()
                val updatedItems = items.map { shopItem ->
                    when {
                        // Товар неактивний, обрана кількість більше 0
                        shopItem.quantityInStock == 0 && shopItem.selectedQuantity > 0 -> {
                            updateSelectedQuantity(shopItem.id, 0)
                            _toastMessage.postValue("${shopItem.name} is no longer available. Quantity set to 0.")
                            shopItem.copy(selectedQuantity = 0)
                        }

                        // Обрана кількість більша за доступну кількість
                        shopItem.selectedQuantity > shopItem.quantityInStock -> {
                            updateSelectedQuantity(shopItem.id, shopItem.quantityInStock)
                            _toastMessage.postValue("Only ${shopItem.quantityInStock} units of ${shopItem.name} are available. Quantity adjusted.")
                            shopItem.copy(selectedQuantity = shopItem.quantityInStock)
                        }

                        // Товар став активним, але обрана кількість дорівнює 0
                        shopItem.quantityInStock > 0 && shopItem.selectedQuantity == 0 -> {
                            updateSelectedQuantity(shopItem.id, 1)
                            _toastMessage.postValue("${shopItem.name} is now back in stock! Quantity set to 1.")
                            shopItem.copy(selectedQuantity = 1)
                        }

                        else -> shopItem // Повертаємо ShopItem без змін
                    }
                }

                _cartItems.postValue(updatedItems)
                _error.postValue(null)
            } catch (e: Exception) {
                Log.e("ViewModelDebug", "Error fetching cart items", e)
                _error.postValue(e.message)
            }
        }
    }



    fun updateSelectedQuantity(shopItemId: String, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateSelectedQuantityUseCase(shopItemId, quantity)
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

}