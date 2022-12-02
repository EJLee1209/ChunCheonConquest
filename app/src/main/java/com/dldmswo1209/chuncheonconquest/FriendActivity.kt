package com.dldmswo1209.chuncheonconquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.adapter.FriendListAdapter
import com.dldmswo1209.chuncheonconquest.databinding.ActivityFriendBinding
import com.dldmswo1209.chuncheonconquest.fragment.AddFriendBottomFragment
import com.dldmswo1209.chuncheonconquest.model.UserData
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel

class FriendActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityFriendBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var friendListAdapter: FriendListAdapter
    var allUser = mutableListOf<UserData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        friendListAdapter = FriendListAdapter()
        binding.recyclerView.adapter = friendListAdapter

        binding.addFriendButton.setOnClickListener{
            val bottomSheet = AddFriendBottomFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        viewModel.getAllUser().observe(this) {
            allUser = it
        }

        viewModel.getMyFriends().observe(this){
            friendListAdapter.submitList(it)
        }
    }

}