package com.dldmswo1209.chuncheonconquest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.databinding.ActivityMainBinding
import com.dldmswo1209.chuncheonconquest.fragment.HomeFragment
import com.dldmswo1209.chuncheonconquest.fragment.MapFragment
import com.dldmswo1209.chuncheonconquest.fragment.MyPageFragment
import com.dldmswo1209.chuncheonconquest.fragment.PostFragment
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.User
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
    private lateinit var userInfo : User
    private lateinit var cafeList : ArrayList<TourSpot>
    private lateinit var restaurantList : ArrayList<TourSpot>
    private lateinit var tourList : ArrayList<TourSpot>
    private lateinit var postList: MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getDataFromIntent()
        initView()
    }
    private fun getDataFromIntent(){
        userInfo = intent.getSerializableExtra("user") as User
        cafeList = intent.getSerializableExtra("cafeList") as ArrayList<TourSpot>
        restaurantList = intent.getSerializableExtra("restaurantList") as ArrayList<TourSpot>
        tourList = intent.getSerializableExtra("tourList") as ArrayList<TourSpot>
        postList = intent.getSerializableExtra("postList") as MutableList<Post>
    }

    private fun initView(){
        val home = HomeFragment()
        val map = MapFragment()
        val post = PostFragment()
        val myPage = MyPageFragment()
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
                R.id.myPage -> {
                    replaceFragment(myPage)
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

    fun getUserInfo() : User{
        return userInfo
    }
    fun getCafeList() : ArrayList<TourSpot>{
        return cafeList
    }
    fun getRestaurantList() : ArrayList<TourSpot>{
        return restaurantList
    }
    fun getTourList() : ArrayList<TourSpot>{
        return tourList
    }
    fun getPostList() : MutableList<Post>{
        return postList
    }
}