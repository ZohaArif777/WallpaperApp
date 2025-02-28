package com.wallpaper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wallpaper.databinding.DetailedListItemBinding
import com.wallpaper.databinding.ItemListBinding
import com.wallpaper.dataclass.Wallpaper

class RecyclerviewAdapter(
    private var wallpaperList: List<Wallpaper>,
    private val onItemClick: (Wallpaper) -> Unit,
    private val isDetailedView: Boolean // Determines which layout to use
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (isDetailedView) {
            val binding = DetailedListItemBinding.inflate(inflater, parent, false)
            WallpaperListViewHolder(binding)
        } else {
            val binding = ItemListBinding.inflate(inflater, parent, false)
            WallpaperViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val wallpaper = wallpaperList[position]

        when (holder) {
            is WallpaperViewHolder -> {
                holder.binding.txtWallpaper.text = wallpaper.txt
                holder.binding.img.setImageResource(wallpaper.img)

                holder.binding.root.setOnClickListener {
                    onItemClick(wallpaper) // Pass wallpaper object to click listener
                }
            }

            is WallpaperListViewHolder -> {
                holder.binding.img.setImageResource(wallpaper.img)

                holder.binding.root.setOnClickListener {
                    onItemClick(wallpaper) // Pass wallpaper object to click listener
                }
            }
        }
    }

    override fun getItemCount(): Int = wallpaperList.size

    class WallpaperViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
    class WallpaperListViewHolder(val binding: DetailedListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
