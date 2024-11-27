package org.bohdan.mallproject.presentation.viewmodel.cart

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.GetCartItemsUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.UpdateSelectedQuantityUseCase
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val updateSelectedQuantityUseCase: UpdateSelectedQuantityUseCase,
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<ShopItem>>()
    val cartItems: LiveData<List<ShopItem>>
        get() = _cartItems

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage


    init{
        getCartItems()
    }

    // TODO: add message to user, if its selectedQuantity of product was changed
    fun getCartItems(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val items = getCartItemsUseCase()
                _cartItems.postValue(items)
                _error.postValue(null)
            }catch (e: Exception){
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

}