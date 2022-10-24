package com.dldmswo1209.chuncheonconquest.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.databinding.PostItemBinding
import com.dldmswo1209.chuncheonconquest.model.Post
import com.google.firebase.storage.FirebaseStorage

class PostListAdapter: ListAdapter<Post, PostListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding : PostItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            binding.titleTextView.text = post.title
            binding.dateTextView.text = post.date
            Glide.with(binding.root)
                .load(post.imageUri)
                .centerCrop()
                .into(binding.imageView)
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