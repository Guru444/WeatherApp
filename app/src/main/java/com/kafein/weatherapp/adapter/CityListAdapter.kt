package com.kafein.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kafein.weatherapp.R
import com.kafein.weatherapp.model.response.CityListResponse
import kotlinx.android.synthetic.main.city_list_item_layout.view.*
import kotlin.collections.ArrayList

class CityListAdapter : RecyclerView.Adapter<CityListAdapter.CityViewHolder>() {
    val items = arrayListOf<CityListResponse.CityListResponseItem>()
    var cityItemClickListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CityViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.city_list_item_layout, parent, false)
        )

    override fun getItemCount() = items.count()
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CityViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CityListResponse.CityListResponseItem) {
            item.let { response->
                itemView.tv_city_info.text = itemView.context.getString(R.string.city_info, response.localizedName, response.englishName, response.localizedType)
                itemView.setOnClickListener {
                    response.localizedName?.let {
                        cityItemClickListener(it)
                    }
                }
            }
        }
    }

    fun setItems(response: ArrayList<CityListResponse.CityListResponseItem>) {
        items.addAll(response)
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }
}