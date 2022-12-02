package com.dldmswo1209.chuncheonconquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.adapter.RankingAdapter
import com.dldmswo1209.chuncheonconquest.databinding.ActivityRankingBinding
import com.dldmswo1209.chuncheonconquest.model.UserData
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel

class RankingActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityRankingBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy{
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var ranking = mutableListOf<UserData>()
    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.getAllUser().observe(this){
            it.sortWith(compareBy { it.conquerCount })
            it.reverse()
            binding.firstNameTextView.text = it[0].information.name
            binding.secondNameTextView.text = it[1].information.name
            binding.thirdNameTextView.text = it[2].information.name
            binding.countTextView1.text = "${it[0].conquerCount} 개 정복"
            binding.countTextView2.text = "${it[1].conquerCount} 개 정복"
            binding.countTextView3.text = "${it[2].conquerCount} 개 정복"

            if(it[0].information.imageUri != null){
                Glide.with(this)
                    .load(it[0].information.imageUri)
                    .circleCrop()
                    .into(binding.profileImageView1)
            }else{
                binding.profileImageView1.setImageResource(R.drawable.user)
            }
            if(it[1].information.imageUri != null){
                Glide.with(this)
                    .load(it[1].information.imageUri)
                    .circleCrop()
                    .into(binding.profileImageView2)
            }else{
                binding.profileImageView2.setImageResource(R.drawable.user)
            }
            if(it[2].information.imageUri != null){
                Glide.with(this)
                    .load(it[2].information.imageUri)
                    .circleCrop()
                    .into(binding.profileImageView3)
            }else{
                binding.profileImageView3.setImageResource(R.drawable.user)
            }

            ranking.clear()
            for(i in 3 until it.size){
                ranking.add(it[i])
            }
            rankingAdapter = RankingAdapter()
            rankingAdapter.submitList(ranking)
            binding.rankRecyclerView.adapter = rankingAdapter


        }

    }
}