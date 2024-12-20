package org.bohdan.mallproject.domain.usecase.auth

import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class CheckIfUserIsValidUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean{
        return authRepository.checkIfUserIsValid()
    }
}