package org.bohdan.mallproject.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import org.bohdan.mallproject.domain.model.Order

class OrderDiffCallback: DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.orderId == newItem.orderId
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }

}