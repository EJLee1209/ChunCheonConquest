package com.dldmswo1209.chuncheonconquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.adapter.FriendListAdapter
import com.dldmswo1209.chuncheonconquest.databinding.ActivityFriendBinding
import com.dldmswo1209.chuncheonconquest.fragment.AddFriendBottomFragment
import com.dldmswo1209.chuncheonconquest.model.UserData
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import kotlinx.coroutines.*

class FriendActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityFriendBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var friendList = mutableListOf<UserInfo>()

    private lateinit var friendListAdapter: FriendListAdapter
    var allUser = mutableListOf<UserData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.addFriendButton.setOnClickListener{
            val bottomSheet = AddFriendBottomFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        viewModel.getAllUser().observe(this) {
            allUser = it
        }

        viewModel.getMyFriends().observe(this){
            friendListAdapter = FriendListAdapter()
            friendListAdapter.submitList(it)
            binding.recyclerView.adapter = friendListAdapter

            friendList.clear()
            friendList = it

        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 누름
                val searchList = mutableListOf<UserInfo>()
                friendList.forEach { user->
                    if(user.name == query){
                        searchList.add(user)
                    }
                }
                friendListAdapter.submitList(searchList)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == ""){
                    friendListAdapter.submitList(friendList)
                }else{
                    val searchList = mutableListOf<UserInfo>()
                    friendList.forEach { user->
                        if(user.name.contains(newText.toString())){
                            searchList.add(user)
                        }
                    }
                    friendListAdapter.submitList(searchList)
                }
                return true
            }

        })
    }

    override fun onPause() {
        super.onPause()
        Log.d("testt", "onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("testt", "onResume: ")
    }
}