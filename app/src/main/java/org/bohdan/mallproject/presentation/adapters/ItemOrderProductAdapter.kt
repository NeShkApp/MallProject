package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bohdan.mallproject.databinding.ItemOrderProductBinding
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.presentation.adapters.diffcallback.ShopItemDiffCallback

class ItemOrderProductAdapter : ListAdapter<ShopItem, ItemOrderProductAdapter.ItemOrderProductViewHolder>(
    ShopItemDiffCallback()
){

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOrderProductViewHolder {
        val binding = ItemOrderProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemOrderProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemOrderProductViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.bind(shopItem)
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    class ItemOrderProductViewHolder(
        private val binding: ItemOrderProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem) {
            binding.tvProductName.text = shopItem.name
            binding.tvProductQuantity.text = "x${shopItem.selectedQuantity}"

            Glide.with(binding.ivProductImage.context)
                .load(shopItem.imageUrl)
                .centerCrop()
                .into(binding.ivProductImage)
        }
    }

}