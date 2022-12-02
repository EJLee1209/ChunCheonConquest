package com.dldmswo1209.chuncheonconquest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.FriendItemBinding
import com.dldmswo1209.chuncheonconquest.model.UserData

class RankingAdapter: ListAdapter<UserData, RankingAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserData){
            binding.nameTextView.text = user.information.name
            binding.countTextView.text = "${user.conquerCount} 개 정복"
            if(user.information.imageUri != null){
                Glide.with(binding.root)
                    .load(user.information.imageUri)
                    .circleCrop()
                    .into(binding.profileImageView)
            }else{
                binding.profileImageView.setImageResource(R.drawable.user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<UserData>(){
            override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
                return oldItem.information.uid == newItem.information.uid
            }

            override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
                return oldItem == newItem
            }

        }
    }

}