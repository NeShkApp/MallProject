package org.bohdan.mallproject

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertNotNull
import org.bohdan.mallproject.data.repositoryimpl.AuthRepositoryImpl
import org.bohdan.mallproject.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AuthModuleTest {

    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockUser: FirebaseUser

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        authRepository = AuthRepositoryImpl(mockAuth, mockFirestore)
    }

    @Test
    fun `test AuthRepository is injected correctly`() {
        assertNotNull(authRepository)
    }

    @Test
    fun `test AuthRepository methods with mocked dependencies`() {
        `when`(mockAuth.currentUser).thenReturn(mockUser)

        authRepository.checkIfUserIsValid()

        verify(mockAuth).currentUser
    }
}
