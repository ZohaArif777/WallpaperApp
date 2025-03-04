package com.wallpaper.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.wallpaper.R
import com.wallpaper.adapter.RecyclerviewAdapter
import com.wallpaper.databinding.ActivityWallpaperListBinding
import com.wallpaper.dataclass.Wallpaper

class WallpaperList : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperListBinding
    private lateinit var adapter: RecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("WALLPAPER_CATEGORY") ?: "Wallpapers"

        setupRecyclerView(category)
    }

    private fun setupRecyclerView(category: String) {
        val wallpaperList = getWallpapersByCategory(category)

        adapter = RecyclerviewAdapter(wallpaperList, { selectedWallpaper ->
            val intent = Intent(this, WallpaperPlayer::class.java)
            intent.putExtra("WALLPAPER_IMAGE", selectedWallpaper.img)
            startActivity(intent)
        }, isDetailedView = true)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }


    private fun getWallpapersByCategory(category: String): List<Wallpaper> {
        return when (category) {
            "iPhone" -> listOf(
                Wallpaper(R.drawable.iphone_16),
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_14),
                Wallpaper(R.drawable.iphone_13),
                Wallpaper(R.drawable.iphone12),
                Wallpaper(R.drawable.iphone11),
                Wallpaper(R.drawable.iphone10)
            )

            "HD" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2),
                Wallpaper(R.drawable.hd_wallpaper3),
                Wallpaper(R.drawable.hd_wallpaper4),
                Wallpaper(R.drawable.hd_wallpaper5)
            )

            "iOS" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper2),
                Wallpaper(R.drawable.ios_wallpaper3),
                Wallpaper(R.drawable.ios_wallpaper4),
                Wallpaper(R.drawable.ios_wallpaper5)
            )

            "Samsung" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2),
                Wallpaper(R.drawable.samsung_wallpaper3),
                Wallpaper(R.drawable.samsung_wallpaper4),
                Wallpaper(R.drawable.samsung_wallpaper)
            )
            "Ringtones" -> listOf(
                Wallpaper(R.drawable.ringtones, "Ringtone Wallpaper 1"),
                Wallpaper(R.drawable.ringtone_wallpaper1, "Ringtone Wallpaper 2")
            )

            else -> emptyList()
        }
    }
}
