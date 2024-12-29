package org.bohdan.mallproject.presentation.viewmodel.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.item_details.GetShopItemDetailsByIdUseCase
import org.bohdan.mallproject.domain.usecase.order.AddOrderToFirestoreUseCase
import org.bohdan.mallproject.domain.usecase.order.GetOrdersFromFirestoreUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersFromFirestoreUseCase: GetOrdersFromFirestoreUseCase,
): ViewModel(){
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ordersList = getOrdersFromFirestoreUseCase()
                _orders.postValue(ordersList)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}