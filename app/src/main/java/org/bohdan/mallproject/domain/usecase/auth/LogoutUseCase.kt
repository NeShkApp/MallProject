package org.bohdan.mallproject.domain.usecase.auth

import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(){
        authRepository.logout()
    }
}