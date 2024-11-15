package com.example.tagginginchat.ui.viewBased

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.tagginginchat.R
import com.example.tagginginchat.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewBasedMainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChatScreenFragment())
                .commit()
        }
    }
}