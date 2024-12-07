package org.bohdan.mallproject.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.presentation.ui.base.BaseActivity
import org.bohdan.mallproject.R
import org.bohdan.mallproject.presentation.ui.auth.AuthActivity
import org.bohdan.mallproject.utils.AirplaneModeReceiver

@AndroidEntryPoint
class MainActivity : BaseActivity() {

//    private val airplaneModeReceiver = AirplaneModeReceiver { isAirplaneModeOn ->
//        if (isAirplaneModeOn) {
//            showCheckInternetDialog()
//        }else {
//            dismissDialog()
//        }
//    }
//    private var checkInternetDialog: AlertDialog? = null
//
//    private fun showCheckInternetDialog() {
//        val builder = AlertDialog.Builder(this)
//        val dialogView = layoutInflater.inflate(R.layout.check_internet_dialog, null)
//
//        builder.setView(dialogView)
//        val btnRetry = dialogView.findViewById<Button>(R.id.btn_retry)
//
//        btnRetry.setOnClickListener {
//            Toast.makeText(this, "Retrying...", Toast.LENGTH_SHORT).show()
//        }
//
//        builder.setCancelable(false)
//        checkInternetDialog = builder.show()
//    }
//
//    private fun dismissDialog() {
//        checkInternetDialog?.dismiss()
//        checkInternetDialog = null
//    }
//

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

//        registerReceiver(airplaneModeReceiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        setupBottomNavController()
    }

    private fun setupBottomNavController() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)


        // TODO: FIX THE NAVIGATION BUG
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

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(airplaneModeReceiver)
    }

}



//        val db = FirebaseFirestore.getInstance()
//
//// Створюємо батч для запису
//        val batch: WriteBatch = db.batch()
//
//// Додаємо дані в колекцію categories
//        val categoryRef = db.collection("categories").document()  // ID буде згенеровано автоматично
//        val categoryData = hashMapOf(
//            "name" to "Electronics",
//            "subcategoriesIds" to listOf("subcategory_electronics_1", "subcategory_electronics_2"),  // Список підкатегорій з конкретними ID
//            "id" to ""  // Firestore згенерує ID для категорії
//        )
//        batch.set(categoryRef, categoryData)
//
//// Додаємо дані в колекцію subcategories з конкретними ID
//        val subcategoryRef1 = db.collection("subcategories").document("subcategory_electronics_1")  // Задаємо конкретний ID для підкатегорії
//        val subcategoryData1 = hashMapOf(
//            "name" to "Smartphones",
//            "categoryId" to categoryRef.id,  // Підкатегорія належить до категорії Electronics
//            "productIds" to listOf<String>(),  // Поки що порожній список продуктів
//            "id" to "subcategory_electronics_1"  // Явно вказуємо ID для підкатегорії
//        )
//        batch.set(subcategoryRef1, subcategoryData1)
//
//        val subcategoryRef2 = db.collection("subcategories").document("subcategory_electronics_2")  // Задаємо конкретний ID для підкатегорії
//        val subcategoryData2 = hashMapOf(
//            "name" to "Laptops",
//            "categoryId" to categoryRef.id,  // Підкатегорія належить до категорії Electronics
//            "productIds" to listOf<String>(),  // Поки що порожній список продуктів
//            "id" to "subcategory_electronics_2"  // Явно вказуємо ID для підкатегорії
//        )
//        batch.set(subcategoryRef2, subcategoryData2)
//
//// Додаємо дані в колекцію products (з автоматичними ID)
//        val productRef1 = db.collection("products").document()  // Firestore згенерує ID для продукту
//        val productData1 = hashMapOf(
//            "name" to "iPhone 13",
//            "description" to "Latest Apple iPhone",
//            "price" to 999.99,
//            "rating" to 4.5,
//            "imageUrl" to "https://image.url",
//            "id" to ""  // Firestore згенерує ID для продукту
//        )
//        batch.set(productRef1, productData1)
//
//        val productRef2 = db.collection("products").document()  // Firestore згенерує ID для продукту
//        val productData2 = hashMapOf(
//            "name" to "Samsung Galaxy S21",
//            "description" to "Newest Samsung flagship phone",
//            "price" to 899.99,
//            "rating" to 4.7,
//            "imageUrl" to "https://image.url",
//            "id" to ""  // Firestore згенерує ID для продукту
//        )
//        batch.set(productRef2, productData2)
//
//        val productRef3 = db.collection("products").document()  // Firestore згенерує ID для продукту
//        val productData3 = hashMapOf(
//            "name" to "MacBook Pro 13\"",
//            "description" to "Apple laptop with M1 chip",
//            "price" to 1299.99,
//            "rating" to 4.8,
//            "imageUrl" to "https://image.url",
//            "id" to ""  // Firestore згенерує ID для продукту
//        )
//        batch.set(productRef3, productData3)
//
//        val productRef4 = db.collection("products").document()  // Firestore згенерує ID для продукту
//        val productData4 = hashMapOf(
//            "name" to "Dell XPS 13",
//            "description" to "High-performance laptop",
//            "price" to 999.99,
//            "rating" to 4.6,
//            "imageUrl" to "https://image.url",
//            "id" to ""  // Firestore згенерує ID для продукту
//        )
//        batch.set(productRef4, productData4)
//
//// Виконання батчу
//        batch.commit().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                println("Batch write was successful!")
//            } else {
//                task.exception?.let {
//                    println("Error during batch write: ${it.localizedMessage}")
//                }
//            }
//        }



//        val products = listOf(
//            ShopItem(
//                name = "Smartphone",
//                description = "Latest model with high performance.",
//                price = 699.99,
//                rating = 4.5f,
//                imageUrl = "https://example.com/images/smartphone.jpg"
//            ),
//            ShopItem(
//                name = "Laptop",
//                description = "Powerful laptop for work and gaming.",
//                price = 1200.00,
//                rating = 4.7f,
//                imageUrl = "https://example.com/images/laptop.jpg"
//            ),
//            ShopItem(
//                name = "Washing Machine",
//                description = "Efficient and quiet washing machine.",
//                price = 450.50,
//                rating = 4.3f,
//                imageUrl = "https://example.com/images/washing_machine.jpg"
//            ),
//            ShopItem(
//                name = "Electric Kettle",
//                description = "Quick boiling kettle with auto shut-off feature.",
//                price = 29.99,
//                rating = 4.2f,
//                imageUrl = "https://example.com/images/electric_kettle.jpg"
//            ),
//            ShopItem(
//                name = "Leather Jacket",
//                description = "Stylish leather jacket, perfect for colder weather.",
//                price = 150.00,
//                rating = 4.6f,
//                imageUrl = "https://example.com/images/leather_jacket.jpg"
//            ),
//            ShopItem(
//                name = "Coffee Maker",
//                description = "Brews the perfect cup of coffee every time.",
//                price = 99.99,
//                rating = 4.4f,
//                imageUrl = "https://example.com/images/coffee_maker.jpg"
//            ),
//            ShopItem(
//                name = "Air Conditioner",
//                description = "Energy-efficient AC unit for cooling large rooms.",
//                price = 499.99,
//                rating = 4.8f,
//                imageUrl = "https://example.com/images/air_conditioner.jpg"
//            ),
//            ShopItem(
//                name = "Bluetooth Speaker",
//                description = "Portable speaker with great sound quality.",
//                price = 89.99,
//                rating = 4.2f,
//                imageUrl = "https://example.com/images/bluetooth_speaker.jpg"
//            )
//        )
//
//        addProductsBatch(products)


//    }


//    fun addProductsBatch(products: List<ShopItem>) {
//        val db = FirebaseFirestore.getInstance()
//
//        val batch: WriteBatch = db.batch()  // Створюємо об'єкт WriteBatch
//
//        // Перебираємо продукти та додаємо їх у батч
//        products.forEach { product ->
//            // Вибираємо колекцію, куди будемо додавати продукт
//            val productRef =
//                db.collection("products").document() // Firestore автоматично генерує ID
//            batch.set(productRef, product)  // Додаємо запис у батч
//        }
//
//        // Виконуємо батч-операцію
//        batch.commit()
//            .addOnSuccessListener {
//                Log.d("Firestore", "Batch write succeeded")
//            }
//            .addOnFailureListener { e ->
//                Log.w("Firestore", "Error with batch write", e)
//            }
//
//}

//}
