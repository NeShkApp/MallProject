package org.bohdan.mallproject.presentation.ui.item_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentShopItemDetailsBinding
import org.bohdan.mallproject.presentation.viewmodel.item_details.ShopItemDetailsViewModel

@AndroidEntryPoint
class ShopItemDetailsFragment : Fragment() {
    private val args by navArgs<ShopItemDetailsFragmentArgs>()

    private val viewModel: ShopItemDetailsViewModel by viewModels()

    private var _binding: FragmentShopItemDetailsBinding? = null
    private val binding: FragmentShopItemDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemDetailsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadShopItemById(args.shopItemId)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {

        viewModel.shopItem.observe(viewLifecycleOwner){shopItem ->
            binding.tvItemName.text = shopItem.name
            binding.tvItemDescription.text = shopItem.description
            binding.tvItemPrice.text = String.format("$%.2f", shopItem.price)
            binding.ratingBar.rating = shopItem.rating
            Glide.with(this).load(shopItem.imageUrl).into(binding.imageView)

            viewModel.checkStockAvailability(shopItem)
        }

        viewModel.isInCart.observe(viewLifecycleOwner) { isInCart ->
            if (isInCart == true) {
                binding.btnAddToCart.visibility = View.GONE
                binding.btnRemoveFromCart.visibility = View.VISIBLE
            } else {
                binding.btnAddToCart.visibility = View.VISIBLE
                binding.btnRemoveFromCart.visibility = View.GONE
            }
        }

        viewModel.isInFavorite.observe(viewLifecycleOwner) { isInFavorite ->
            if (isInFavorite == true) {
                binding.btnAddToFavorite.text = "Remove from Favorites"
                binding.btnAddToFavorite.setOnClickListener {
                    viewModel.removeItemFromFavorite(args.shopItemId)
                    Toast.makeText(requireContext(), "Item removed from favorites", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.btnAddToFavorite.text = "Add to Favorites"
                binding.btnAddToFavorite.setOnClickListener {
                    viewModel.addItemToFavorite(args.shopItemId)
                    Toast.makeText(requireContext(), "Item added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.isAddToCartEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnAddToCart.isEnabled = isEnabled
            binding.btnAddToCart.text = if (isEnabled) "Add to Cart" else "Out of Stock"
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.contentLayout.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.contentLayout.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }

    }

    private fun setupListeners() {
        binding.btnAddToCart.setOnClickListener {
            viewModel.addItemToCart(args.shopItemId)
            Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
        }

        binding.btnRemoveFromCart.setOnClickListener {
            viewModel.removeItemFromCart(args.shopItemId)
            Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddToFavorite.setOnClickListener {
            viewModel.addItemToFavorite(args.shopItemId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}