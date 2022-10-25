package com.dldmswo1209.chuncheonconquest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.PostItemBinding
import com.dldmswo1209.chuncheonconquest.model.Post
import com.google.firebase.storage.FirebaseStorage

class PostListAdapter: ListAdapter<Post, PostListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding : PostItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            binding.titleTextView.text = post.title
            binding.dateTextView.text = post.date
            binding.contentTextView.text = post.content
            binding.nameTextView.text = post.user.name

            if(post.imageUri != ""){
                binding.imageView.visibility = View.VISIBLE
                Glide.with(binding.root)
                    .load(post.imageUri)
                    .centerCrop()
                    .into(binding.imageView)
            }else{
                binding.imageView.visibility = View.GONE
            }

            if(post.user.imageUri == null){
                binding.profileImageView.setImageResource(R.drawable.user)
            }else {
                Glide.with(binding.root)
                    .load(post.user.imageUri)
                    .circleCrop()
                    .into(binding.profileImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<Post>(){
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }
}