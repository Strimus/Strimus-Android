package com.sps.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.machinarium.sbs.model.stream.StreamItem
import com.sps.R
import com.sps.databinding.ListItemTitleBinding
import com.sps.databinding.ListItemVideoBinding
import com.squareup.picasso.Picasso

class StreamListAdapter(val onStreamClicked: (stream: StreamItem) -> Unit) :
    ListAdapter<StreamListAdapter.ListItem, RecyclerView.ViewHolder>(ListItemDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.list_item_title -> TitleViewHolder(
                ListItemTitleBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            R.layout.list_item_video -> StreamViewHolder(
                ListItemVideoBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (holder is StreamViewHolder) {
            (currentItem as? ListItem.Item)?.let {
                holder.bind(it)
            }
        } else if (holder is TitleViewHolder) {
            (currentItem as? ListItem.Title)?.let {
                holder.bind(it)
            }
        }
    }

    inner class StreamViewHolder internal constructor(private val binding: ListItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.data?.apply {
                    onStreamClicked.invoke(this)
                }
            }
        }


        fun bind(data: ListItem.Item) {
            val streamItem = data.item
            binding.data = streamItem
            Picasso.get().load(streamItem.streamData.image).into(binding.image)
            binding.executePendingBindings()
        }
    }

    inner class TitleViewHolder internal constructor(private val binding: ListItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: ListItem.Title) {
            binding.tvTitle.text = data.title
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.Title -> R.layout.list_item_title
            is ListItem.Item -> R.layout.list_item_video
        }
    }

    object ListItemDiffUtil : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            if (oldItem is ListItem.Title && newItem is ListItem.Title) {
                return oldItem.title == newItem.title
            } else if (oldItem is ListItem.Item && newItem is ListItem.Item) {
                return oldItem.item.id == newItem.item.id
            }
            return false
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            if (oldItem is ListItem.Title && newItem is ListItem.Title) {
                return oldItem.title == newItem.title
            } else if (oldItem is ListItem.Item && newItem is ListItem.Item) {
                return oldItem.item == newItem.item
            }
            return false
        }

    }

    sealed class ListItem {

        data class Title(val title: String) : ListItem()

        data class Item(val item: StreamItem) : ListItem()
    }
}