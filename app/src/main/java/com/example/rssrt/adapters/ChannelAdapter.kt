package com.example.rssrt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rssrt.model.database.Channel
import com.example.rssrt.databinding.ChannelItemBinding

class ChannelAdapter(private val onClickListener: ChannelOnClickListener) :
    ListAdapter<Channel, ChannelAdapter.ViewHolder>(ChannelDiffCallback()) {

    class ViewHolder private constructor(private val binding: ChannelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChannelItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Channel) {
            binding.channel = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { onClickListener.onClick(item) }
        holder.itemView.setOnLongClickListener { onClickListener.onLongClick(item) }
        holder.bind(item)
    }
}

class ChannelDiffCallback : DiffUtil.ItemCallback<Channel>() {
    override fun areItemsTheSame(oldItem: Channel, newItem: Channel) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Channel, newItem: Channel) = oldItem == newItem
}

class ChannelOnClickListener(
    val clickListener: (item: Channel) -> Unit,
    val longClickListener: (item: Channel) -> Boolean
) {
    fun onClick(item: Channel) = clickListener(item)
    fun onLongClick(item: Channel) = longClickListener(item)
}