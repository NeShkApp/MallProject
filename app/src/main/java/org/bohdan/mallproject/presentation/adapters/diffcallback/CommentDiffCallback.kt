package org.bohdan.mallproject.presentation.adapters.diffcallback

import androidx.recyclerview.widget.DiffUtil
import org.bohdan.mallproject.domain.model.Comment

class CommentDiffCallback: DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

}