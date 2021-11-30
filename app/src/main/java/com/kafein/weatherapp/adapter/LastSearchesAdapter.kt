package com.kafein.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kafein.weatherapp.R
import com.kafein.weatherapp.model.LastSearches
import kotlinx.android.synthetic.main.last_searches_item_layout.view.*

class LastSearchesAdapter(): RecyclerView.Adapter<LastSearchesAdapter.ItemViewHolder>() {
    var items: ArrayList<LastSearches> = arrayListOf()
    var lastSearchItemClickListener: (String) -> Unit = {}


    fun initializeValues(items: ArrayList<LastSearches>){
        this.items = items
        this.items.reverse()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.last_searches_item_layout, parent, false)
        )

    override fun onBindViewHolder(holder: LastSearchesAdapter.ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind (itemModel: LastSearches){
            itemView.item_title.text = itemModel.lastSearchTitle
            itemView.setOnClickListener {
                lastSearchItemClickListener(itemModel.lastSearchTitle)
            }
        }
    }
}