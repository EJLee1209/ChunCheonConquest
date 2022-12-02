package com.dldmswo1209.chuncheonconquest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.databinding.ActivitySplashBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var userInfo: UserInfo
    private var tourList = arrayListOf<TourSpot>()
    private var cafeList = arrayListOf<TourSpot>()
    private var restaurantList = arrayListOf<TourSpot>()
    private var loadCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")
        if(uid != ""){
            // 로그인 한적이 있다면
            // 데이터를 가져옴
            // 데이터 로딩을 스플래시 화면에서 함으로써 데이터 로드 지연시간을 없앤다.
            viewModel.getUserInfo().observe(this){
                userInfo = it
                loadCount++
            }

            viewModel.getCafeList().observe(this){
                cafeList = it as ArrayList<TourSpot>
                loadCount++
            }

            viewModel.getTourList().observe(this){
                tourList = it as ArrayList<TourSpot>
                loadCount++
            }


            viewModel.getRestaurantList().observe(this){
                restaurantList = it as ArrayList<TourSpot>
                loadCount++
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            if(uid == ""){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }else{
                while(loadCount != 4){
                    // 모든 데이터가 로드될 때까지 기다림
                }
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.putExtra("user", userInfo)
                intent.putExtra("cafeList", cafeList)
                intent.putExtra("tourList", tourList)
                intent.putExtra("restaurantList", restaurantList)
                startActivity(intent)
            }
        }
    }
}