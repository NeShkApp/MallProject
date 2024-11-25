package org.bohdan.mallproject.presentation.viewmodel.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.GetCartItemsUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<ShopItem>>()
    val cartItems: LiveData<List<ShopItem>>
        get() = _cartItems

    init{
        getCartItems()
    }

    fun getCartItems(){
        viewModelScope.launch(Dispatchers.IO) {
            val cartItems = try{
                getCartItemsUseCase()
            }catch (e: Exception){
                emptyList()
            }
            _cartItems.postValue(cartItems)
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