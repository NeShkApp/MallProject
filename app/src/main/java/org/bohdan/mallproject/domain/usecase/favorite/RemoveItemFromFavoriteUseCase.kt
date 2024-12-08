package org.bohdan.mallproject.domain.usecase.favorite

import org.bohdan.mallproject.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveItemFromFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(shopItemId: String){
        return favoriteRepository.removeItemFromFavorite(shopItemId)
    }
}