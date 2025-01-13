package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bohdan.mallproject.databinding.ItemCategoryBinding
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.presentation.adapters.diffcallback.CategoryDiffCallback

class CategoriesAdapter : ListAdapter<Category, CategoriesAdapter.CategoryViewHolder>(
    CategoryDiffCallback()
) {

    var onCategoryClickListener: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(
            binding,
            onCategoryClickListener
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        category?.let{
            holder.bind(category)
        }
    }


    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onCategoryClickListener: ((Category) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategory.text = category.name
            Glide.with(binding.root.context)
                .load(category.imageUrl)
                .into(binding.ivCategory)

            binding.root.setOnClickListener {
                onCategoryClickListener?.invoke(category)
            }
        }
    }
}