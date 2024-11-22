import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user ?: throw Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("User not found")

            firebaseUser.sendEmailVerification().await()

            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: throw Exception("User not found")

            val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()

            if (!userDoc.exists()) {
                val createUserResult = createUserInFirestore(firebaseUser.uid, firebaseUser.email ?: "", firebaseUser.displayName)
                if (createUserResult.isFailure) {
                    return Result.failure(createUserResult.exceptionOrNull() ?: Exception("Failed to create user in Firestore"))
                }
            }

            Result.success(firebaseUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun createUserInFirestore(
        userId: String,
        email: String,
        name: String?
    ): Result<Unit> {
        return try {
            val user = hashMapOf(
                "email" to email,
                "name" to (name ?: "Anonymous"),
                "userId" to userId
            )

            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()

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

}