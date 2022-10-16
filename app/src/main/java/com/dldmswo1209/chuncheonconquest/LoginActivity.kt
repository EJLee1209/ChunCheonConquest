package com.dldmswo1209.chuncheonconquest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.dldmswo1209.chuncheonconquest.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    val auth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        binding.registerLayout.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pw = binding.pwEditText.text.toString()
            if(email == "" || pw == ""){
                toastMsg("모든 정보를 입력해주세요")
                return@setOnClickListener
            }
            // 로그인

            auth.signInWithEmailAndPassword(email, pw)
                .addOnSuccessListener {
                    val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("uid", it.user?.uid)
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    toastMsg("아이디 또는 비밀번호를 확인해주세요.")
                }


        }

    }
    fun toastMsg(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

