package com.dldmswo1209.chuncheonconquest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.MapViewpagerItemBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot

class TourViewPager: ListAdapter<TourSpot, TourViewPager.ViewHolder>(differ) {

    inner class ViewHolder(val binding: MapViewpagerItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tour: TourSpot){
            binding.titleTextView.text = tour.title
            binding.addressTextView.text = tour.addr
            if(tour.img_url != "") {
                Glide.with(binding.root)
                    .load(tour.img_url)
                    .centerCrop()
                    .into(binding.thumbnailImageView)
            }else{
                Glide.with(binding.root)
                    .load(R.drawable.no_image)
                    .centerCrop()
                    .into(binding.thumbnailImageView)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MapViewpagerItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])

    }

    companion object{
        val differ = object: DiffUtil.ItemCallback<TourSpot>(){
            override fun areItemsTheSame(oldItem: TourSpot, newItem: TourSpot): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TourSpot, newItem: TourSpot): Boolean {
                return oldItem == newItem
            }
        }
    }

}