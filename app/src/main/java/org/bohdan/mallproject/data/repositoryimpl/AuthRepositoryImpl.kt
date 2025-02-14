package org.bohdan.mallproject.data.repositoryimpl

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()
            Result
                .success(result.user
                    ?: throw Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(
        username: String,
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("User not found")
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()
            firebaseUser.sendEmailVerification().await()
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Log.e("RegisterError", "Error during registration: ${e.message}", e)
            val errorMessage = when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    when {
                        email.isEmpty() || !android.
                        util.Patterns.EMAIL_ADDRESS.matcher(email).
                        matches() -> "Invalid email address format"
                        password.length < 6 -> "Password must be at least 6 characters long"
                        else -> "Invalid credentials"
                    }
                }
                is FirebaseAuthUserCollisionException -> "User with this email already exists"
                else -> "An unknown error occurred"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    override suspend fun signInWithGoogle(
        account: GoogleSignInAccount
    ): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("User not found")
            val userDoc = firestore.collection("users").
            document(firebaseUser.uid).get().await()
            if (!userDoc.exists()) {
                val createUserResult = createUserInFirestore(
                    firebaseUser.displayName.toString(),
                    firebaseUser.uid,
                    firebaseUser.email ?: "")
                if (createUserResult.isFailure) {
                    return Result.failure(createUserResult.exceptionOrNull()
                        ?: Exception("Failed to create user in Firestore"))
                }
            }
            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun createUserInFirestore(username: String, userId: String, email: String)
    : Result<Unit> {
        return try {
            val user = hashMapOf("name" to username, "email" to email, "userId" to userId)
            firestore.collection("users").document(userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun monitorEmailVerification(): Result<FirebaseUser> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("No authenticated user found"))
        while (!currentUser.isEmailVerified) {
            try {
                currentUser.reload().await()
                if (currentUser.isEmailVerified) {
                    return Result.success(currentUser)
                }
            } catch (e: Exception) {
                return Result.failure(Exception("Error checking email verification: ${e.message}"))
            }
            delay(3000)
        }
        return Result.failure(Exception("Email verification monitoring stopped unexpectedly"))
    }

    override suspend fun logout() {
        try{
            auth.signOut()
        }catch (e: Exception){
            throw RuntimeException("Logout was failed. Error: ${e.message}")
        }
    }

    override fun checkIfUserIsValid(): Boolean {
        try{
            return auth.currentUser != null
        }catch (e: Exception){
            throw RuntimeException("Checking validity was failed. Error: ${e.message}")
        }
    }


}