package org.bohdan.mallproject

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.bohdan.mallproject.data.repositoryimpl.AuthRepositoryImpl
import org.bohdan.mallproject.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    @Mock
    private lateinit var mockAuthResult: AuthResult

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    @Before
    fun setUp() {
        // Inicjalizacja mocków
        MockitoAnnotations.openMocks(this)

        // Ręczne tworzenie instancji AuthRepository z mockami
        authRepository = AuthRepositoryImpl(mockAuth, mockFirestore)
    }

    @Test
    fun `loginWithEmail should return FirebaseUser on success`() = runBlocking {
        // Ustawianie mocka dla Task<AuthResult>
        val email = "test@example.com"
        val password = "password123"

        // Tworzymy mocka dla AuthResult
        `when`(mockAuthResult.user).thenReturn(mockFirebaseUser)

        // Tworzymy mocka dla Task
        val task = Tasks.forResult(mockAuthResult)
        `when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(task)

        // Wywołanie metody
        val result = authRepository.loginWithEmail(email, password)

        // Sprawdzamy, czy zwrócony wynik jest sukcesem
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertTrue(result.getOrNull() is FirebaseUser)
    }

    @Test
    fun `loginWithEmail should return failure when an error occurs`() = runBlocking {
        // Ustawianie mocka dla Task<AuthResult>, aby wystąpił błąd
        val email = "test@example.com"
        val password = "wrongpassword"

        // Mock dla Task, który rzuca wyjątek
        val exception = Exception("Login failed")
        val task = Tasks.forException<AuthResult>(exception)
        `when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(task)

        // Wywołanie metody
        val result = authRepository.loginWithEmail(email, password)

        // Sprawdzamy, czy wynik to błąd
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
        assertTrue(result.exceptionOrNull()?.message == "Login failed")
    }
}
