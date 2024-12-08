package org.bohdan.mallproject.domain.usecase.favorite

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteItemsUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(): List<ShopItem>{
        return favoriteRepository.getFavoriteItems()
    }
}