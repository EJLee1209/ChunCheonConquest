package com.dldmswo1209.chuncheonconquest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.FragmentHomeBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        // 스토리지에서 이미지 가져오기
//        val storage = Firebase.storage.reference
//        CoroutineScope(Dispatchers.IO).launch {
//            storage.child("images/namisum.jpeg").downloadUrl.addOnSuccessListener {
//                Glide.with(this@HomeFragment)
//                    .load(it)
//                    .centerCrop()
//                    .into(binding.testImageView)
//            }
//        }

    }
}