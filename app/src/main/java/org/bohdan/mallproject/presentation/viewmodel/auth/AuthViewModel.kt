//package org.bohdan.mallproject.presentation.viewmodel.auth
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.google.firebase.auth.FirebaseUser
//import org.bohdan.mallproject.domain.repository.AuthRepository
//import org.bohdan.mallproject.domain.usecase.auth.LoginUseCase
//import org.bohdan.mallproject.domain.usecase.auth.LogoutUseCase
//import org.bohdan.mallproject.domain.usecase.auth.RegisterUseCase
//
//class AuthViewModel(
//    private val loginUseCase: LoginUseCase,
//    private val registerUseCase: RegisterUseCase,
//    private val logoutUseCase: LogoutUseCase
//): ViewModel() {
//
//    private val _loginResult = MutableLiveData<Result<FirebaseUser?>>()
//    val loginResult: LiveData<Result<FirebaseUser?>>
//        get() = _loginResult
//
//    private val _registerResult = MutableLiveData<Result<FirebaseUser?>>()
//    val registerResult: LiveData<Result<FirebaseUser?>>
//        get() = _registerResult
//
//    fun login(email: String, password: String) {
//        _loginResult.value = loginUseCase(email, password).value
//    }
//
//    fun register(email: String, password: String) {
//        _registerResult.value = registerUseCase(email, password).value
//    }
//
//    fun logout() {
//        logoutUseCase()
//    }
//}