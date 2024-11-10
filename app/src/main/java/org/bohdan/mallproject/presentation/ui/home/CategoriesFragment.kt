package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.bohdan.mallproject.databinding.FragmentCategoriesBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.presentation.adapter.CategoriesAdapter
import org.bohdan.mallproject.presentation.viewmodel.CategoriesViewModel

class CategoriesFragment : Fragment() {

    private lateinit var adapter: CategoriesAdapter

    private val viewModel: CategoriesViewModel by lazy {
        ViewModelProvider(this)[CategoriesViewModel::class.java]
    }

    private var _binding: FragmentCategoriesBinding? = null
    private val binding: FragmentCategoriesBinding
        get() = _binding ?: throw RuntimeException("FragmentCategoriesBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)

        viewModel.categories.observe(viewLifecycleOwner){categories ->
            categories?.let{
                adapter.categories = it
            }
        }

        adapter.onCategoryClickListener = {
            launchSubcategoriesFragment(it)
        }
    }

    private fun launchSubcategoriesFragment(category: Category) {
        Log.d("CategoriesFragment", "Launching SubcategoriesFragment with category: ${category.toString()}")
        findNavController().navigate(
            CategoriesFragmentDirections.actionCategoriesFragmentToSubcategoriesFragment(category)
        )
        Log.d("CategoriesFragmentDirections", "launchSubcategoriesFragment: worked")
    }

    private fun setupRecyclerView(view: View) {
        adapter = CategoriesAdapter()
        binding.rvCategories.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}