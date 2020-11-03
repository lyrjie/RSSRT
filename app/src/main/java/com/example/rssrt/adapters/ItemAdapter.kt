package com.example.rssrt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rssrt.model.database.Item
import com.example.rssrt.databinding.ItemItemBinding

class ItemAdapter(private val onClickListener: ItemOnClickListener) :
    ListAdapter<Item, ItemAdapter.ViewHolder>(ItemDiffCallback()) {

    class ViewHolder private constructor(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Item) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(item) }
        holder.bind(item)
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}

class ItemOnClickListener(
    val clickListener: (item: Item) -> Unit,
) {
    fun onClick(item: Item) = clickListener(item)
}