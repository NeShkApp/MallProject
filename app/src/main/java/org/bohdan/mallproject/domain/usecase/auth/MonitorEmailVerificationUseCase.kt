package org.bohdan.mallproject.domain.usecase.auth

import AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.repository.AuthRepository

class MonitorEmailVerificationUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<FirebaseUser>{
        return authRepository.monitorEmailVerification()
    }
}