package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentSubcategoriesBinding
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.presentation.adapters.SubcategoriesAdapter
import org.bohdan.mallproject.presentation.viewmodel.home.SubcategoriesViewModel

@AndroidEntryPoint
class SubcategoriesFragment : Fragment() {

    private lateinit var adapter: SubcategoriesAdapter

    private val args by navArgs<SubcategoriesFragmentArgs>()

    private val viewModel: SubcategoriesViewModel by viewModels()

    private var _binding: FragmentSubcategoriesBinding? = null
    private val binding: FragmentSubcategoriesBinding
        get() = _binding ?: throw RuntimeException("SubcategoriesFragmentBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubcategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.text?.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)

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
                    launchProductsFragment(null, searchQuery)
                }
                true
            } else {
                false
            }
        }



        viewModel.subcategories.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        adapter.onSubcategoryClickListener = {
            launchProductsFragment(it, null)
        }
    }

    private fun setupRecyclerView(view: View) {
        adapter = SubcategoriesAdapter()
        binding.rvCategories.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchProductsFragment(subcategory: Subcategory?, searchQuery: String?) {
        val currentDestination = findNavController().currentDestination?.id
        if (currentDestination == R.id.subcategoriesFragment) {
            findNavController().navigate(
                SubcategoriesFragmentDirections.actionSubcategoriesFragmentToAllProducts(
                    args.category,
                    subcategory,
                    searchQuery
                )
            )
        }
    }


}