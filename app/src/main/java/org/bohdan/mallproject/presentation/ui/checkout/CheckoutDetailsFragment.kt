package org.bohdan.mallproject.presentation.ui.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentCheckoutDetailsBinding


class CheckoutDetailsFragment : Fragment() {
    private val args by navArgs<CheckoutDetailsFragmentArgs>()

    private var _binding: FragmentCheckoutDetailsBinding? = null
    private val binding: FragmentCheckoutDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentCheckoutDetailsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val shopItems = args.shopItems.toList()
//
//        shopItems.forEach {
//            Log.d("CheckoutDetailsFragment", "Item: ${it.name}, Quantity: ${it.selectedQuantity}")
//        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnContinueToOrderSummary.setOnClickListener {
            val clientName = binding.etName.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val selectedPaymentMethodId = binding.rgPaymentMethod.checkedRadioButtonId

            if (clientName.isEmpty() || address.isEmpty() || selectedPaymentMethodId == -1) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val paymentMethod = when (selectedPaymentMethodId) {
                    R.id.rbCard -> "Card"
                    R.id.rbCash -> "Cash"
                    else -> ""
                }

                findNavController().navigate(
                    CheckoutDetailsFragmentDirections.actionCheckoutDetailsFragmentToOrderSummaryFragment(
                        clientName,
                        address,
                        paymentMethod,
                        args.shopItems
                    )
                )
            }

        }
    }
}