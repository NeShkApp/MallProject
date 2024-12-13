package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.databinding.FragmentFilterShopItemsBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.presentation.viewmodel.filter.FilterViewModel

@AndroidEntryPoint
class FilterShopItemsFragment(
    private val listener: FilterOptionListener,
    private val selectedCategory: Category?,
    private val selectedSubcategory: Subcategory?,
    private val selectedSearchQuery: String?
) : BottomSheetDialogFragment() {

    private val viewModel: FilterViewModel by viewModels()
    private var _binding: FragmentFilterShopItemsBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var subcategoryAdapter: ArrayAdapter<String>

    interface FilterOptionListener {
        fun onFilterApplied(category: Category?, subcategory: Subcategory?, searchQuery: String?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterShopItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        subcategoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSubcategory.adapter = subcategoryAdapter

        observeViewModel()

        viewModel.loadCategories()

        binding.buttonApplyFilter.setOnClickListener {
            val selectedCategory = binding.spinnerCategory.selectedItem as? String
            val selectedSubcategory = binding.spinnerSubcategory.selectedItem as? String

            val category = viewModel.categories.value?.find { it.name == selectedCategory }
            val subcategory = viewModel.subcategories.value?.find { it.name == selectedSubcategory }

            listener.onFilterApplied(category, subcategory, selectedSearchQuery)
            dismiss()
        }

        // Слухач для зміни вибраної категорії
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSubcategories()  // Оновлюємо підкатегорії після зміни категорії
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }


    private fun observeViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val allCategories = mutableListOf("All Categories")
            allCategories.addAll(categories.map { it.name })
            categoryAdapter.clear()
            categoryAdapter.addAll(allCategories)
            categoryAdapter.notifyDataSetChanged()

            selectedCategory?.let {
                val categoryPosition = allCategories.indexOf(it.name)
                binding.spinnerCategory.setSelection(categoryPosition.takeIf { it >= 0 } ?: 0)
                updateSubcategories()
            }
        }

        viewModel.subcategories.observe(viewLifecycleOwner) { subcategories ->
            val allSubcategories = mutableListOf("All Subcategories")
            allSubcategories.addAll(subcategories.map { it.name })

            subcategoryAdapter.clear()
            subcategoryAdapter.addAll(allSubcategories)
            subcategoryAdapter.notifyDataSetChanged()

            selectedSubcategory?.let {
                val subcategoryPosition = allSubcategories.indexOf(it.name)
                binding.spinnerSubcategory.setSelection(subcategoryPosition.takeIf { it >= 0 } ?: 0)
            }
        }
    }

    private fun updateSubcategories() {
        // Оновлюємо підкатегорії, якщо вибрана категорія не "всі категорії"
        val selectedCategory = binding.spinnerCategory.selectedItem as? String
        if (selectedCategory != "All Categories") {
            val category = viewModel.categories.value?.find { it.name == selectedCategory }
            category?.let {
                // Завантажуємо підкатегорії для вибраної категорії
                viewModel.loadSubcategories(it)
                binding.spinnerSubcategory.isEnabled = true // робимо спінер підкатегорій доступним
            }
        } else {
            // Якщо вибрано "всі категорії", робимо спінер підкатегорій недоступним
            binding.spinnerSubcategory.isEnabled = false
            subcategoryAdapter.clear()
            subcategoryAdapter.add("All Subcategories")
            subcategoryAdapter.notifyDataSetChanged()
        }
    }
}
