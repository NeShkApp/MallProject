package org.bohdan.mallproject.presentation.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bohdan.mallproject.databinding.ItemFavoriteBinding
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.presentation.adapters.diffcallback.ShopItemDiffCallback

class FavoriteAdapter : ListAdapter<ShopItem, FavoriteAdapter.FavoriteViewHolder>(
    ShopItemDiffCallback()
) {

    var onFavoriteItemClickListener: ((ShopItem) -> Unit)? = null
    var onRemoveClickListener: ((ShopItem) -> Unit)? = null

    inner class FavoriteViewHolder(
        private val binding: ItemFavoriteBinding,
        private val onFavoriteItemClickListener: ((ShopItem) -> Unit)?,
        private val onRemoveClickListener: ((ShopItem) -> Unit)?,
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(shopItem: ShopItem){
            binding.itemName.text = shopItem.name
            binding.itemDescription.text = shopItem.description
            binding.itemRating.rating = shopItem.rating
//            binding.itemPrice.text = "${shopItem.price}"
            val finalPrice = if (shopItem.discount > 0) {
                shopItem.price - (shopItem.price * shopItem.discount / 100)
            } else {
                shopItem.price
            }
            binding.itemPrice.text = String.format("$%.2f", finalPrice)

            Glide.with(binding.root.context)
                .load(shopItem.imageUrl)
                .into(binding.itemImage)

            binding.removeItemButton.setOnClickListener{
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to remove this item from the favorite?")
                    .setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                        onRemoveClickListener?.invoke(shopItem)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            binding.root.setOnClickListener {
                onFavoriteItemClickListener?.invoke(shopItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(
            binding,
            onFavoriteItemClickListener,
            onRemoveClickListener
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val shopItem = getItem(position)
        shopItem?.let{
            holder.bind(shopItem)
        }
    }
}