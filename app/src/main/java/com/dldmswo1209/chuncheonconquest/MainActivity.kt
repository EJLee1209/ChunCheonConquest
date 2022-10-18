package com.dldmswo1209.chuncheonconquest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.databinding.ActivityMainBinding
import com.dldmswo1209.chuncheonconquest.fragment.HomeFragment
import com.dldmswo1209.chuncheonconquest.fragment.MapFragment
import com.dldmswo1209.chuncheonconquest.fragment.PostFragment
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        uid = sharedPreferences.getString("uid", "").toString()

        initView()
    }

    fun initView(){
        val home = HomeFragment()
        val map = MapFragment()
        val post = PostFragment()
        replaceFragment(home)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(home)
                }
                R.id.post -> {
                    replaceFragment(post)
                }
                R.id.map -> {
                    replaceFragment(map)
                }
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerLayout, fragment)
            .commit()
    }
}