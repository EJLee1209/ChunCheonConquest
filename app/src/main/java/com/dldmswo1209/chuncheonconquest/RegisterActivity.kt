package com.dldmswo1209.chuncheonconquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dldmswo1209.chuncheonconquest.databinding.ActivityRegisterBinding
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val auth by lazy{
        FirebaseAuth.getInstance()
    }
    private val db by lazy{
        Firebase.database.reference.child("Users")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pw = binding.pwEditText.text.toString()
            val pw2 = binding.pwConfirmEditText.text.toString()

            if(name == "" || email == "" || pw == "" || pw2 == ""){
                toastMsg("모든 정보를 입력해주세요")
                return@setOnClickListener
            }
            if(pw != pw2){
                toastMsg("비밀번호가 일치하지 않습니다")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pw)
                .addOnSuccessListener {
                    toastMsg("회원가입에 성공했습니다")
                    val uid = it.user?.uid.toString()
                    val newUserInfo = UserInfo(uid, email, pw, name)

                    db.child(uid).child("information").setValue(newUserInfo)

                    finish()
                }
                .addOnCanceledListener {
                    toastMsg("회원가입에 실패했습니다.")
                }
        }
    }
    fun toastMsg(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

