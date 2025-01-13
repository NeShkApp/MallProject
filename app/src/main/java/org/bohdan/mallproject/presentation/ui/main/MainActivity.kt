package org.bohdan.mallproject.presentation.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.presentation.ui.base.BaseActivity
import org.bohdan.mallproject.data.sharedpreferences.FCMTokenPreferences


@AndroidEntryPoint
class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val db = FirebaseFirestore.getInstance()
//
//        val batch: WriteBatch = db.batch()
//
//        val products = listOf(
//            // Cleaning
//            mapOf("name" to "Floor Cleaner", "description" to "Lemon-scented floor cleaner", "price" to 3.5, "quantityInStock" to 40, "category" to "cleaning"),
//            mapOf("name" to "Glass Cleaner", "description" to "Streak-free glass cleaner", "price" to 2.8, "quantityInStock" to 50, "category" to "cleaning"),
//
//            // Decor
//            mapOf("name" to "Scented Candle", "description" to "Lavender-scented candle", "price" to 8.0, "quantityInStock" to 20, "category" to "decor"),
//            mapOf("name" to "Vase", "description" to "Elegant ceramic vase", "price" to 15.0, "quantityInStock" to 15, "category" to "decor"),
//
//            // Hygiene
//            mapOf("name" to "Shampoo", "description" to "Herbal essence shampoo", "price" to 6.5, "quantityInStock" to 60, "category" to "hygiene"),
//            mapOf("name" to "Toothpaste", "description" to "Mint-flavored toothpaste", "price" to 3.0, "quantityInStock" to 80, "category" to "hygiene"),
//
//            // Tools
//            mapOf("name" to "Hammer", "description" to "Durable steel hammer", "price" to 10.0, "quantityInStock" to 15, "category" to "tools"),
//            mapOf("name" to "Screwdriver Set", "description" to "Set of 5 screwdrivers", "price" to 12.0, "quantityInStock" to 10, "category" to "tools")
//        )
//
//        for (product in products) {
//            val documentRef = db.collection("products").document()
//            batch.set(documentRef, product)
//        }
//
//
//        batch.commit()
//            .addOnSuccessListener {
//                Toast.makeText(this, "Products added successfully!", Toast.LENGTH_SHORT).show()
//            }

        setupBottomNavController()
        requestPermission()
    }

    private fun setupBottomNavController() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
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


