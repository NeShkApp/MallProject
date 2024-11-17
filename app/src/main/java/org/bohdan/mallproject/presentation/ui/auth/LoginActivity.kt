package org.bohdan.mallproject.presentation.ui.auth

import AuthRepositoryImpl
import AuthViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ActivityLoginBinding
import org.bohdan.mallproject.presentation.ui.home.MainActivity
import org.bohdan.mallproject.presentation.viewmodel.auth.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val GOOGLE_SIGN_IN_REQUEST_CODE = 100
    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: AuthViewModel by lazy {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        val authRepository = AuthRepositoryImpl(firebaseAuth, firestore)
        val factory = AuthViewModelFactory(authRepository)

        ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }


    private var email: String = ""
    private var password: String = ""

    private lateinit var auth: FirebaseAuth
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val currentUser = FirebaseAuth.getInstance().currentUser
//
//        if (currentUser != null) {
//            viewModel.navigateToMainActivity()
//            return
//        }

        val googleSignInOptions = GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchText.setOnClickListener {
            toggleLoginRegisterMode()
        }

        viewModel.navigateToMainActivity.observe(this) {
            if (it == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.user.observe(this) { firebaseUser ->
            firebaseUser?.let {
                if (it.isEmailVerified) {
                    Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please verify your email address!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }


        binding.registerButton.setOnClickListener {
            email = binding.emailInput.text.toString().trim()
            password = binding.passwordInput.text.toString().trim()
            registerUser(email, password)
        }

        binding.loginButton.setOnClickListener {
            email = binding.emailInput.text.toString().trim()
            password = binding.passwordInput.text.toString().trim()
            loginUser(email, password)
        }

    }

//    private fun navigateToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

    private fun registerUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.register(email, password)
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show()
        }
    }

    private fun loginUser(email: String, password: String){
        if(email.isNotEmpty() && password.isNotEmpty()){
            viewModel.login(email, password)
        }else{
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    viewModel.signInWithGoogle(it)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}