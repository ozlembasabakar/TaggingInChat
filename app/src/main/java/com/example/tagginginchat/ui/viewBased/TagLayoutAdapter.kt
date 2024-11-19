package com.example.tagginginchat.ui.viewBased

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.User

class TagAdapter(
    private val users: List<User>,
    private val onUserSelected: ((User) -> Unit)? = null,
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_layout, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        private val userName: TextView = itemView.findViewById(R.id.userName)

        fun bind(user: User) {
            userName.text = user.name
            profileImage.setImageResource(user.profileImage)
            itemView.setOnClickListener {
                onUserSelected?.invoke(user)
            }
        }
    }
}