package org.bohdan.mallproject.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(username: String, email: String, password: String
    ): Result<FirebaseUser> {
        return authRepository.registerWithEmail(username, email, password)
    }
}
