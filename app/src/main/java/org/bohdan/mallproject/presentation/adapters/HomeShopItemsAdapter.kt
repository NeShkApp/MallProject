package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ItemShopEnabledBinding
import org.bohdan.mallproject.domain.model.ShopItem

class HomeShopItemsAdapter : ListAdapter<ShopItem, HomeShopItemsAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    class ShopItemViewHolder(
        private val binding: ItemShopEnabledBinding
    ) :RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem){
            binding.tvName.text = shopItem.name
            binding.tvPrice.text = shopItem.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ItemShopEnabledBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopItemViewHolder(binding)
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


}