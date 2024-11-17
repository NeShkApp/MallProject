package org.bohdan.mallproject.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import org.bohdan.mallproject.R
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.HomeRepository
import org.bohdan.mallproject.presentation.adapter.HomeShopItemsAdapter
import org.bohdan.mallproject.presentation.ui.auth.LoginActivity
import org.bohdan.mallproject.presentation.viewmodel.AllProductsViewModel

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavController()

    }

    private fun setupBottomNavController() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
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
