package org.bohdan.mallproject.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class MonitorEmailVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<FirebaseUser>{
        return authRepository.monitorEmailVerification()
    }
}