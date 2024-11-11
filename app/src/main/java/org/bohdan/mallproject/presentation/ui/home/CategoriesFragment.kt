package org.bohdan.mallproject.presentation.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentCategoriesBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory
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
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)

        binding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Перевірка, чи є текст в полі
                if (s.isNullOrEmpty()) {
                    // Якщо текст порожній, приховуємо кнопку "Search" на клавіатурі
                    binding.etSearch.imeOptions = EditorInfo.IME_ACTION_NONE
                } else {
                    // Якщо текст не порожній, показуємо кнопку "Search" на клавіатурі
                    binding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Обробка натискання на кнопку "Готово" на клавіатурі
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = binding.etSearch.text.toString().trim()

                if (searchQuery.isNotEmpty()) {
                    // Перехід до фрагмента всіх продуктів, якщо є запит
                    launchAllProductFragment(searchQuery)
                }
                true
            } else {
                false
            }
        }


        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                adapter.categories = it
            }
        }

        adapter.onCategoryClickListener = {
            launchSubcategoriesFragment(it)
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

    private fun setupRecyclerView(view: View) {
        adapter = CategoriesAdapter()
        binding.rvCategories.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}