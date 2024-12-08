package org.bohdan.mallproject.presentation.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentFavoriteBinding
import org.bohdan.mallproject.presentation.adapters.FavoriteAdapter
import org.bohdan.mallproject.presentation.viewmodel.favorite.FavoriteViewModel

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var adapter: FavoriteAdapter

    private val viewModel: FavoriteViewModel by viewModels()

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoriteBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        adapter.onRemoveClickListener = {
            viewModel.removeFavoriteItem(it.id)
        }
        adapter.onFavoriteItemClickListener = {
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToShopItemDetailsFragment(it.id)
            )
        }
        binding.goToShopButton.setOnClickListener {
            findNavController().navigate(
                FavoriteFragmentDirections.actionFavoriteFragmentToCategoriesFragment()
            )
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter()
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoritesRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.favoriteItems.observe(viewLifecycleOwner) {
            Log.d("FavoriteFragment", "Observed favorite items: $it")
            adapter.submitList(it)

            with(binding) {
                if (it.isNullOrEmpty()) {
                    favoritesRecyclerView.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE
                } else {
                    favoritesRecyclerView.visibility = View.VISIBLE
                    emptyStateLayout.visibility = View.GONE
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.resetErrorMessage()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            with(binding) {
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    favoritesRecyclerView.visibility = View.GONE
                    emptyStateLayout.visibility = View.GONE
                }
            }
        }


    }

}