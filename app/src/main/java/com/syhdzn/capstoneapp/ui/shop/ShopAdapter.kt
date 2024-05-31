package com.syhdzn.capstoneapp.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.api_access.api_response.ShopItem
import com.syhdzn.capstoneapp.databinding.ShopItemBinding

class ShopAdapter(
    private var shopList: List<ShopItem>,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(shopItem: ShopItem)
    }

    inner class ShopViewHolder(private val binding: ShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickListener.onItemClick(shopList[adapterPosition])
            }
        }

        fun bind(shopItem: ShopItem) {
            with(binding) {
                // Display shop details
                Glide.with(itemView.context)
                    .load(shopItem.image)
                    .into(ivItemStore)

                tvNameStore.text = shopItem.name
                tvLocationStore.text = shopItem.location
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(shopList[position])
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    fun updateData(newItemList: List<ShopItem>) {
        shopList = newItemList
        notifyDataSetChanged()
    }
}

