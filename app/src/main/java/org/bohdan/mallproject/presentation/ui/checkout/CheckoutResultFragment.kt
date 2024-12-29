package org.bohdan.mallproject.presentation.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentCheckoutResultBinding

class CheckoutResultFragment: Fragment() {
    private val args by navArgs<CheckoutResultFragmentArgs>()

    private var _binding: FragmentCheckoutResultBinding? = null
    private val binding: FragmentCheckoutResultBinding
        get() = _binding ?: throw RuntimeException("FragmentResultBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.transactionResult) {
            binding.tvResult.text = getString(R.string.transaction_success)
            binding.ivStatusImage.setImageResource(R.mipmap.success)
        } else {
            binding.tvResult.text = getString(R.string.transaction_failure)
            binding.ivStatusImage.setImageResource(R.mipmap.failure)
        }

        binding.btnBackToHome.setOnClickListener {
            findNavController().navigate(
                CheckoutResultFragmentDirections.actionCheckoutResultFragmentToCategoriesFragment()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
