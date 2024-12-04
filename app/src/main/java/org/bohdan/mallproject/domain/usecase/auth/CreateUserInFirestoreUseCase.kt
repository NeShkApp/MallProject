package org.bohdan.mallproject.domain.usecase.auth

import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserInFirestoreUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        userId: String,
        email: String,
    ): Result<Unit> {
        return authRepository.createUserInFirestore(
            username,
            userId,
            email
        )
    }
}