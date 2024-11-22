package com.example.tagginginchat.ui.viewBased

import android.graphics.Color.WHITE
import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.databinding.UserLayoutBinding

class TagAdapter(
    private val users: List<User>,
    private var searchedText: String,
    private val onUserSelected: ((User) -> Unit)? = null,
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private val userList = mutableListOf<User>().apply { addAll(users) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    fun updateUsers(newUsers: List<User>) {
        userList.clear()
        userList.addAll(newUsers)
        notifyDataSetChanged()
    }

    fun updateSearchedText(newSearchedText: String) {
        searchedText = newSearchedText
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    inner class TagViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userName.text = getStyledText(user.name, searchedText)
            binding.profileImage.setImageResource(user.profileImage)
            binding.root.setOnClickListener {
                onUserSelected?.invoke(user)
            }
        }
    }

    private fun getStyledText(fullName: String, searchText: String): SpannableString {
        val spannableString = SpannableString(fullName)
        val startIndex = fullName.indexOf(searchText, ignoreCase = true)
        if (startIndex != -1 && searchText.isNotEmpty()) {
            val endIndex = startIndex + searchText.length
            spannableString.setSpan(
                ForegroundColorSpan(WHITE),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                StyleSpan(BOLD),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }
}