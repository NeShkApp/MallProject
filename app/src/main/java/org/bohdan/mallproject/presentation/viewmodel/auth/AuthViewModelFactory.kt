import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bohdan.mallproject.domain.repository.AuthRepository
import org.bohdan.mallproject.domain.usecase.auth.CreateUserInFirestoreUseCase
import org.bohdan.mallproject.domain.usecase.auth.GoogleSignInUseCase
import org.bohdan.mallproject.domain.usecase.auth.LoginUseCase
import org.bohdan.mallproject.domain.usecase.auth.MonitorEmailVerificationUseCase
import org.bohdan.mallproject.domain.usecase.auth.RegisterUseCase
import org.bohdan.mallproject.presentation.viewmodel.auth.AuthViewModel

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            LoginUseCase(authRepository),
            RegisterUseCase(authRepository),
            GoogleSignInUseCase(authRepository),
            CreateUserInFirestoreUseCase(authRepository),
            MonitorEmailVerificationUseCase(authRepository)
        ) as T
    }
}
