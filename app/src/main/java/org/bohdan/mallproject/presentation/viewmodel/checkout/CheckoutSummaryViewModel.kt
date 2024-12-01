package org.bohdan.mallproject.presentation.viewmodel.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.ClearCartUseCase
import org.bohdan.mallproject.domain.usecase.order.AddOrderToFirestoreUseCase
import javax.inject.Inject

@HiltViewModel
class CheckoutSummaryViewModel @Inject constructor(
    private val addOrderToFirestoreUseCase: AddOrderToFirestoreUseCase,
    private val cleanCartUseCase: ClearCartUseCase
): ViewModel() {

    fun addOrder(shopItems: List<ShopItem>){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addOrderToFirestoreUseCase(shopItems)
            } catch (e: Exception) {
                println("Error caused by: ${e.message}")
            }
        }
    }

    fun cleanCart(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                cleanCartUseCase()
            }catch (e: Exception){
                println("Error caused by: ${e.message}")
            }
        }
    }

}