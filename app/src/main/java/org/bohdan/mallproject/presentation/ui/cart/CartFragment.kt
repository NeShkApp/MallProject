package org.bohdan.mallproject.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.databinding.FragmentCartBinding
import org.bohdan.mallproject.presentation.adapters.CartAdapter
import org.bohdan.mallproject.presentation.viewmodel.cart.CartViewModel

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var adapter: CartAdapter

    private val viewModel: CartViewModel by lazy{
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    private var _binding: FragmentCartBinding?= null
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

        adapter = CartAdapter()
        binding.recyclerViewCart.adapter = adapter

        setupObservers()

        setupClickListeners()

    }

    private fun setupClickListeners() {
        adapter.onCartItemClickListener = {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToShopItemDetailsFragment(it.id)
            )
        }

        adapter.onRemoveClickListener = {
            viewModel.removeCartItem(it.id)
            Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.cartItems.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItems()
    }
}