package org.bohdan.mallproject

import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.bohdan.mallproject.domain.repository.AuthRepository
import org.bohdan.mallproject.domain.usecase.auth.LoginUseCase
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase

    @Mock
    private lateinit var mockAuthRepository: AuthRepository

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(mockAuthRepository)
    }

    @Test
    fun `login should return success when authRepository returns user`() = runBlocking {
        val email = "test@example.com"
        val password = "password123"

        // Налаштовуємо мок для репозиторію, щоб він повертав успішний результат
        `when`(mockAuthRepository.loginWithEmail(email, password)).thenReturn(Result.success(mockFirebaseUser))

        // Викликаємо метод LoginUseCase
        val result = loginUseCase(email, password)

        // Перевірка, чи повертається успішний результат
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() is FirebaseUser)
    }

    @Test
    fun `login should return failure when authRepository returns error`() = runBlocking {
        val email = "test@example.com"
        val password = "wrongpassword"

        // Налаштовуємо мок для репозиторію, щоб він повертав помилку
        val exception = Exception("Login failed")
        `when`(mockAuthRepository.loginWithEmail(email, password)).thenReturn(Result.failure(exception))

        // Викликаємо метод LoginUseCase
        val result = loginUseCase(email, password)

        // Перевірка, чи повертається помилка
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
        assertTrue(result.exceptionOrNull()?.message == "Login failed")
    }
}