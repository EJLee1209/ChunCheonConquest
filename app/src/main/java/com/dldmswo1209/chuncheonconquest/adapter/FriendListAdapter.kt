package com.dldmswo1209.chuncheonconquest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.FriendItemBinding
import com.dldmswo1209.chuncheonconquest.model.UserInfo

class FriendListAdapter: ListAdapter<UserInfo, FriendListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserInfo){
            binding.nameTextView.text = user.name
            if(user.imageUri != null){
                Glide.with(binding.root)
                    .load(user.imageUri)
                    .circleCrop()
                    .into(binding.profileImageView)
            }else{
                Glide.with(binding.root)
                    .load(R.drawable.user)
                    .circleCrop()
                    .into(binding.profileImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<UserInfo>(){
            override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
                return oldItem.email == newItem.email
            }

        }
    }
}