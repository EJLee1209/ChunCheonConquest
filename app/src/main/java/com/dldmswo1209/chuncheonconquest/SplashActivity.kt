package com.dldmswo1209.chuncheonconquest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dldmswo1209.chuncheonconquest.databinding.ActivitySplashBinding
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("uid", "")

        CoroutineScope(Dispatchers.Default).launch {
            async {
                delay(3000)
            }.await()

            if(uid == ""){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
        }


    }
}