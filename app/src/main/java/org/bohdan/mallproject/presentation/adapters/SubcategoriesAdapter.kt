package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.R
import org.bohdan.mallproject.domain.model.Subcategory

class SubcategoriesAdapter: RecyclerView.Adapter<SubcategoriesAdapter.SubcategoryViewHolder>() {
    var onSubcategoryClickListener: ((Subcategory) -> Unit)? = null

    var subcategories = listOf<Subcategory>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubcategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_subcategory,
            parent,
            false
        )
        return SubcategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        val subcategory = subcategories[position]
        holder.tvSubcategory.text = subcategory.name
        holder.itemView.setOnClickListener {
            onSubcategoryClickListener?.invoke(subcategory)
        }
    }

    override fun getItemCount(): Int {
        return subcategories.size
    }

    class SubcategoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvSubcategory = view.findViewById<TextView>(R.id.tv_subcategory)
    }
}