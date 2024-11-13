//package org.bohdan.mallproject.domain.usecase.auth
//
//import androidx.lifecycle.LiveData
//import com.google.firebase.auth.FirebaseUser
//import org.bohdan.mallproject.domain.repository.AuthRepository
//
//class RegisterUseCase(private val authRepository: AuthRepository) {
//
//    operator fun invoke(email: String, password: String): LiveData<Result<FirebaseUser?>> {
//        return authRepository.register(email, password)
//    }
//}
