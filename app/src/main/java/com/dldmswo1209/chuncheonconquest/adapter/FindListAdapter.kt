package com.dldmswo1209.chuncheonconquest.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dldmswo1209.chuncheonconquest.databinding.FindItemBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot

class FindListAdapter(val onClick: (TourSpot)->(Unit)): ListAdapter<TourSpot, FindListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(val binding: FindItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(tour: TourSpot){
            binding.titleTextView.text = tour.title
            binding.root.setOnClickListener {
                onClick(tour)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FindItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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