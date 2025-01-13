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

    // Використовуємо правило для тестування LiveData
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

        // Створюємо ViewModel з моком LoginUseCase
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
        // Після кожного тесту скидаємо диспетчер
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login should show success message when loginUseCase returns success`() = runBlockingTest {
        val email = "test@example.com"
        val password = "password123"

        // Створюємо мок користувача для успішного входу
        val mockFirebaseUser = mock(FirebaseUser::class.java)

        // Налаштовуємо мок для loginUseCase, щоб він повертав успішний результат
        `when`(mockLoginUseCase.invoke(email, password)).thenReturn(Result.success(mockFirebaseUser))

        // Створюємо обсервер для LiveData
        val messageObserver = mock(Observer::class.java) as Observer<Int>
        authViewModel.messageId.observeForever(messageObserver)

        val navigateObserver = mock(Observer::class.java) as Observer<Boolean>
        authViewModel.navigateToMainActivity.observeForever(navigateObserver)

        // Викликаємо метод login() на ViewModel
        authViewModel.login(email, password)

        // Перевіряємо, чи завершені всі корутини
        advanceUntilIdle()

        // Перевіряємо, що LiveData отримує правильне повідомлення про успіх
        verify(messageObserver).onChanged(R.string.sign_in_successful)

        // Перевіряємо, чи відбулося перенаправлення до головної активності
        verify(navigateObserver).onChanged(true)

        // Видаляємо обсервери після завершення тесту
        authViewModel.messageId.removeObserver(messageObserver)
        authViewModel.navigateToMainActivity.removeObserver(navigateObserver)
    }

//    @Test
//    fun `login should set general error message when an exception is thrown`() = runBlockingTest {
//        val email = "test@example.com"
//        val password = "password123"
//
//        // Налаштовуємо мок для loginUseCase, щоб він кидав неперевірену помилку (RuntimeException)
//        `when`(mockLoginUseCase.invoke(email, password)).thenThrow(RuntimeException("General error"))
//
//        // Створюємо обсервер для LiveData
//        val messageObserver = mock(Observer::class.java) as Observer<Int>
//        authViewModel.messageId.observeForever(messageObserver)
//
//        val navigateObserver = mock(Observer::class.java) as Observer<Boolean>
//        authViewModel.navigateToMainActivity.observeForever(navigateObserver)
//
//        // Викликаємо метод login() на ViewModel
//        authViewModel.login(email, password)
//
//        // Очікуємо завершення всіх корутин
//        advanceUntilIdle()
//
//        // Перевіряємо, що відобразилось загальне повідомлення про помилку
//        verify(messageObserver).onChanged(R.string.login_error_general)
//
//        // Перевіряємо, що користувач не перенаправляється
//        verify(navigateObserver, never()).onChanged(true)
//
//        // Видаляємо обсервери після завершення тесту
//        authViewModel.messageId.removeObserver(messageObserver)
//        authViewModel.navigateToMainActivity.removeObserver(navigateObserver)
//    }


}
