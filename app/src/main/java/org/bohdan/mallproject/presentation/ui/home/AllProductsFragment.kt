package org.bohdan.mallproject.presentation.ui.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.databinding.FragmentAllProductsBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.presentation.adapters.HomeShopItemsAdapter
import org.bohdan.mallproject.presentation.viewmodel.home.AllProductsViewModel

@AndroidEntryPoint
class AllProductsFragment : Fragment(), SortShopItemsFragment.SortOptionListener,
    FilterShopItemsFragment.FilterOptionListener {

    private lateinit var adapter: HomeShopItemsAdapter
    private val viewModel: AllProductsViewModel by viewModels()
    private val args by navArgs<AllProductsFragmentArgs>()

    private var _binding: FragmentAllProductsBinding? = null
    private val binding: FragmentAllProductsBinding
        get() = _binding ?: throw RuntimeException("FragmentAllProductsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupSortButton()
        setupFilterButton()
        setupClickListeners()
        observeData()

    }

    private fun setupClickListeners() {
        binding.btnResetFilters.setOnClickListener {
            binding.etSearch.text?.clear()
            viewModel.updateCurrentFilters(null, null, null)
            viewModel.loadShopItemsByFilters(null, null, null)
            viewModel.setOnlyDiscounts(false)
            viewModel.setMinRating(0)
        }
    }

    private fun observeData() {
        viewModel.shopItems.observe(viewLifecycleOwner) { products ->
            adapter.submitList(products)
            with(binding) {
                if (products.isNullOrEmpty()) {
                    rvShopList.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE
                } else {
                    rvShopList.visibility = View.VISIBLE
                    emptyStateLayout.visibility = View.GONE
                }
            }
        }

        viewModel.currentCategory.observe(viewLifecycleOwner) { category ->
            updateFiltersDisplay()
        }

        viewModel.currentSubcategory.observe(viewLifecycleOwner) { subcategory ->
            updateFiltersDisplay()
        }

        viewModel.currentSearchQuery.observe(viewLifecycleOwner) { searchQuery ->
            updateFiltersDisplay()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Log.e("AllProductsFragment", "Error: $it")
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

//        viewModel.currentOnlyDiscounts.observe(viewLifecycleOwner){
//            if(it){
//                viewModel.filterByDiscount()
//            }
//        }

    }

    private fun updateFiltersDisplay() {
        val category = viewModel.currentCategory.value?.name ?: "All categories"
        val subcategory = viewModel.currentSubcategory.value?.name ?: "All subcategories"
        val searchQuery = viewModel.currentSearchQuery.value

        val filtersText = if (searchQuery.isNullOrEmpty()) {
            "$category / $subcategory"
        } else {
            "$category / $subcategory / \"$searchQuery\""
        }

        with(binding.tvSelectedFilters) {
            text = filtersText
            visibility = View.VISIBLE
        }
    }

    private fun setupSortButton() {
        binding.btnSort.setOnClickListener {
            val currentSort = viewModel.currentSortOrder.value
            SortShopItemsFragment(this, currentSort).show(
                parentFragmentManager,
                "SortBottomSheetFragment"
            )
        }
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            val currentCategory = viewModel.currentCategory.value
            val currentSubcategory = viewModel.currentSubcategory.value
            val currentSearchQuery = viewModel.currentSearchQuery.value
            val currentOnlyDiscounts = viewModel.currentOnlyDiscounts.value ?: false
            val currentMinRating = viewModel.currentMinRating.value ?: 0

            val filterFragment = FilterShopItemsFragment(
                this,
                currentCategory,
                currentSubcategory,
                currentSearchQuery,
                currentOnlyDiscounts,
                currentMinRating
            )
            filterFragment.show(parentFragmentManager, "FilterBottomSheetFragment")
        }
    }

    override fun onFilterApplied(
        category: Category?,
        subcategory: Subcategory?,
        searchQuery: String?,
        onlyDiscounts: Boolean,
        minRating: Int
    ) {
        Log.d("AllProductsFragment", "onFilterApplied called with category=$category, subcategory=$subcategory, searchQuery=$searchQuery, onlyDiscounts=$onlyDiscounts")

        viewModel.updateCurrentFilters(category, subcategory, searchQuery)
        viewModel.loadShopItemsByFilters(category, subcategory, searchQuery)

        viewModel.setOnlyDiscounts(onlyDiscounts)
        viewModel.setMinRating(minRating)

    }

    private fun launchShopItemDetailsFragment(shopItem: ShopItem) {
        findNavController().navigate(
            AllProductsFragmentDirections.actionAllProductsToShopItemDetailsFragment(shopItem.id)
        )
    }

    private fun setupSearch() {

        args.searchQuery?.let { query ->
            binding.etSearch.setText(query)
        }

        binding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim()

                viewModel.updateSearchQuery(query)
                updateFiltersDisplay()

                if (query.isNullOrEmpty()) {
                    binding.etSearch.imeOptions = EditorInfo.IME_ACTION_NONE
                    viewModel.loadShopItemsByFilters(
                        viewModel.currentCategory.value,
                        viewModel.currentSubcategory.value,
                        null
                    )
                } else {
                    binding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
                    viewModel.loadShopItemsByFilters(
                        viewModel.currentCategory.value,
                        viewModel.currentSubcategory.value,
                        viewModel.currentSearchQuery.value,
                    )

                }

//                val query = s?.toString()?.trim()
//                viewModel.onSearchQueryChanged(query)
                updateFiltersDisplay()

            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString().trim()
                Log.d("AllProductsFragment", "Search triggered with query: $query")

                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

                if (query.isNotEmpty()) {
                    viewModel.loadShopItemsByFilters(
                        viewModel.currentCategory.value,
                        viewModel.currentSubcategory.value,
                        viewModel.currentSearchQuery.value
                    )
                } else {
                    viewModel.loadShopItemsByFilters(
                        null,
                        null,
                        null
                    )
                    Log.d("AllProductsFragment", "Empty search query, no action taken")
                }

//                viewModel.onSearchQueryChanged(query)
                return@setOnEditorActionListener true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeShopItemsAdapter()
        adapter.onShopItemClickListener = { shopItem ->
            launchShopItemDetailsFragment(shopItem)
        }
        adapter.onFavoriteClickListener = { shopItem ->
            Toast.makeText(requireContext(), "onFavoriteClickListener clicked: ${shopItem.name}", Toast.LENGTH_LONG).show()
        }
        binding.rvShopList.layoutManager = GridLayoutManager(context,2)
        binding.rvShopList.adapter = adapter
    }

    override fun onSortOptionSelected(sortOption: SortBy) {
        viewModel.updateSortOrder(sortOption)
    }


}