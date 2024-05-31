package com.syhdzn.capstoneapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.api_access.api_response.FoodsItem
import com.syhdzn.capstoneapp.databinding.HomeItemBinding

class FoodAdapter(
    private var foodList: List<FoodsItem>,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(foodItem: FoodsItem)
    }

    inner class FoodViewHolder(private val binding: HomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickListener.onItemClick(foodList[adapterPosition])
            }
        }

        fun bind(foodItem: FoodsItem) {
            binding.apply {
                tvNameHome.text = foodItem.name
                 Glide.with(itemView.context)
                    .load(foodItem.image)
                    .into(ivItemHome)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun setData(newfoodList: List<FoodsItem>) {
        foodList = newfoodList
        notifyDataSetChanged()
    }
}
