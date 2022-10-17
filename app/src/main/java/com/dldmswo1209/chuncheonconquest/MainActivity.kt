package com.dldmswo1209.chuncheonconquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dldmswo1209.chuncheonconquest.databinding.ActivityMainBinding
import com.dldmswo1209.chuncheonconquest.fragment.HomeFragment
import com.dldmswo1209.chuncheonconquest.fragment.MapFragment
import com.dldmswo1209.chuncheonconquest.fragment.PostFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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