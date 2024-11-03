package org.bohdan.mallproject.presentation.ui.itemdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.FragmentShopItemDetailsBinding

class ShopItemDetailsFragment : Fragment() {
    private val args by navArgs<ShopItemDetailsFragmentArgs>()

    private var _binding: FragmentShopItemDetailsBinding? = null
    private val binding: FragmentShopItemDetailsBinding
        get() = _binding ?:throw RuntimeException("FragmentShopItemDetailsBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        val shopItem = args.shopItem

        binding.tvItemName.text = shopItem.name
        binding.tvItemDescription.text = shopItem.description
        binding.tvItemCategory.text = shopItem.category
        binding.tvItemPrice.text = String.format("$%.2f", shopItem.price) // Форматування ціни
        binding.tvItemRating.text = String.format("Rating: %.1f", shopItem.rating)

        // Завантаження зображення товару
        Glide.with(this)
            .load(shopItem.imageUrl)
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}