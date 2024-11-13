package org.bohdan.mallproject.presentation.viewmodel.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ActivityLoginBinding
import org.bohdan.mallproject.presentation.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 100
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація прив'язки та Firebase Auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val gso = Builder(DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Перевірка, чи користувач уже увійшов у систему
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            navigateToMainActivity()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            loginUser(email, password)
        }

        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()
            registerUser(email, password)
        }

        binding.switchText.setOnClickListener {
            toggleLoginRegisterMode()
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            Log.d(TAG, "signInWithEmail:success")
                            updateLoginStatus(true)
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this, "Будь ласка, підтвердіть свою електронну пошту", Toast.LENGTH_SHORT).show()
                            auth.signOut()  // Вихід, якщо електронну пошту не підтверджено
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Перевірте електронну пошту для підтвердження. Після підтвердження ви будете автоматично перенаправлені.",
                                    Toast.LENGTH_LONG
                                ).show()
                                startEmailVerificationCheck() // Починаємо перевірку статусу підтвердження

                                user.let {
                                    createUserInFirestore(it.uid, it.email ?: "")
                                }
                            } else {
                                Log.w(TAG, "sendEmailVerification:failure", verificationTask.exception)
                                Toast.makeText(
                                    this,
                                    "Не вдалося надіслати лист підтвердження: ${verificationTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
    }

    // Метод для запуску таймера, який перевіряє статус підтвердження
    private fun startEmailVerificationCheck() {
        val handler = Handler(Looper.getMainLooper())
        val checkInterval = 3000L // Інтервал перевірки в мілісекундах (3 секунди)

        handler.post(object : Runnable {
            override fun run() {
                auth.currentUser?.reload()?.addOnSuccessListener {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        updateLoginStatus(true)
                        navigateToMainActivity()
                    } else {

                        handler.postDelayed(this, checkInterval)
                    }
                }
            }
        })
    }

    private fun updateLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun toggleLoginRegisterMode() {
        isLoginMode = !isLoginMode
        if (isLoginMode) {
            binding.loginButton.visibility = View.VISIBLE
            binding.registerButton.visibility = View.GONE
            binding.switchText.text = "Хочете зареєструватись?"
        } else {
            binding.loginButton.visibility = View.GONE
            binding.registerButton.visibility = View.VISIBLE
            binding.switchText.text = "Хочете залогуватись?"
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    updateLoginStatus(true)


                    val user = auth.currentUser
                    if (user != null) {
                        createUserInFirestore(user.uid, user.email ?: "", account.displayName)
                    }
                    navigateToMainActivity()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                firebaseAuthWithGoogle(account)
            }
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun createUserInFirestore(userId: String, email: String, name: String? = null) {
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "email" to email,
            "name" to (name ?: "Anonymous"),
            "userId" to userId
        )

        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "User document added to Firestore!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding user document to Firestore", e)
            }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
