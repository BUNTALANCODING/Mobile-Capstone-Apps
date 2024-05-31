package com.syhdzn.capstoneapp.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.api_access.api_response.Datahistori
import com.syhdzn.capstoneapp.databinding.HistoryItemBinding

class HistoryAdapter(
    private var historyList: List<Datahistori>,
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(historiItem: Datahistori)
    }

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                clickListener.onItemClick(historyList[adapterPosition])
            }
        }

        fun bind(historiItem: Datahistori) {
            with(binding) {
                tvHistoryName.text = historiItem.name
                Glide.with(itemView.context)
                    .load(historiItem.image)
                    .into(ivHistori)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun updateData(newShopList: List<Datahistori>) {
        historyList = newShopList
        notifyDataSetChanged()
    }
}
