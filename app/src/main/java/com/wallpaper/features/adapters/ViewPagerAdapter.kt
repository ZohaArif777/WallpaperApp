package com.wallpaper.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wallpaper.R

class ViewPagerAdapter(private val items: List<ViewPagerItem>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val textViewDesc: TextView = itemView.findViewById(R.id.txtDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewpager_item, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        instantiateItem(holder, position)
    }

    override fun getItemCount(): Int = items.size

    private fun instantiateItem(holder: PagerViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textViewTitle.text = item.title
        holder.textViewDesc.text = item.description
    }
}

data class ViewPagerItem(val imageResId: Int, val title: String, val description: String)
