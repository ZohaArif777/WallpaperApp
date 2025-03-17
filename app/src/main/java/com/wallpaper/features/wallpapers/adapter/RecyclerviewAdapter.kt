package com.wallpaper.features.wallpapers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wallpaper.databinding.DetailedListItemBinding
import com.wallpaper.databinding.ItemListBinding
import com.wallpaper.features.data_class.WallpaperModel

class RecyclerviewAdapter(
    var context : Context,
    private var wallpaperList: List<WallpaperModel>,
    private val onItemClick: (WallpaperModel) -> Unit,
    private val isDetailedView: Boolean
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
                    onItemClick(wallpaper)
                }
            }

            is WallpaperListViewHolder -> {
//                holder.binding.img.setImageResource(wallpaper.img)
                Glide.with(context).load(wallpaper.imageUrl?.trim())

                    .into(holder.binding.img)
                holder.binding.root.setOnClickListener {
                    onItemClick(wallpaper)
                }
            }
        }
    }

    override fun getItemCount(): Int = wallpaperList.size

    class WallpaperViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
    class WallpaperListViewHolder(val binding: DetailedListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
