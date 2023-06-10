package com.sps

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.machinarium.sbs.model.stream.StreamItem
import com.sps.databinding.ListItemTitleBinding
import com.sps.databinding.ListItemVideoBinding
import com.squareup.picasso.Picasso

class ListAdapter(val onClick: (url: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: MutableList<Item> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_TITLE) {
            Title(
                ListItemTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            Holder(
                ListItemVideoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return if (list[position].title.isNullOrEmpty()) {
            TYPE_ITEM
        } else {
            TYPE_TITLE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.bind(list[position])
        } else if (holder is Title) {
            holder.bind(list[position])
        }
    }

    inner class Holder internal constructor(private val binding: ListItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.data?.apply {
                    onClick.invoke(id)
                }
            }
        }


        fun bind(data: Item) {
            data.item?.let {
                binding.data = it
                Picasso.get().load(it.streamData.image).into(binding.image)
            }

        }
    }

    inner class Title internal constructor(private val binding: ListItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: Item) {
            data.title?.let {
                binding.tvTitle.text = it
            }
        }
    }


    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_ITEM = 1
    }

    class Item(val title: String? = null, val item: StreamItem? = null)
}