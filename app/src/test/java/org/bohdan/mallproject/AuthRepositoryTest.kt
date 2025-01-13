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
        // Ініціалізація моків
        MockitoAnnotations.openMocks(this)

        // Створення екземпляра AuthRepository вручну з мока
        authRepository = AuthRepositoryImpl(mockAuth, mockFirestore)
    }

    @Test
    fun `loginWithEmail should return FirebaseUser on success`() = runBlocking {
        // Налаштування мока для Task<AuthResult>
        val email = "test@example.com"
        val password = "password123"

        // Створюємо мок для AuthResult
        `when`(mockAuthResult.user).thenReturn(mockFirebaseUser)

        // Створюємо мок для Task
        val task = Tasks.forResult(mockAuthResult)
        `when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(task)

        // Виклик методу
        val result = authRepository.loginWithEmail(email, password)

        // Перевірка, чи повертається успішний результат
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertTrue(result.getOrNull() is FirebaseUser)
    }

    @Test
    fun `loginWithEmail should return failure when an error occurs`() = runBlocking {
        // Налаштування мока для Task<AuthResult>, щоб виникла помилка
        val email = "test@example.com"
        val password = "wrongpassword"

        // Мок для Task, який кидає виключення
        val exception = Exception("Login failed")
        val task = Tasks.forException<AuthResult>(exception)
        `when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(task)

        // Виклик методу
        val result = authRepository.loginWithEmail(email, password)

        // Перевірка, чи повертається помилка
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
        assertTrue(result.exceptionOrNull()?.message == "Login failed")
    }
}