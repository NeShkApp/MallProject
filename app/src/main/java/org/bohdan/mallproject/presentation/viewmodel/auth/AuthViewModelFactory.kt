//package org.bohdan.mallproject.presentation.viewmodel.auth
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import org.bohdan.mallproject.domain.repository.AuthRepository
//import org.bohdan.mallproject.domain.usecase.auth.LoginUseCase
//import org.bohdan.mallproject.domain.usecase.auth.LogoutUseCase
//import org.bohdan.mallproject.domain.usecase.auth.RegisterUseCase
//
//class AuthViewModelFactory(
//    private val loginUseCase: LoginUseCase,
//    private val registerUseCase: RegisterUseCase,
//    private val logoutUseCase: LogoutUseCase
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return AuthViewModel(loginUseCase, registerUseCase, logoutUseCase) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
