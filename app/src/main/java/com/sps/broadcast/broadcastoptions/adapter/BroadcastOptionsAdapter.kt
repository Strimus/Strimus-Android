package com.sps.broadcast.broadcastoptions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.machinarium.sbs.model.init.ConfigItem
import com.sps.databinding.ItemBroadcastOptionBinding

class BroadcastOptionsAdapter(val onSelectionListener: (ConfigItem) -> Unit) :
    ListAdapter<ConfigItem, BroadcastOptionsAdapter.BroadcastOptionViewHolder>(
        BroadcastOptionDiffCallback
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BroadcastOptionViewHolder {
        return BroadcastOptionViewHolder(
            ItemBroadcastOptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BroadcastOptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BroadcastOptionViewHolder(val binding: ItemBroadcastOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(configItem: ConfigItem) {
            binding.tvSource.text = configItem.label

            binding.root.setOnClickListener {
                onSelectionListener.invoke(configItem)
            }
        }
    }

    object BroadcastOptionDiffCallback : DiffUtil.ItemCallback<ConfigItem>() {
        override fun areItemsTheSame(oldItem: ConfigItem, newItem: ConfigItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ConfigItem, newItem: ConfigItem): Boolean {
            return oldItem == newItem
        }
    }
}