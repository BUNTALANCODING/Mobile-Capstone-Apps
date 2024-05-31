package com.syhdzn.capstoneapp.ui.detail_shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.syhdzn.capstoneapp.api_access.api_response.FoodDetail
import com.syhdzn.capstoneapp.databinding.DetailItemBinding

class DetailShopAdapter(private val foodList: List<FoodDetail>) :
    RecyclerView.Adapter<DetailShopAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(private val binding: DetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: FoodDetail) {
            with(binding) {
                tvNamefoodDetail.text = foodItem.name
                tvPriceDetail.text = "Rp ${foodItem.price}0"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding =
            DetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}

