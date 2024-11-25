package org.bohdan.mallproject.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser>
    suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser>
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser>
    suspend fun createUserInFirestore(userId: String, email: String, name: String? = null): Result<Unit>
    suspend fun monitorEmailVerification(): Result<FirebaseUser>
}
