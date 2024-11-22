//package org.bohdan.mallproject.domain.usecase.home
//
//import org.bohdan.mallproject.domain.model.ShopItem
//import org.bohdan.mallproject.domain.repository.HomeRepository
//
//class GetShopItemByIdUseCase(
//    private val homeRepository: HomeRepository
//) {
//
//    suspend operator fun invoke(shopItemId: String): ShopItem{
//        return homeRepository.getShopItemById(shopItemId)
//    }
//}