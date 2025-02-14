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

        // Konfigurujemy mock dla repozytorium, aby zwróciło wynik sukcesu
        `when`(mockAuthRepository.loginWithEmail(email, password)).thenReturn(Result.success(mockFirebaseUser))

        // Wywołujemy metodę LoginUseCase
        val result = loginUseCase(email, password)

        // Sprawdzamy, czy zwrócono wynik sukcesu
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() is FirebaseUser)
    }

    @Test
    fun `login should return failure when authRepository returns error`() = runBlocking {
        val email = "test@example.com"
        val password = "wrongpassword"

        // Konfigurujemy mock dla repozytorium, aby zwróciło błąd
        val exception = Exception("Login failed")
        `when`(mockAuthRepository.loginWithEmail(email, password)).thenReturn(Result.failure(exception))

        // Wywołujemy metodę LoginUseCase
        val result = loginUseCase(email, password)

        // Sprawdzamy, czy zwrócono błąd
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
        assertTrue(result.exceptionOrNull()?.message == "Login failed")
    }
}
