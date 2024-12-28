package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bohdan.mallproject.R
import org.bohdan.mallproject.databinding.ItemSubcategoryBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.Subcategory

class SubcategoriesAdapter: ListAdapter<Subcategory, SubcategoriesAdapter.SubcategoryViewHolder>(SubcategoryDiffCallback()) {

    var onSubcategoryClickListener: ((Subcategory) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubcategoryViewHolder {
        val binding = ItemSubcategoryBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return SubcategoryViewHolder(
            binding,
            onSubcategoryClickListener
        )
    }

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        val subcategory = getItem(position)
        subcategory?.let{
            holder.bind(subcategory)
        }
    }

    class SubcategoryViewHolder(
        private val binding: ItemSubcategoryBinding,
        private val onSubcategoryClickListener: ((Subcategory) -> Unit)? = null
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(subcategory: Subcategory){
            binding.tvSubcategory.text = subcategory.name

            Glide.with(binding.root.context)
                .load(subcategory.imageUrl)
                .into(binding.ivIcon)

            binding.root.setOnClickListener {
                onSubcategoryClickListener?.invoke(subcategory)
            }
        }
    }
}