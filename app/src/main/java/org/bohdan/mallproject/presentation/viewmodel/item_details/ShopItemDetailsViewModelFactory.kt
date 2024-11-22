package org.bohdan.mallproject.presentation.viewmodel.item_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bohdan.mallproject.domain.usecase.cart.AddItemToCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.CheckIfItemInCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.item_details.GetShopItemDetailsByIdUseCase

class ShopItemDetailsViewModelFactory(
    private val shopItemId: String,
    private val getShopItemDetailsUseCase: GetShopItemDetailsByIdUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val checkIfItemInCartUseCase: CheckIfItemInCartUseCase,
//    private val addItemToFavoritesUseCase: AddItemToFavoritesUseCase,
//    private val removeItemFromFavoritesUseCase: RemoveItemFromFavoritesUseCase,
//    private val checkIfItemInFavorites: CheckIfItemInFavoritesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopItemDetailsViewModel::class.java)) {
            return ShopItemDetailsViewModel(
                shopItemId,
                getShopItemDetailsUseCase,
                addItemToCartUseCase,
                removeItemFromCartUseCase,
                checkIfItemInCartUseCase,
//                addItemToFavoritesUseCase,
//                removeItemFromFavoritesUseCase,
//                checkIfItemInFavorites
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
