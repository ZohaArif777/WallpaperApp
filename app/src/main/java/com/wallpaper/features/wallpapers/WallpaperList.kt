package com.wallpaper.features.wallpapers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.R
import com.wallpaper.features.adapters.RecyclerviewAdapter
import com.wallpaper.databinding.ActivityWallpaperListBinding
import com.wallpaper.features.data_class.Wallpaper

class WallpaperList : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperListBinding
    private lateinit var adapter: RecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val subcategory = intent.getStringExtra("WALLPAPER_SUBCATEGORY") ?: "Wallpapers"
        binding.wallpaperText.text = subcategory
        setupRecyclerView(subcategory)
    }

    private fun setupRecyclerView(subcategory: String) {
        val wallpaperList = getWallpapersBySubcategory(subcategory)

        adapter = RecyclerviewAdapter(wallpaperList, { selectedWallpaper ->
            val intent = Intent(this, WallpaperPlayer::class.java)
            intent.putExtra("WALLPAPER_IMAGE", selectedWallpaper.img)
            startActivity(intent)
        }, isDetailedView = true)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getWallpapersBySubcategory(subcategory: String): List<Wallpaper> {
        return when (subcategory) {
            "iPhone 16" -> listOf(
                Wallpaper(R.drawable.iphone16_1),
                Wallpaper(R.drawable.iphone16_1),
                Wallpaper(R.drawable.iphone16_1)
            )

            "iPhone 15" -> listOf(
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15)
            )

            "iPhone 14" -> listOf(
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15)
            )

            "iPhone 13" -> listOf(
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15)
            )

            "iPhone 12" -> listOf(
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15)
            )

            "iPhone 11" -> listOf(
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15),
                Wallpaper(R.drawable.iphone_15)
            )

            "Car" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2)
            )
            "Animal" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2)
            )
            "Nature" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2)
            )
            "Aesthetics" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2)
            )
            "City" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2)
            )
            "Abstract" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1),
                Wallpaper(R.drawable.hd_wallpaper2)
            )

            "S25" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2)
            )

            "S24" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2)
            )

            "S23" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2)
            )

            "S22" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2)
            )

            "S21" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2)
            )

            "S20" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1),
                Wallpaper(R.drawable.samsung_wallpaper2)
            )

            "iOS 18" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1)
            )

            "iOS 17" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1)
            )

            "iOS 16" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1)
            )

            "iOS 15" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1)
            )

            "iOS 14" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1),
                Wallpaper(R.drawable.ios_wallpaper1)
            )

            else -> emptyList()
        }
    }
}
