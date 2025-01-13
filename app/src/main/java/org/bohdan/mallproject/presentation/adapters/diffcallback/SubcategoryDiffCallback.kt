package org.bohdan.mallproject.presentation.adapters.diffcallback

import androidx.recyclerview.widget.DiffUtil
import org.bohdan.mallproject.domain.model.Subcategory

class SubcategoryDiffCallback : DiffUtil.ItemCallback<Subcategory>() {
    override fun areItemsTheSame(oldItem: Subcategory, newItem: Subcategory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subcategory, newItem: Subcategory): Boolean {
        return oldItem == newItem
    }
}