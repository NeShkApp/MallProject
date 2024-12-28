package org.bohdan.mallproject.presentation.viewmodel.item_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.model.Comment
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.AddItemToCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.CheckIfItemInCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.favorite.AddItemToFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.favorite.IsItemInFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.favorite.RemoveItemFromFavoriteUseCase
import org.bohdan.mallproject.domain.usecase.item_details.CanUserLeaveCommentUseCase
import org.bohdan.mallproject.domain.usecase.item_details.GetShopItemCommentsUseCase
import org.bohdan.mallproject.domain.usecase.item_details.GetShopItemDetailsByIdUseCase
import org.bohdan.mallproject.domain.usecase.item_details.SubmitReviewUseCase
import org.bohdan.mallproject.domain.usecase.item_details.UpdateRatingUseCase
import javax.inject.Inject

@HiltViewModel
class ShopItemDetailsViewModel @Inject constructor(
    private val getShopItemDetailsByIdUseCase: GetShopItemDetailsByIdUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val checkIfItemInCartUseCase: CheckIfItemInCartUseCase,
    private val addItemToFavoriteUseCase: AddItemToFavoriteUseCase,
    private val isItemInFavoriteUseCase: IsItemInFavoriteUseCase,
    private val removeItemFromFavoriteUseCase: RemoveItemFromFavoriteUseCase,
    private val getShopItemCommentsUseCase: GetShopItemCommentsUseCase,
    private val canUserLeaveCommentUseCase: CanUserLeaveCommentUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase,
    private val updateRatingUseCase: UpdateRatingUseCase
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

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    private val _canUserLeaveComment = MutableLiveData<Boolean>()
    val canUserLeaveComment: LiveData<Boolean>
        get() = _canUserLeaveComment

    private val _isReviewSubmitted = MutableLiveData<Boolean>()
    val isReviewSubmitted: LiveData<Boolean>
        get() = _isReviewSubmitted

    fun checkStockAvailability(shopItem: ShopItem) {
        _isAddToCartEnabled.postValue(shopItem.quantityInStock > 0)
    }

    fun loadShopItemById(shopItemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true) // Включаємо прогрес-бар
            try {
                // Завантажуємо всі необхідні дані паралельно
                val itemDeferred = async { getShopItemDetailsByIdUseCase(shopItemId) }
                val isFavoriteDeferred = async { isItemInFavoriteUseCase(shopItemId) }
                val isCartDeferred = async { checkIfItemInCartUseCase(shopItemId) }
                val commentsDeferred = async { getShopItemCommentsUseCase(shopItemId) }
                val canLeaveCommentDeferred = async { canUserLeaveCommentUseCase(shopItemId) }

                // Очікуємо завершення всіх завдань
                val item = itemDeferred.await()
                val isFavorite = isFavoriteDeferred.await()
                val isCart = isCartDeferred.await()
                val comments = commentsDeferred.await()
                val canLeaveComment = canLeaveCommentDeferred.await()

                // Публікуємо завантажені дані
                _shopItem.postValue(item)
                _isInFavorite.postValue(isFavorite)
                _isInCart.postValue(isCart)
                _comments.postValue(comments)
                _canUserLeaveComment.postValue(canLeaveComment)

                checkStockAvailability(item)
            } catch (e: Exception) {
                _errorMessage.postValue("Error loading item details: ${e.message}")
            } finally {
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

    fun loadComments(shopItemId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val comments = getShopItemCommentsUseCase(shopItemId)
                _comments.postValue(comments)
            }catch (e: Exception){
                _errorMessage.postValue("Error getting comments from item: ${e.message}")
            }
        }
    }

    fun checkIfUserCanLeaveComment(shopItemId: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = canUserLeaveCommentUseCase(shopItemId)
                _canUserLeaveComment.postValue(result)
            }catch (e: Exception){
                _errorMessage.postValue("Error checking users ability leaving a comment: ${e.message}")
            }
        }
    }

    fun submitReview(shopItemId: String, rating: Float, text: String){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                submitReviewUseCase(shopItemId, rating, text)
                updateRatingUseCase(shopItemId)
                loadShopItemById(shopItemId)
                _isReviewSubmitted.postValue(true)
            }catch (e: Exception){
                _errorMessage.postValue("Error submitting a comment for product: ${e.message}")
            }
            Log.d("ShopItemDetails", "Review submitted for $shopItemId: $rating stars, Text: $text")
        }

    }


}