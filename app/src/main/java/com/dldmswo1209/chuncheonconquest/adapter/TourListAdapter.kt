package com.dldmswo1209.chuncheonconquest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.TourItemBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot

class TourListAdapter(val itemClicked : (TourSpot) -> (Unit)): ListAdapter<TourSpot, TourListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: TourItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: TourSpot){
            if(item.img_url == ""){
                binding.tourImageView.setImageResource(R.drawable.no_image)
            }else {
                Glide.with(binding.root)
                    .load(item.img_url)
                    .centerCrop()
                    .into(binding.tourImageView)
            }
            binding.nameTextView.text = item.title
            binding.tourImageView.clipToOutline = true

            binding.root.setOnClickListener {
                itemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<TourSpot>(){
            override fun areItemsTheSame(oldItem: TourSpot, newItem: TourSpot): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TourSpot, newItem: TourSpot): Boolean {
                return oldItem == newItem
            }

        }
    }
}