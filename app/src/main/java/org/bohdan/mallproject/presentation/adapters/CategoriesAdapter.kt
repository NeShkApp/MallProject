package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.Category

class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    var onCategoryClickListener: ((Category) -> Unit)? = null

    var categories = listOf<Category>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_category,
            parent,
            false
        )
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.CategoryViewHolder, position: Int) {
        val category = categories[position]
//        holder.tvCategory.text = category.name
        holder.tvCategory.text = category.name
        holder.itemView.setOnClickListener {
            onCategoryClickListener?.invoke(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvCategory = view.findViewById<TextView>(R.id.tv_category)
    }
}