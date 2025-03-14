package org.bohdan.mallproject.domain.usecase.home

import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.repository.HomeRepository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    suspend operator fun invoke(): List<Category>{
        return homeRepository.getAllCategories()
    }
}