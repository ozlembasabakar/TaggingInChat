package com.example.tagginginchat.ui.viewBased

import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.data.model.User
import com.example.tagginginchat.databinding.MessageBoxBinding

@RequiresApi(Build.VERSION_CODES.O)
class MessageAdapter(
    private val users: List<User>,
    private val messages: List<Message>,
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageBoxBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    inner class MessageViewHolder(private val binding: MessageBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            val userInformation = users.find { it.id == message.userId }

            if (userInformation != null) {
                binding.content.text = message.content
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
    }
}