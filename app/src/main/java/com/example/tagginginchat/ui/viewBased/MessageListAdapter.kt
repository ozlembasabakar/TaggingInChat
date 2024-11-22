package com.example.tagginginchat.ui.viewBased

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.databinding.MessageBoxBinding
import com.example.tagginginchat.ui.jetpackCompose.theme.MentionedUserTextColor

@RequiresApi(Build.VERSION_CODES.O)
class MessageAdapter(
    private val users: List<User>,
    private val prevMentionedUsers: List<String>,
    private val messages: List<Message>,
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messageList = messages
    private var prevMentionedUsersList = prevMentionedUsers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageBoxBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePrevMentionedUsers(newPrevMentionedUsers: List<String>) {
        prevMentionedUsersList = newPrevMentionedUsers
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMessages(newMessages: List<Message>) {
        messageList = newMessages
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(private val binding: MessageBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            val userInformation = users.find { it.id == message.userId }

            if (userInformation != null) {
                binding.content.text = updateMessageContentWithSelectedUser(message.content, prevMentionedUsersList)
                binding.userName.text = userInformation.name
                binding.userName.setTextColor(
                    Color.valueOf(userInformation.color.value.toLong()).toArgb()
                )
                binding.profileImage.setImageResource(userInformation.profileImage)
            }
            if (message.isSent) {
                binding.messageContainer.gravity = Gravity.END
                binding.messageContent.background = ContextCompat.getDrawable(binding.messageContent.context, R.drawable.sent_message_box_shape)

            } else {
                binding.messageContainer.gravity = Gravity.START
                binding.messageContent.background = ContextCompat.getDrawable(binding.messageContent.context, R.drawable.received_message_box_shape)
            }
        }

        private fun updateMessageContentWithSelectedUser(
            message: String,
            previousUsers: List<String>
        ): SpannableString {
            val spannableString = SpannableString(message)

            for (user in previousUsers) {
                val startIndex = message.indexOf("@$user")
                if (startIndex != -1) {
                    val endIndex = startIndex + user.length + 1
                    spannableString.setSpan(
                        ForegroundColorSpan(MentionedUserTextColor.toArgb()),
                        startIndex,
                        endIndex,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            return spannableString
        }
    }
}