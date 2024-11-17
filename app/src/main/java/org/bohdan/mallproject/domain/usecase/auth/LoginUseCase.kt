package org.bohdan.mallproject.domain.usecase.auth

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import org.bohdan.mallproject.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser> {
        return authRepository.loginWithEmail(email, password)
    }
}

