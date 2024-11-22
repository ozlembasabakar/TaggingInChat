package com.example.tagginginchat.ui.viewBased

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.data.model.Message
import com.example.tagginginchat.databinding.ChatScreenBinding
import com.example.tagginginchat.ui.ChatScreenViewModel
import com.example.tagginginchat.ui.ChatScreenViewState
import com.example.tagginginchat.ui.jetpackCompose.theme.MentionedUserTextColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatScreenFragment : Fragment() {
    private var _binding: ChatScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var messagesRecyclerView: RecyclerView
    private val chatScreenViewModel: ChatScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ChatScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            chatScreenViewModel.state.collect { state ->
                sendMessage(state)
                setupTagLayout(state)
                setupMessageList(state)
                setupEditTextLayout(state)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendMessage(state: ChatScreenViewState) {
        binding.sendButton.setOnClickListener {
            if (state.message.isNotBlank()) {
                chatScreenViewModel.sendMessage(
                    Message(
                        isSent = true,
                        userId = 1,
                        content = state.message
                    )
                )
                if (state.mentionedUser.value.isNotBlank()) {
                    chatScreenViewModel.receivedMessage(
                        Message(
                            isSent = false,
                            userId = state.users
                                .filter { it.name == state.mentionedUser.value }
                                .first().id,
                            content = "Of course!"
                        )
                    )
                }
            }
            binding.editText.setText("")
        }
    }

    private fun setupTagLayout(state: ChatScreenViewState) {
        binding.editText.post {
            val editTextWidth = binding.editText.width
            val layoutParams = binding.tagLayout.root.layoutParams
            layoutParams.width = editTextWidth
            binding.tagLayout.root.layoutParams = layoutParams
        }
        tagRecyclerView = binding.tagLayout.root
        val itemHeight =
            resources.getDimensionPixelSize(R.dimen.chat_screen_user_list_item_size)
        val layoutParams = tagRecyclerView.layoutParams
        layoutParams.height = itemHeight * 5
        tagRecyclerView.layoutParams = layoutParams
        tagRecyclerView.layoutManager = LinearLayoutManager(context)
        tagRecyclerView.adapter =
            TagAdapter(
                users = state.filteredUsers,
                searchedText = state.message.substringAfterLast("@")
            ) { selectedUser ->
                chatScreenViewModel.onSelectedUser(selectedUser).apply {
                    val styledMessage = updateEditTextWithSelectedUser(
                        state.message,
                        selectedUser.name,
                        state.prevMentionedUsers
                    )
                    binding.editText.setText(styledMessage)
                    binding.editText.setSelection(styledMessage.length)
                }
        }
    }

    private fun setupEditTextLayout(state: ChatScreenViewState) {
        binding.editText.gravity = Gravity.CENTER_VERTICAL
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentText = s.toString()
                chatScreenViewModel.onMessageChanged(currentText)
            }

            override fun afterTextChanged(s: Editable?) {
                // Update UI based on current state
                if (state.showUserList) {
                    binding.tagLayout.root.visibility = View.VISIBLE
                    (binding.tagLayout.root.adapter as? TagAdapter)?.updateUsers(state.filteredUsers)
                    (binding.tagLayout.root.adapter as? TagAdapter)?.updateSearchedText(
                        state.message.substringAfterLast("@")
                    )
                    binding.editText.background =
                        resources.getDrawable(R.drawable.edit_text_corner_when_tag_layout_is_open)
                } else {
                    binding.tagLayout.root.visibility = View.GONE
                    binding.editText.background =
                        resources.getDrawable(R.drawable.edit_text_corner_when_tag_layout_is_closed)
                }
            }
        })
    }

    private fun updateEditTextWithSelectedUser(
        message: String,
        selectedUser: String,
        previousUsers: List<String>,
    ): SpannableString {
        val searchedText = message.substringAfterLast("@")
        val newMessage = message.replace(searchedText, "") + "$selectedUser "
        val spannableString = SpannableString(newMessage)

        val startIndex = newMessage.indexOf("@$selectedUser")
        val endIndex = startIndex + selectedUser.length + 1
        spannableString.setSpan(
            ForegroundColorSpan(MentionedUserTextColor.toArgb()),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        for (user in previousUsers) {
            val startIndexOfPrev = newMessage.indexOf("@$user")
            if (startIndexOfPrev != -1) {
                val endIndexOfPrev = startIndexOfPrev + user.length + 1
                spannableString.setSpan(
                    ForegroundColorSpan(MentionedUserTextColor.toArgb()),
                    startIndexOfPrev,
                    endIndexOfPrev,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannableString
    }

    private fun setupMessageList(state: ChatScreenViewState) {
        messagesRecyclerView = binding.messageList
        messagesRecyclerView.adapter =
            MessageAdapter(state.users, state.prevMentionedUsers, state.messageList)
        messagesRecyclerView.layoutManager = LinearLayoutManager(context)
        (binding.messageList.adapter as? MessageAdapter)?.updateMessages(state.messageList)
        (binding.messageList.adapter as? MessageAdapter)?.updatePrevMentionedUsers(state.prevMentionedUsers)
    }
}