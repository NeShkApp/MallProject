package org.bohdan.mallproject.domain.usecase.home

import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.repository.HomeRepository
import javax.inject.Inject

class GetSubcategoriesByCategoryUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {

    suspend operator fun invoke(category: Category): List<Subcategory>{
        return homeRepository.getSubcategoriesByCategory(category)
    }
}