package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.presentation.adapter.HomeShopItemsAdapter
import org.bohdan.mallproject.presentation.viewmodel.AllProductsViewModel

class AllProductsFragment : Fragment(), SortShopItemsFragment.SortOptionListener {
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
        setupSortAndFilterButtons(view)

        viewModel.loadItemsWithCurrentSortOrder()
        viewModel.shopItems.observe(viewLifecycleOwner) {
            it?.let {
                adapter.shopList = it
            }
        }

        adapter.onShopItemClickListener = {
            launchShopItemDetailsFragment(it)
        }

    }

//    override fun onPause() {
//        super.onPause()
//        Log.d("AllProductsFragment", "onPause: ")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d("AllProductsFragment", "onStop: ")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("AllProductsFragment", "onDestroy: ")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.d("AllProductsFragment", "onDestroyView: ")
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        Log.d("AllProductsFragment", "onDetach: ")
//    }

    private fun setupSortAndFilterButtons(view: View) {
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
            AllProductsFragmentDirections.actionAllProductsToShopItemDetailsFragment(shopItem)
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