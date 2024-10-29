package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.data.HomeRepositoryImpl
import org.bohdan.mallproject.domain.repository.HomeRepository
import org.bohdan.mallproject.presentation.adapter.HomeShopItemsAdapter
import org.bohdan.mallproject.presentation.viewmodel.AllProductsViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewModel.shopItems.observe(this) {
//            it.forEach {
//                Log.d(
//                    "MainActivity",
//                    "ID: ${it.id}, " +
//                            "Name: ${it.name}, " +
//                            "Description: ${it.description}, " +
//                            "Category: ${it.category}, " +
//                            "Price: ${it.price}" +
//                            "Rating: ${it.rating}."
//                )
//            }
//            adapter.shopList = it
    }


}
