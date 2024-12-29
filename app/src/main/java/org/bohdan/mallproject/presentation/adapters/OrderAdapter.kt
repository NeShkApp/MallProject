package org.bohdan.mallproject.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.databinding.ItemCartBinding
import org.bohdan.mallproject.databinding.ItemOrderBinding
import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.model.ShopItem

class OrderAdapter: ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapter.OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val productAdapter = ItemOrderProductAdapter()

        init {
            binding.rvOrderItems.apply {
                layoutManager = LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = productAdapter
            }
        }

        fun bind(order: Order) {
            binding.tvTimestamp.text = order.formattedTimestamp
            binding.tvTotalPrice.text = "${order.totalAmount} $"

            val products = order.shopItems.map{it}
            productAdapter.submitList(products)

            productAdapter.onShopItemClickListener = { shopItem ->
                onShopItemClickListener?.invoke(shopItem)
            }
        }

    }
}