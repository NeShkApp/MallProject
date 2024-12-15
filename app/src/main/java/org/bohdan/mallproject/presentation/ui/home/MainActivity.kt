package org.bohdan.mallproject.presentation.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.presentation.ui.auth.AuthActivity
import org.bohdan.mallproject.presentation.ui.base.BaseActivity
import org.bohdan.mallproject.utils.FCMTokenPreferences

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavController()
        requestPermission()
    }

    private fun setupBottomNavController() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)


//         TODO: FIX THE NAVIGATION BUG
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.categoriesFragment -> {
                    Log.d("MainActivity", "Categories icon clicked")
                    // Перехід до фрагменту Categories
                    findNavController(R.id.home_container).navigate(R.id.categoriesFragment)
                    true
                }
                R.id.favoriteFragment -> {
                    Log.d("MainActivity", "Favorite icon clicked")
                    // Перехід до фрагменту Favorites
                    findNavController(R.id.home_container).navigate(R.id.favoriteFragment)
                    true
                }
                R.id.cartFragment -> {
                    Log.d("MainActivity", "Cart icon clicked")
                    // Перехід до фрагменту Cart
                    findNavController(R.id.home_container).navigate(R.id.cartFragment)
                    true
                }
                R.id.profileFragment -> {
                    Log.d("MainActivity", "Profile icon clicked")
                    // Перехід до фрагменту Profile
                    findNavController(R.id.home_container).navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }

    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fetchFCMToken()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            fetchFCMToken()
        }
    }

    private fun fetchFCMToken() {
        if (!FCMTokenPreferences.hasToken(this)) {
            FCMTokenPreferences.fetchAndSaveToken(this) { token ->
                Log.d("FirebaseLogs", "Fetched new FCM token: $token")
            }
        } else {
            val savedToken = FCMTokenPreferences.getToken(this)
            Log.d("FirebaseLogs", "Using saved FCM token: $savedToken")
        }
    }



    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            fetchFCMToken()
        } else {
            Log.d("FirebaseLogs", "Permission denied for POST_NOTIFICATIONS")
        }
    }


}


