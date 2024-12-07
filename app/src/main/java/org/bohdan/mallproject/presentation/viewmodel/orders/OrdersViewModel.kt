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
    private val getShopItemDetailsByIdUseCase: GetShopItemDetailsByIdUseCase
): ViewModel(){
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadOrders()
    }


    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }


//    fun loadOrders() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val ordersList = getOrdersFromFirestoreUseCase()
//
//                val ordersWithDetails = ordersList.map { order ->
//                    order.copy(formattedTimestamp = formatTimestamp(order.timestamp))
//                    // Для кожного замовлення, отримуємо деталі продуктів
//                    val productsWithDetails = order.productsWithQuantities.map { productData ->
//                        val shopItemId = productData["shopItemId"].toString()
//                        val quantity = productData["quantity"] as? Long ?: 0
//
//                        val shopItem = getShopItemDetailsByIdUseCase(shopItemId)
//
//                        // Повертаємо ShopItem з кількістю
//                        shopItem.copy(selectedQuantity = quantity.toInt())
//                    }
//
//                    // Повертаємо оновлене замовлення з продуктами
//                    order.copy(shopItems = productsWithDetails)
//                }
//
//                _orders.postValue(ordersWithDetails)
//            } catch (e: Exception) {
//                _error.postValue(e.message)
//            }
//        }
//    }

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