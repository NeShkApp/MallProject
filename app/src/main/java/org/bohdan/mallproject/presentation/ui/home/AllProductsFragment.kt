package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.presentation.adapter.HomeShopItemsAdapter
import org.bohdan.mallproject.presentation.viewmodel.AllProductsViewModel

class AllProductsFragment : Fragment() {
    private lateinit var adapter: HomeShopItemsAdapter

    private val viewModel: AllProductsViewModel by lazy {
        ViewModelProvider(this)[AllProductsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)

//        viewModel.getShopItems()
        viewModel.getAllShopItems()
        viewModel.shopItems.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("AllProductsFragment", "Received items in Fragment: ${it.size}")
                adapter.shopList = it
            }
        }

    }

    private fun setupRecyclerView(view: View) {
        val rvShopList = view.findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = HomeShopItemsAdapter()
        rvShopList.adapter = adapter
    }

}