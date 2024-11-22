package org.bohdan.mallproject.presentation.viewmodel.cart

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.data.CartRepositoryImpl
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.CartRepository
import org.bohdan.mallproject.domain.usecase.cart.AddItemToCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.GetCartItemsUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase

class CartViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val cartRepository: CartRepository = CartRepositoryImpl(auth, firestore)

    private val getCartItemsUseCase = GetCartItemsUseCase(cartRepository)
    private val addItemToCartUseCase = AddItemToCartUseCase(cartRepository)
    private val removeItemFromCartUseCase = RemoveItemFromCartUseCase(cartRepository)

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