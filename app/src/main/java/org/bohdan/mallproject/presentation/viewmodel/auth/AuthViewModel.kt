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
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.usecase.auth.CheckIfUserIsValidUseCase
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
    private val monitorEmailVerificationUseCase: MonitorEmailVerificationUseCase,
    private val checkIfUserIsValidUseCase: CheckIfUserIsValidUseCase
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    private val _messageId = MutableLiveData<Int>()
    val messageId: LiveData<Int> get() = _messageId

    private val _navigateToMainActivity = MutableLiveData<Boolean>(false)
    val navigateToMainActivity: LiveData<Boolean> get() = _navigateToMainActivity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

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
                        is FirebaseAuthInvalidCredentialsException -> R.string.login_error_invalid_credentials
                        is FirebaseAuthInvalidUserException -> R.string.login_error_no_user
                        else -> R.string.login_error_general
                    }
                    _messageId.postValue(errorMessage)
                }
            } catch (exception: Exception) {
                _messageId.postValue(R.string.login_error_general)
            }
        }
    }

    init {
//        checkIfUserLoggedIn()
    }

//    private fun checkIfUserLoggedIn() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
//            _navigateToMainActivity.postValue(true)
//        }
//    }

//    private fun checkIfUserLoggedIn() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _isLoading.postValue(true)
//            try{
//                val currentUser = FirebaseAuth.getInstance().currentUser
//                if (currentUser != null) {
//                    _navigateToMainActivity.postValue(true)
//                }
//            }catch (e: Exception){
//                _messageId.postValue(R.string.google_sign_in_error)
//            }finally {
//                _isLoading.postValue(false)
//            }
//
//        }
//    }

    fun checkIfUserLoggedIn(): Boolean {
        return checkIfUserIsValidUseCase()
    }


    fun register(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = registerUseCase(username, email, password)
            if (result.isSuccess) {
                val firebaseUser = result.getOrNull()
                _user.postValue(firebaseUser)

                firebaseUser?.let { user ->
                    createUserInFirestore(user.displayName.toString(), user.uid, user.email ?: "")
                    _messageId.postValue(R.string.verification_email_sent)
                    monitorEmailVerification()
                }
            } else {
                _messageId.postValue(R.string.register_error_general)
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = googleSignInUseCase(account)
            if (result.isSuccess) {
                _messageId.postValue(R.string.google_sign_in_success)
                _navigateToMainActivity.postValue(true)
            } else {
                _messageId.postValue(R.string.google_sign_in_error)
            }
        }
    }


    private fun createUserInFirestore(username: String, userId: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = createUserInFirestoreUseCase(username, userId, email)
            if (result.isFailure) {
//                _message.value = result.exceptionOrNull()?.message
                _messageId.value = R.string.create_user_error
            }
        }
    }

    private fun monitorEmailVerification() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = monitorEmailVerificationUseCase()
            result.onSuccess { verifiedUser ->
                _user.postValue(verifiedUser)
                _navigateToMainActivity.postValue(true)
            }.onFailure { error ->
//                _message.postValue(error.message)
                _messageId.postValue(R.string.verify_email_error)
            }
        }
    }


}