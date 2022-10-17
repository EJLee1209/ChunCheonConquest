package com.dldmswo1209.chuncheonconquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val storage = Firebase.storage.reference

        storage.child("images/namisum.jpeg").downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.testImageView)
        }
    }
}