package org.bohdan.mallproject.presentation.ui.item_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.bohdan.mallproject.R
import org.bohdan.mallproject.data.CartRepositoryImpl
import org.bohdan.mallproject.data.ShopItemDetailsRepositoryImpl
import org.bohdan.mallproject.databinding.FragmentShopItemDetailsBinding
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.usecase.cart.AddItemToCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.CheckIfItemInCartUseCase
import org.bohdan.mallproject.domain.usecase.cart.RemoveItemFromCartUseCase
import org.bohdan.mallproject.domain.usecase.item_details.GetShopItemDetailsByIdUseCase
import org.bohdan.mallproject.presentation.viewmodel.item_details.ShopItemDetailsViewModel
import org.bohdan.mallproject.presentation.viewmodel.item_details.ShopItemDetailsViewModelFactory

class ShopItemDetailsFragment : Fragment() {
    private val args by navArgs<ShopItemDetailsFragmentArgs>()

    private val viewModel: ShopItemDetailsViewModel by lazy {
        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val shopItemDetailsRepository = ShopItemDetailsRepositoryImpl(firestore)
        val cartRepository = CartRepositoryImpl(auth, firestore)

        // TODO: create DI object for easier views' models implementation
        val factory = ShopItemDetailsViewModelFactory(
            args.shopItemId,
            GetShopItemDetailsByIdUseCase(shopItemDetailsRepository),
            AddItemToCartUseCase(cartRepository),
            RemoveItemFromCartUseCase(cartRepository),
            CheckIfItemInCartUseCase(cartRepository)
        )
        ViewModelProvider(
            this, factory
        )[ShopItemDetailsViewModel::class.java]
    }

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

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvItemName.visibility = View.GONE
                binding.tvItemDescription.visibility = View.GONE
                binding.tvItemPrice.visibility = View.GONE
                binding.ratingBar.visibility = View.GONE
                binding.btnAddToCart.visibility = View.GONE
                binding.btnRemoveFromCart.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.tvItemName.visibility = View.VISIBLE
                binding.tvItemDescription.visibility = View.VISIBLE
                binding.tvItemPrice.visibility = View.VISIBLE
                binding.ratingBar.visibility = View.VISIBLE
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

        binding.radioGroupColors.setOnCheckedChangeListener { _, checkedId ->
            val color = when (checkedId) {
                R.id.rb_color_brown -> "Brown"
                R.id.rb_color_black -> "Black"
                else -> "Unknown"
            }
            Toast.makeText(requireContext(), "Selected color: $color", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}