package org.bohdan.mallproject.presentation.ui.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.bohdan.mallproject.databinding.FragmentCheckoutSummaryBinding
import org.bohdan.mallproject.presentation.viewmodel.checkout.CheckoutSummaryViewModel

@AndroidEntryPoint
class CheckoutSummaryFragment : Fragment() {

    private val viewModel: CheckoutSummaryViewModel by viewModels()
    private val args by navArgs<CheckoutSummaryFragmentArgs>()

    private var _binding: FragmentCheckoutSummaryBinding? = null
    private val binding: FragmentCheckoutSummaryBinding
        get() = _binding ?: throw RuntimeException("FragmentCheckoutSummaryBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvName.text = "${tvName.text}\n${args.clientName}"
            tvAddress.text = "${tvAddress.text}\n${args.address}"
            tvPaymentMethod.text = "${tvPaymentMethod.text}\n${args.paymentMethod}"
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnConfirmOrder.setOnClickListener {

            val transactionResult = true
            if(transactionResult){
                viewModel.addOrder(args.shopItems.toList())
                viewModel.cleanCart()
            }
            findNavController().navigate(
                CheckoutSummaryFragmentDirections.actionOrderSummaryFragmentToCheckoutResultFragment(transactionResult)
            )
        }
    }
}
