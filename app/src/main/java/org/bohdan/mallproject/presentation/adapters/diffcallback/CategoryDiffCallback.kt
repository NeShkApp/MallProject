package org.bohdan.mallproject.presentation.adapters.diffcallback

import androidx.recyclerview.widget.DiffUtil
import org.bohdan.mallproject.domain.model.Category

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}