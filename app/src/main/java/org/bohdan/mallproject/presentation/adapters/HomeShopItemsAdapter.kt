package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ItemShopEnabledBinding
import org.bohdan.mallproject.domain.model.ShopItem

class HomeShopItemsAdapter : RecyclerView.Adapter<HomeShopItemsAdapter.ShopItemViewHolder>() {

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    // TODO: probably delete notifyDataSetChanged
    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.bind(shopItem)
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }


}