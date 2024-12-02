package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.databinding.ItemProfileOptionBinding
import org.bohdan.mallproject.domain.model.ProfileOption

class ProfileOptionsAdapter(
    private val onOptionClick: (ProfileOption) -> Unit
) : RecyclerView.Adapter<ProfileOptionsAdapter.OptionViewHolder>() {

    private val options = mutableListOf<ProfileOption>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemProfileOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    fun submitList(newOptions: List<ProfileOption>) {
        options.addAll(newOptions)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    inner class OptionViewHolder(
        private val binding: ItemProfileOptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(option: ProfileOption) {
            binding.textViewTitle.text = option.title

            option.iconResId?.let {
                binding.imageViewIcon.setImageResource(it)
            }

            binding.root.setOnClickListener {
                onOptionClick(option)
            }
        }
    }
}