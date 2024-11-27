package org.bohdan.mallproject.presentation.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
    var onQuantityChangedListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(
            binding,
            onCartItemClickListener,
            onRemoveClickListener,
            onQuantityChangedListener
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.bind(shopItem)
    }

    class CartViewHolder(
        private val binding: ItemCartBinding,
        private val onCartItemClickListener: ((ShopItem) -> Unit)?,
        private val onRemoveClickListener: ((ShopItem) -> Unit)?,
        private val onQuantityChangedListener: ((ShopItem) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shopItem: ShopItem) {
            if (shopItem.quantityInStock == 0) {
                binding.itemName.text = shopItem.name
                binding.itemPrice.text = "${shopItem.price}"
                binding.itemRemainingQuantity.text = "Not available"
                binding.decreaseButton.isEnabled = false
                binding.increaseButton.isEnabled = false

                Glide.with(binding.root.context)
                    .load(shopItem.imageUrl)
                    .into(binding.itemImage)

                binding.itemQuantity.visibility = View.GONE
                binding.tvLeft.visibility = View.GONE
            } else {
                binding.itemName.text = shopItem.name
                binding.itemDescription.text = shopItem.description
                binding.itemPrice.text = "${shopItem.price}"
                binding.itemRating.rating = shopItem.rating
                binding.itemQuantity.text = shopItem.selectedQuantity.toString()
                binding.itemRemainingQuantity.text = shopItem.quantityInStock.toString()

                Glide.with(binding.root.context)
                    .load(shopItem.imageUrl)
                    .into(binding.itemImage)

                updateButtonStates(shopItem)

                binding.decreaseButton.setOnClickListener {
                    if (shopItem.selectedQuantity > 1) {
                        shopItem.selectedQuantity--
                        binding.itemQuantity.text = shopItem.selectedQuantity.toString()
                        updateButtonStates(shopItem)
                        onQuantityChangedListener?.invoke(shopItem)
                    }
                }

                binding.increaseButton.setOnClickListener {
                    if (shopItem.selectedQuantity < shopItem.quantityInStock) {
                        shopItem.selectedQuantity++
                        binding.itemQuantity.text = shopItem.selectedQuantity.toString()
                        updateButtonStates(shopItem)
                        onQuantityChangedListener?.invoke(shopItem)
                    }
                }

            }

            binding.root.setOnClickListener {
                onCartItemClickListener?.invoke(shopItem)
            }

            binding.removeItemButton.setOnClickListener {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to remove this item from the cart?")
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

        }

        private fun updateButtonStates(shopItem: ShopItem) {
            binding.decreaseButton.isEnabled = shopItem.selectedQuantity > 1

            if (shopItem.quantityInStock == 0) {
                binding.increaseButton.isEnabled = false
                binding.decreaseButton.isEnabled = false
            } else {
                binding.increaseButton.isEnabled =
                    shopItem.selectedQuantity < shopItem.quantityInStock
            }
        }
    }
}
