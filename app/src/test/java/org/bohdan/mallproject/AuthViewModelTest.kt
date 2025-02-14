package org.bohdan.mallproject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.invoke
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.bohdan.mallproject.domain.usecase.auth.CheckIfUserIsValidUseCase
import org.bohdan.mallproject.domain.usecase.auth.CreateUserInFirestoreUseCase
import org.bohdan.mallproject.domain.usecase.auth.GoogleSignInUseCase
import org.bohdan.mallproject.domain.usecase.auth.LoginUseCase
import org.bohdan.mallproject.domain.usecase.auth.MonitorEmailVerificationUseCase
import org.bohdan.mallproject.domain.usecase.auth.RegisterUseCase
import org.bohdan.mallproject.presentation.viewmodel.auth.AuthViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    // Używamy reguły do testowania LiveData
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var authViewModel: AuthViewModel

    @Mock
    private lateinit var mockLoginUseCase: LoginUseCase

    @Mock
    private lateinit var mockRegisterUseCase: RegisterUseCase

    @Mock
    private lateinit var mockGoogleSignInUseCase: GoogleSignInUseCase

    @Mock
    private lateinit var mockCreateUserInFirestoreUseCase: CreateUserInFirestoreUseCase

    @Mock
    private lateinit var mockMonitorEmailVerificationUseCase: MonitorEmailVerificationUseCase

    @Mock
    private lateinit var mockCheckIfUserIsValidUseCase: CheckIfUserIsValidUseCase

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Tworzymy ViewModel z mockiem LoginUseCase
        authViewModel = AuthViewModel(
            mockLoginUseCase,
            mockRegisterUseCase,
            mockGoogleSignInUseCase,
            mockCreateUserInFirestoreUseCase,
            mockMonitorEmailVerificationUseCase,
            mockCheckIfUserIsValidUseCase
        )
    }

    @After
    fun tearDown() {
        // Po każdym teście resetujemy dispatcher
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login should show success message when loginUseCase returns success`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password123"

        // Tworzymy mock użytkownika do udanego logowania
        val mockFirebaseUser = mock(FirebaseUser::class.java)

        // Konfigurujemy mock dla loginUseCase, aby zwrócił sukces
        `when`(mockLoginUseCase.invoke(email, password)).thenReturn(Result.success(mockFirebaseUser))

        // Tworzymy obserwatora dla LiveData
        val messageObserver = mock(Observer::class.java) as Observer<Int>
        authViewModel.messageId.observeForever(messageObserver)

        val navigateObserver = mock(Observer::class.java) as Observer<Boolean>
        authViewModel.navigateToMainActivity.observeForever(navigateObserver)

        // Wywołujemy metodę login() na ViewModel
        authViewModel.login(email, password)

        // Sprawdzamy, czy wszystkie korutyny zakończone
        advanceUntilIdle()

        // Sprawdzamy, czy LiveData otrzymuje odpowiednią wiadomość o sukcesie
        verify(messageObserver).onChanged(R.string.sign_in_successful)

        // Sprawdzamy, czy nastąpiła nawigacja do głównej aktywności
        verify(navigateObserver).onChanged(true)

        // Usuwamy obserwatorów po zakończeniu testu
        authViewModel.messageId.removeObserver(messageObserver)
        authViewModel.navigateToMainActivity.removeObserver(navigateObserver)
    }

//    @Test
//    fun `login should set general error message when an exception is thrown`() = runBlockingTest {
//        val email = "test@example.com"
//        val password = "password123"
//
//        // Konfigurujemy mock dla loginUseCase, aby rzucił błąd (RuntimeException)
//        `when`(mockLoginUseCase.invoke(email, password)).thenThrow(RuntimeException("General error"))
//
//        // Tworzymy obserwatora dla LiveData
//        val messageObserver = mock(Observer::class.java) as Observer<Int>
//        authViewModel.messageId.observeForever(messageObserver)
//
//        val navigateObserver = mock(Observer::class.java) as Observer<Boolean>
//        authViewModel.navigateToMainActivity.observeForever(navigateObserver)
//
//        // Wywołujemy metodę login() na ViewModel
//        authViewModel.login(email, password)
//
//        // Czekamy na zakończenie wszystkich korutyn
//        advanceUntilIdle()
//
//        // Sprawdzamy, czy wyświetlił się ogólny komunikat o błędzie
//        verify(messageObserver).onChanged(R.string.login_error_general)
//
//        // Sprawdzamy, czy użytkownik nie zostaje przekierowany
//        verify(navigateObserver, never()).onChanged(true)
//
//        // Usuwamy obserwatorów po zakończeniu testu
//        authViewModel.messageId.removeObserver(messageObserver)
//        authViewModel.navigateToMainActivity.removeObserver(navigateObserver)
//    }

}

