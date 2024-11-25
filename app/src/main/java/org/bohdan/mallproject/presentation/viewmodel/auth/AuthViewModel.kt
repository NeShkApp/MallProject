package org.bohdan.mallproject.presentation.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bohdan.mallproject.domain.usecase.auth.CreateUserInFirestoreUseCase
import org.bohdan.mallproject.domain.usecase.auth.GoogleSignInUseCase
import org.bohdan.mallproject.domain.usecase.auth.LoginUseCase
import org.bohdan.mallproject.domain.usecase.auth.MonitorEmailVerificationUseCase
import org.bohdan.mallproject.domain.usecase.auth.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val createUserInFirestoreUseCase: CreateUserInFirestoreUseCase,
    private val monitorEmailVerificationUseCase: MonitorEmailVerificationUseCase
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _navigateToMainActivity = MutableLiveData<Boolean>()
    val navigateToMainActivity: LiveData<Boolean> get() = _navigateToMainActivity

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = loginUseCase(email, password)
                if (result.isSuccess) {
                    _user.postValue(result.getOrNull())
                    _navigateToMainActivity.postValue(true)
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = when (exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Incorrect password or email"
                        is FirebaseAuthInvalidUserException -> "No user found with this email"
                        else -> "Login error"
                    }
                    _message.postValue(errorMessage)
                }
            } catch (exception: Exception) {
                _message.postValue(exception.message ?: "Login error")
            }
        }
    }

    init {
        checkIfUserLoggedIn()
    }

    private fun checkIfUserLoggedIn() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            _navigateToMainActivity.postValue(true)
        }
    }

    fun navigateToMainActivity() {
        _navigateToMainActivity.postValue(true)
    }

    fun register(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = registerUseCase(email, password)
            if (result.isSuccess) {
                val firebaseUser = result.getOrNull()
                _user.postValue(firebaseUser)

                firebaseUser?.let { user ->
                    createUserInFirestore(user.uid, user.email ?: "")
                    _message.postValue("Verification email sent to ${user.email}. Please verify your email.")
                    monitorEmailVerification()
                }
            } else {
                _message.postValue(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = googleSignInUseCase(account)
            if (result.isSuccess) {
                _message.postValue("Google Sign-in was successfully!")
                _navigateToMainActivity.postValue(true)
            } else {
                _message.postValue(result.exceptionOrNull()?.message ?: "Google Sign-In failed")
            }
        }
    }


    private fun createUserInFirestore(userId: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = createUserInFirestoreUseCase(userId, email)
            if (result.isFailure) {
                _message.value = result.exceptionOrNull()?.message
            }
        }
    }

    private fun monitorEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = monitorEmailVerificationUseCase()
            result.onSuccess { verifiedUser ->
                _user.postValue(verifiedUser)
                navigateToMainActivity()
            }.onFailure { error ->
                _message.postValue(error.message)
            }
        }
    }


}

