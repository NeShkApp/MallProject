package org.bohdan.mallproject.presentation.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.bohdan.mallproject.databinding.FragmentCartBinding
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.presentation.adapters.CartAdapter
import org.bohdan.mallproject.presentation.ui.favorite.FavoriteFragmentDirections
import org.bohdan.mallproject.presentation.viewmodel.cart.CartViewModel

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var adapter: CartAdapter

    private val viewModel: CartViewModel by viewModels()

    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding ?: throw RuntimeException("FragmentCartBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isRestored) {
            viewModel.getCartItems()
            val items = viewModel.cartItems.value?.let {
                viewModel.getAvailableCartItems(
                    it
                )
            }
            if (items != null) {
                viewModel.releaseCartItems(items)
            }
            Log.d("CartFragment", "Fragment state restored via ViewModel")
        }else {
            Log.d("CartFragment", "Fragment created anew")
            viewModel.isRestored = true
        }

        adapter = CartAdapter()
        binding.recyclerViewCart.adapter = adapter

        setupObservers()
        setupClickListeners()

        viewModel.getCartItems()
        viewModel.calculateTotalPrice()
    }

    private fun setupClickListeners() {
        adapter.onCartItemClickListener = {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToShopItemDetailsFragment(it.id)
            )
        }

        adapter.onRemoveClickListener = {
            viewModel.removeCartItem(it.id)
            viewModel.calculateTotalPrice()
            Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
        }

        adapter.onQuantityChangedListener = { shopItem ->
            viewModel.updateSelectedQuantity(shopItem.id, shopItem.selectedQuantity)
            viewModel.calculateTotalPrice()
        }

        binding.buttonCheckout.setOnClickListener {
            viewModel.checkAndStartCheckout()
        }

        binding.goToShopButton.setOnClickListener {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToCategoriesFragment()
            )
        }

    }

    private fun setupObservers() {

        viewModel.cartItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)

            with(binding) {
                if (it.isNullOrEmpty()) {
                    recyclerViewCart.visibility = View.GONE
                    emptyStateLayout.visibility = View.VISIBLE
                } else {
                    recyclerViewCart.visibility = View.VISIBLE
                    emptyStateLayout.visibility = View.GONE
                }
            }
            viewModel.updateCartEmptyState()
            viewModel.calculateTotalPrice()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.resetToastMessage()
            }
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.totalPriceNumber.text = String.format("$%.2f", it)
        }

//        viewModel.navigateToCheckout.observe(viewLifecycleOwner) {
//            if (it == true) {
//                viewModel.getCartItems()
//                val items = viewModel.cartItems.value?.let {
//                    viewModel.getAvailableCartItems(
//                        it
//                    )
//                }
//                if (items != null) {
//                    viewModel.reserveCartItems(items)
//                    findNavController().navigate(
//                        CartFragmentDirections.actionCartFragmentToCheckoutDetailsFragment(items.toTypedArray())
//                    )
//                }
//                viewModel.resetNavigateToCheckout()
//            }
//        }
        viewModel.navigateToCheckout.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.getCartItems()
                val items = viewModel.cartItems.value?.let { cartItems ->
                    viewModel.getAvailableCartItems(cartItems)
                }

                if (!items.isNullOrEmpty()) {
                    // Logowanie wybranej liczby produktów
                    items.forEach { item ->
                        Log.d("CartFragment", "Item ID: ${item.id}, Selected Quantity: ${item.selectedQuantity}")
                    }

                    // Rezerwowanie wybranych towarów
                    viewModel.reserveCartItems(items)

                    // Nawigacja do CheckoutDetailsFragment
                    findNavController().navigate(
                        CartFragmentDirections.actionCartFragmentToCheckoutDetailsFragment(items.toTypedArray())
                    )
                } else {
                    // Logowanie przypadku, gdy nie ma wybranych produktów
                    Log.e("CartFragment", "No items selected for checkout.")
                }

                viewModel.resetNavigateToCheckout()
            }
        }


        viewModel.isCartEmpty.observe(viewLifecycleOwner) {
            binding.buttonCheckout.isEnabled = !it
            binding.recyclerViewCart.visibility = if (it) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            with(binding) {
                progressBarCart.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isLoading) {
                    recyclerViewCart.visibility = View.GONE
                }else{
                    recyclerViewCart.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onDetach() {
        super.onDetach()
        val items = viewModel.cartItems.value
        items?.let {
            runBlocking {
                viewModel.releaseCartItemsBlocking(items)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItems()
    }

}