package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bohdan.mallproject.databinding.ItemShopEnabledBinding
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.presentation.adapters.diffcallback.ShopItemDiffCallback

class HomeShopItemsAdapter : ListAdapter<ShopItem, HomeShopItemsAdapter.ShopItemViewHolder>(
    ShopItemDiffCallback()
) {

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null
    var onFavoriteClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ItemShopEnabledBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopItemViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        shopItem?.let{ item ->
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onShopItemClickListener?.invoke(item)
            }
        }
    }

    class ShopItemViewHolder(
        private val binding: ItemShopEnabledBinding
    ) :RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem){
            binding.tvName.text = shopItem.name
            binding.tvPrice.text = shopItem.price.toString()
            binding.tvDescription.text = shopItem.description

            if (shopItem.new) {
                binding.tvBadge.apply {
                    text = "NEW"
                    visibility = View.VISIBLE
                }
            } else if (shopItem.discount > 0) {
                binding.tvBadge.apply {
                    text = "-${shopItem.discount}%"
                    visibility = View.VISIBLE
                }
            } else {
                binding.tvBadge.visibility = View.GONE
            }

            val finalPrice = if (shopItem.discount > 0) {
                shopItem.price - (shopItem.price * shopItem.discount / 100)
            } else {
                shopItem.price
            }
            binding.tvPrice.text = String.format("$%.2f", finalPrice)

            Glide.with(binding.ivProductImage.context)
                .load(shopItem.imageUrl)
                .centerCrop()
                .into(binding.ivProductImage)
        }
    }


}