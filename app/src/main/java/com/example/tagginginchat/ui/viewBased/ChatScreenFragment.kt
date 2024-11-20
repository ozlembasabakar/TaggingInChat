package com.example.tagginginchat.ui.viewBased

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.databinding.ChatScreenBinding
import com.example.tagginginchat.ui.ChatScreenViewModel
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
        setupTagLayout()
        setupMessageList()
        setupEditTextLayout()
    }

    private fun setupTagLayout() {
        binding.editText.post {
            val editTextWidth = binding.editText.width
            val layoutParams = binding.tagLayout.root.layoutParams
            layoutParams.width = editTextWidth
            binding.tagLayout.root.layoutParams = layoutParams
        }
        viewLifecycleOwner.lifecycleScope.launch {
            chatScreenViewModel.state.collect { state ->
                tagRecyclerView = binding.tagLayout.root
                val itemHeight =
                    resources.getDimensionPixelSize(R.dimen.chat_screen_user_list_item_size)
                val layoutParams = tagRecyclerView.layoutParams
                layoutParams.height = itemHeight * 5
                tagRecyclerView.layoutParams = layoutParams
                tagRecyclerView.layoutManager = LinearLayoutManager(context)
                tagRecyclerView.adapter = TagAdapter(state.users) { selectedUser ->
                    Toast.makeText(context, "Clicked: ${selectedUser.name}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setupEditTextLayout() {
        binding.editText.gravity = Gravity.CENTER_VERTICAL

        viewLifecycleOwner.lifecycleScope.launch {
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val currentText = s.toString()
                    chatScreenViewModel.chatScreenInputModel.message.value = currentText

                    chatScreenViewModel.chatScreenInputModel.showUserList.value =
                        currentText.lastOrNull() == '@'

                    binding.tagLayout.root.visibility = if (chatScreenViewModel.chatScreenInputModel.showUserList.value) View.VISIBLE else View.GONE

                    if (chatScreenViewModel.chatScreenInputModel.showUserList.value) {
                        binding.editText.background =
                            resources.getDrawable(R.drawable.edit_text_corner_when_tag_layout_is_open)
                    } else {
                        binding.editText.background =
                            resources.getDrawable(R.drawable.edit_text_corner_when_tag_layout_is_closed)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    private fun setupMessageList() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatScreenViewModel.state.collect { state ->
                messagesRecyclerView = binding.messageList
                messagesRecyclerView.adapter = MessageAdapter(state.users, state.messageList)
                messagesRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        }
    }
}