package com.example.tagginginchat.ui.viewBased

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.databinding.UserLayoutBinding

class TagAdapter(
    private val users: List<User>,
    private val onUserSelected: ((User) -> Unit)? = null,
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    inner class TagViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userName.text = user.name
            binding.profileImage.setImageResource(user.profileImage)
            binding.root.setOnClickListener {
                onUserSelected?.invoke(user)
            }
        }
    }
}