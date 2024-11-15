package com.example.tagginginchat.ui.viewBased

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.databinding.ChatScreenBinding
import com.example.tagginginchat.databinding.TagLayoutBinding
import com.example.tagginginchat.databinding.UserLayoutBinding

class ChatScreenFragment : Fragment() {
    private var _binding: ChatScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ChatScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class UserAdapter(
    private val context: Context,
    private var users: List<User>,
    private val onUserSelected: (User) -> Unit,
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var query: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(context, user, query)
        //forceLoadPicture(id, holder.avatar, holder.avatarIcon)
    }

    override fun getItemCount(): Int = users.size

    fun updateQuery(query: String) {
        this.query = query
        notifyDataSetChanged()
    }

    fun updateTags(newTags: List<String>) {
        //this.users.map { it.name } = newTags
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.userName)
        val avatar: ImageView = itemView.findViewById(R.id.profileImage)

        fun bind(context: Context, user: User, query: String) {
            val formattedTag = SpannableString(user.name)
            if (query.isNotBlank()) {
                val tagStartIndex = user.name.indexOf(query, ignoreCase = true)
                if (tagStartIndex != -1) {
                    val tagEndIndex = tagStartIndex + query.length
                    formattedTag.setSpan(
                        ForegroundColorSpan(ActivityCompat.getColor(context, R.color.white)),
                        tagStartIndex, tagEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    formattedTag.setSpan(
                        StyleSpan(Typeface.BOLD),
                        tagStartIndex, tagEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            name.text = formattedTag
            itemView.setOnClickListener { onUserSelected(user) }
        }
    }
}