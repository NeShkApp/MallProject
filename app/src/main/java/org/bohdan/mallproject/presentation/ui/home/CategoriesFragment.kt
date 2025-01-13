package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentCategoriesBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.presentation.adapters.CategoriesAdapter
import org.bohdan.mallproject.presentation.viewmodel.home.CategoriesViewModel

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private lateinit var adapter: CategoriesAdapter

    private val viewModel: CategoriesViewModel by viewModels()

    private var _binding: FragmentCategoriesBinding? = null
    private val binding: FragmentCategoriesBinding
        get() = _binding ?: throw RuntimeException("FragmentCategoriesBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.text?.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        adapter.onCategoryClickListener = {
            launchSubcategoriesFragment(it)
        }
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                adapter.submitList(it)
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.etSearch.imeOptions = EditorInfo.IME_ACTION_NONE
                } else {
                    binding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = binding.etSearch.text.toString().trim()

                if (searchQuery.isNotEmpty()) {
                    launchAllProductFragment(searchQuery)
                }
                true
            } else {
                false
            }
        }
    }

    private fun launchAllProductFragment(
        searchQuery: String?
    ) {
        val currentDestination = findNavController().currentDestination?.id
        if (currentDestination == R.id.categoriesFragment) {
            findNavController().navigate(
                CategoriesFragmentDirections.actionCategoriesFragmentToAllProducts(
                    null,
                    null,
                    searchQuery
                )
            )
        }
    }

    private fun launchSubcategoriesFragment(category: Category) {
        findNavController().navigate(
            CategoriesFragmentDirections.actionCategoriesFragmentToSubcategoriesFragment(
                category
            )
        )
    }

    private fun setupRecyclerView() {
        adapter = CategoriesAdapter()
        binding.rvCategories.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}