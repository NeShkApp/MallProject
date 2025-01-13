package org.bohdan.mallproject.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.presentation.ui.base.BaseActivity
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ActivityAuthBinding
import org.bohdan.mallproject.presentation.ui.main.MainActivity
import org.bohdan.mallproject.presentation.viewmodel.auth.AuthViewModel

@AndroidEntryPoint
class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    private val GOOGLE_SIGN_IN_REQUEST_CODE = 100
    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: AuthViewModel by viewModels()

    private var username: String = ""
    private var email: String = ""
    private var password: String = ""

    private var isLoginMode = true

    override fun onStart() {
        super.onStart()
        if (viewModel.checkIfUserLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeGoogleKey()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        binding.switchText.setOnClickListener {
            toggleLoginRegisterMode()
        }

        binding.registerButton.setOnClickListener {
            username = binding.usernameInput.text.toString().trim()
            email = binding.emailInput.text.toString().trim()
            password = binding.passwordInput.text.toString().trim()
            registerUser(username, email, password)
        }

        binding.loginButton.setOnClickListener {
            email = binding.emailInput.text.toString().trim()
            password = binding.passwordInput.text.toString().trim()
            loginUser(email, password)
        }
    }

    private fun setupObservers() {
        viewModel.navigateToMainActivity.observe(this) {
            if (it == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewModel.messageId.observe(this) {
            it?.let {
                val message = getString(it)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.user.observe(this) { firebaseUser ->
            firebaseUser?.let {
                if (it.isEmailVerified) {
                    Toast.makeText(this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.email_verification_reminder),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun initializeGoogleKey() {
        val googleSignInOptions = GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun registerUser(username: String, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            viewModel.register(username, email, password)
        } else {
            Toast.makeText(
                this,
                getString(R.string.login_error_empty_fields),
                Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(email, password)
        } else {
            Toast.makeText(this, getString(R.string.login_error_empty_fields), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun toggleLoginRegisterMode() {
        isLoginMode = !isLoginMode
        if (isLoginMode) {
            binding.usernameInput.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
            binding.registerButton.visibility = View.GONE
            binding.switchText.text = getString(R.string.switch_to_register)
        } else {
            binding.usernameInput.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
            binding.registerButton.visibility = View.VISIBLE
            binding.switchText.text = getString(R.string.switch_to_login)
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
                Toast.makeText(
                    this,
                    getString(R.string.google_sign_in_error, e.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}
