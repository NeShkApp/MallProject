package org.bohdan.mallproject.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ItemCartBinding
import org.bohdan.mallproject.domain.model.ShopItem

class CartAdapter : ListAdapter<ShopItem, CartAdapter.CartViewHolder>(ShopItemDiffCallback()) {

    var onCartItemClickListener: ((ShopItem) -> Unit)? = null
    var onRemoveClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, onCartItemClickListener, onRemoveClickListener)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.bind(shopItem)
    }

    class CartViewHolder(
        private val binding: ItemCartBinding,
        private val onCartItemClickListener: ((ShopItem) -> Unit)?,
        private val onRemoveClickListener: ((ShopItem) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem) {
            binding.itemName.text = shopItem.name
            binding.itemDescription.text = shopItem.description
            binding.itemPrice.text = "Price: $${shopItem.price}"
            binding.itemRating.rating = shopItem.rating

            Glide.with(binding.root.context)
                .load(shopItem.imageUrl)
                .into(binding.itemImage)

            binding.root.setOnClickListener {
                onCartItemClickListener?.invoke(shopItem)
            }

            binding.removeItemButton.setOnClickListener {
                onRemoveClickListener?.invoke(shopItem)
            }
        }
    }
}
