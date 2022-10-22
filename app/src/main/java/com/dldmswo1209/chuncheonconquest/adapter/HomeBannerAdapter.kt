package com.dldmswo1209.chuncheonconquest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.BannerItemBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot

class HomeBannerAdapter(val itemClicked : (TourSpot)->(Unit)): ListAdapter<TourSpot, HomeBannerAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(val binding: BannerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(tour: TourSpot){

            if(tour.img_url == ""){
                binding.imageView.setImageResource(R.drawable.no_image)
            }else {
                Glide.with(binding.root)
                    .load(tour.img_url)
                    .centerCrop()
                    .into(binding.imageView)
            }
            binding.textView.text = tour.title

            binding.root.setOnClickListener {
                itemClicked(tour)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BannerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position%currentList.size])
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
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