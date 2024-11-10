package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentCategoriesBinding
import org.bohdan.mallproject.databinding.FragmentSubcategoriesBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.presentation.adapter.CategoriesAdapter
import org.bohdan.mallproject.presentation.adapter.SubcategoriesAdapter
import org.bohdan.mallproject.presentation.viewmodel.CategoriesViewModel
import org.bohdan.mallproject.presentation.viewmodel.SubcategoriesViewModel
import org.bohdan.mallproject.presentation.viewmodel.SubcategoriesViewModelFactory

class SubcategoriesFragment : Fragment() {

    private lateinit var adapter: SubcategoriesAdapter

    private val args by navArgs<SubcategoriesFragmentArgs>()

    private val viewModel: SubcategoriesViewModel by lazy {
        ViewModelProvider(
            this,
            SubcategoriesViewModelFactory(
                args.category,
                requireActivity().application
            )
        )[SubcategoriesViewModel::class.java]
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)

        viewModel.subcategories.observe(viewLifecycleOwner) { subcategory ->
            subcategory?.let {
                adapter.subcategories = it
            }
        }

        adapter.onSubcategoryClickListener = {
            launchProductsFragment()
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

    private fun launchProductsFragment() {
        findNavController().navigate(
            SubcategoriesFragmentDirections.actionSubcategoriesFragmentToAllProducts()
        )
    }

}