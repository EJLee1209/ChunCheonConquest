package com.dldmswo1209.chuncheonconquest

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.MainActivity.Constants.CHANNEL_ID
import com.dldmswo1209.chuncheonconquest.MainActivity.Constants.CHANNEL_NAME
import com.dldmswo1209.chuncheonconquest.databinding.ActivityMainBinding
import com.dldmswo1209.chuncheonconquest.fragment.HomeFragment
import com.dldmswo1209.chuncheonconquest.fragment.MapFragment
import com.dldmswo1209.chuncheonconquest.fragment.MyPageFragment
import com.dldmswo1209.chuncheonconquest.fragment.PostFragment
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private lateinit var userInfo : UserInfo
    private lateinit var cafeList : ArrayList<TourSpot>
    private lateinit var restaurantList : ArrayList<TourSpot>
    private lateinit var tourList : ArrayList<TourSpot>
    val post = PostFragment()

    object Constants {
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        getDataFromIntent()
        initView()
        mainViewModel.getUserInfo().observe(this){
            userInfo = it
        }

        binding.bottomNavigationView.selectedItemId

        binding.menuButton.setOnClickListener{
            binding.drawer.openDrawer(GravityCompat.END)
        }
        binding.navigationDrawer.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.rank ->{
                    // ????????????
                    startActivity(Intent(this, RankingActivity::class.java))
                }
                R.id.friends ->{
                    // ????????????
                    startActivity(Intent(this, FriendActivity::class.java))
                }
                R.id.logout ->{
                    // ????????????
                    getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                        .putString("uid", "")
                        .apply()
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            true
        }

    }
    private fun getDataFromIntent(){
        userInfo = intent.getSerializableExtra("user") as UserInfo
        cafeList = intent.getSerializableExtra("cafeList") as ArrayList<TourSpot>
        restaurantList = intent.getSerializableExtra("restaurantList") as ArrayList<TourSpot>
        tourList = intent.getSerializableExtra("tourList") as ArrayList<TourSpot>
    }

    private fun initView(){
        val home = HomeFragment()
        val map = MapFragment()
        val myPage = MyPageFragment()
        replaceFragment(home)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(home)
                    binding.titleTextView.text = "?????? ??????"
                    binding.toolbar.visibility = View.VISIBLE
                }
                R.id.post -> {
                    replaceFragment(post)
                    binding.titleTextView.text = "${userInfo.name}??? ??? ??????"
                    binding.toolbar.visibility = View.VISIBLE
                }
                R.id.map -> {
                    replaceFragment(map)
                    binding.toolbar.visibility = View.GONE
                    binding.titleTextView.text = "??????"
                }
                R.id.myPage -> {
                    replaceFragment(myPage)
                    binding.titleTextView.text = "??? ?????????"
                    binding.toolbar.visibility = View.VISIBLE
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

    fun getUserInfo() : UserInfo{
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
    fun getTotalCount() : Int{
        return cafeList.size + restaurantList.size + tourList.size
    }

    fun showLottie(){
        post.showLottie()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH).apply {
            description = "Channel Description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun sendNotification(tour: TourSpot) {
        val title = "?????? ??????"
        val message = "${tour.title}???(???) ??????????????????!"

        mainViewModel.conquerCountUp(userInfo, tour)

        val intent = Intent(this, SplashActivity::class.java)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val pendingIntent = getActivity(this, notificationID, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setWhen(System.currentTimeMillis()) //?????? ??????
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationManager.notify(notificationID, notification)
    }



}