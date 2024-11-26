package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.presentation.adapters.HomeShopItemsAdapter
import org.bohdan.mallproject.presentation.viewmodel.home.AllProductsViewModel

@AndroidEntryPoint
class AllProductsFragment : Fragment(), SortShopItemsFragment.SortOptionListener {
    private lateinit var adapter: HomeShopItemsAdapter

//    private val args by navArgs<AllProductsFragmentArgs>()
    private val viewModel: AllProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_products, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.savedStateHandle["category"] = args.category
//        viewModel.savedStateHandle["subcategory"] = args.subcategory
//        viewModel.savedStateHandle["searchQuery"] = args.searchQuery
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupSortButton(view)

        observeData()
    }

    private fun observeData() {
        viewModel.shopItems.observe(viewLifecycleOwner) { products ->
            if (products.isNotEmpty()) {
                adapter.shopList = products
                adapter.onShopItemClickListener = { shopItem ->
                    launchShopItemDetailsFragment(shopItem)
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSortButton(view: View) {
        val sortButton: Button = view.findViewById(R.id.btn_sort)
        sortButton.setOnClickListener {
            val currentSort = viewModel.currentSortOrder.value
            SortShopItemsFragment(this, currentSort).show(
                parentFragmentManager,
                "SortBottomSheetFragment"
            )
        }
    }

    private fun launchShopItemDetailsFragment(shopItem: ShopItem) {
        findNavController().navigate(
            AllProductsFragmentDirections.actionAllProductsToShopItemDetailsFragment(shopItem.id)
        )
    }

    private fun setupRecyclerView(view: View) {
        val rvShopList = view.findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = HomeShopItemsAdapter()
        rvShopList.adapter = adapter
    }

    override fun onSortOptionSelected(sortOption: SortBy) {
        viewModel.updateSortOrder(sortOption)
    }


}