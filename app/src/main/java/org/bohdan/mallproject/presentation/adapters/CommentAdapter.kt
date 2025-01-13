package org.bohdan.mallproject.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bohdan.mallproject.databinding.ItemCommentBinding
import org.bohdan.mallproject.domain.model.Comment
import org.bohdan.mallproject.presentation.adapters.diffcallback.CommentDiffCallback

class CommentAdapter: ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    class CommentViewHolder(
        private val binding: ItemCommentBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment){
            binding.tvCommentText.text = comment.text
            binding.tvUsername.text = comment.username
            binding.ratingBar.rating = comment.rating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentItem = getItem(position)
        commentItem?.let{
            holder.bind(commentItem)
        }
    }

}