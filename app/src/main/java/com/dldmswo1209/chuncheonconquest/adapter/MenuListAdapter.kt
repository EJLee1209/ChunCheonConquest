package com.dldmswo1209.chuncheonconquest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dldmswo1209.chuncheonconquest.databinding.MenuItemBinding
import com.dldmswo1209.chuncheonconquest.model.Menu

class MenuListAdapter: ListAdapter<Menu, MenuListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Menu){
            binding.menuTextView.text = item.menu
            binding.priceTextView.text = item.price
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object: DiffUtil.ItemCallback<Menu>(){
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.menu == newItem.menu
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem == newItem
            }

        }
    }

}