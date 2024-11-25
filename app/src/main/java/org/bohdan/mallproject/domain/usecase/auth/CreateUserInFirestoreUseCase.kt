package org.bohdan.mallproject.domain.usecase.auth

import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserInFirestoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userId: String, email: String): Result<Unit> {
        return authRepository.createUserInFirestore(userId, email)
    }
}