package org.bohdan.mallproject.domain.usecase.favorite

import org.bohdan.mallproject.domain.repository.FavoriteRepository
import javax.inject.Inject

class IsItemInFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(shopItemId: String): Boolean{
        return favoriteRepository.isItemInFavorite(shopItemId)
    }
}