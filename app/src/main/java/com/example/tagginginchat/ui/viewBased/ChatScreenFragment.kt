package com.example.tagginginchat.ui.viewBased

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagginginchat.R
import com.example.tagginginchat.data.DataSource
import com.example.tagginginchat.databinding.ChatScreenBinding

class ChatScreenFragment : Fragment() {
    private var _binding: ChatScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ChatScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView = binding.tagLayout.root
        val itemHeight = resources.getDimensionPixelSize(R.dimen.chat_screen_user_list_item_size)
        val layoutParams = recyclerView.layoutParams
        layoutParams.height = itemHeight * 5
        recyclerView.layoutParams = layoutParams
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TagAdapter(DataSource().users) { selectedUser ->
            Toast.makeText(context, "Clicked: ${selectedUser.name}", Toast.LENGTH_SHORT).show()
        }
    }
}