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

        // TODO: fix unnecessary calls of releaseCartItems!!! 
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

    }

    private fun setupObservers() {

        viewModel.cartItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
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
            binding.totalPriceText.text = String.format("Total: $%.2f", it)
        }

        viewModel.navigateToCheckout.observe(viewLifecycleOwner) {
            if (it == true) {
                viewModel.getCartItems()
                val items = viewModel.cartItems.value?.let {
                    viewModel.getAvailableCartItems(
                        it
                    )
                }
                if (items != null) {
                    viewModel.reserveCartItems(items)
                    findNavController().navigate(
                        CartFragmentDirections.actionCartFragmentToCheckoutDetailsFragment(items.toTypedArray())
                    )
                }
                viewModel.resetNavigateToCheckout()
//                viewModel.resetToastMessage()
            }
        }

        viewModel.isCartEmpty.observe(viewLifecycleOwner) {
            binding.buttonCheckout.isEnabled = !it
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