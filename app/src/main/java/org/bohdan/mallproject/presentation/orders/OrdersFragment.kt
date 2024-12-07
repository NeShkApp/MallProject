package org.bohdan.mallproject.presentation.orders

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
import org.bohdan.mallproject.databinding.FragmentOrdersBinding
import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.presentation.adapters.OrderAdapter
import org.bohdan.mallproject.presentation.viewmodel.orders.OrdersViewModel

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private val viewModel: OrdersViewModel by viewModels()
    private var _binding: FragmentOrdersBinding ?= null
    val binding: FragmentOrdersBinding
        get() = _binding ?: throw RuntimeException("FragmentOrdersBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("OrdersFragment", "onViewCreated called")

        val orderAdapter = OrderAdapter().apply {
            onShopItemClickListener={
                findNavController().navigate(
                    OrdersFragmentDirections.actionOrdersFragmentToShopItemDetailsFragment(it.id)
                )
            }
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(context)
        binding.rvOrders.adapter = orderAdapter


        viewModel.orders.observe(viewLifecycleOwner){
            Log.d("OrdersFragment", "Orders received: ${it.size}")
            orderAdapter.submitList(it)
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}