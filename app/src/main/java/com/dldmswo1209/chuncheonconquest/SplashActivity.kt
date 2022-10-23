package com.dldmswo1209.chuncheonconquest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.chuncheonconquest.databinding.ActivitySplashBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.User
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var user: User
    private val tourList = arrayListOf<TourSpot>()
    private val cafeList = arrayListOf<TourSpot>()
    private val restaurantList = arrayListOf<TourSpot>()
    private var loadCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")
        if(uid != ""){
            // 로그인 한적이 있다면
            // 데이터를 가져옴
            viewModel.getTourList()
            viewModel.getCafeList()
            viewModel.getRestaurantList()
            viewModel.getUserInfo()
        }
        viewModel.user.observe(this, Observer {
            user = it
            loadCount++
        })

        viewModel.cafeList.observe(this, Observer {
             it.forEach { spot ->
                 cafeList.add(spot)
             }
            loadCount++
        })

        viewModel.tourList.observe(this, Observer {
            it.forEach { spot ->
                tourList.add(spot)
            }
            loadCount++
        })

        viewModel.restaurantList.observe(this, Observer {
            it.forEach { spot ->
                restaurantList.add(spot)
            }
            loadCount++
        })

        CoroutineScope(Dispatchers.Default).launch {
            async {
                delay(2000) // 데이터가 로드될때까지 최소 2초는 기다림
            }.await()

            if(uid == ""){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }else{
                if(loadCount == 4){ // 모든 데이터가 로드 됐으면 메인 액티비티로 이동
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("user", user)
                    intent.putExtra("cafeList", cafeList)
                    intent.putExtra("tourList", tourList)
                    intent.putExtra("restaurantList", restaurantList)
                    startActivity(intent)
                }
            }
        }


    }
}