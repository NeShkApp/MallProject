package org.bohdan.mallproject.domain.usecase.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(account: GoogleSignInAccount): Result<FirebaseUser> {
        return authRepository.signInWithGoogle(account)
    }
}
