package com.wallpaper.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.R
import com.wallpaper.adapter.RecyclerviewAdapter
import com.wallpaper.databinding.ActivityWallpaperBinding
import com.wallpaper.dataclass.Wallpaper

class Wallpaper : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperBinding
    private lateinit var adapter: RecyclerviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("CATEGORY_NAME") ?: "Wallpapers"
        binding.wallpaperText.text = "$category Wallpaper"

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView(category)
    }

    private fun setupRecyclerView(category: String) {
        val wallpaperList = getWallpapersByCategory(category)

        adapter = RecyclerviewAdapter(wallpaperList, {
            val intent = Intent(this, WallpaperList::class.java)
            intent.putExtra("WALLPAPER_CATEGORY", category)
            startActivity(intent)
        }, isDetailedView = false) // Use item_list.xml

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getWallpapersByCategory(category: String): List<Wallpaper> {
        return when (category) {
            "iPhone" -> listOf(
                Wallpaper(R.drawable.iphone_16, "iPhone 16"),
                Wallpaper(R.drawable.iphone_15, "iPhone 15"),
                Wallpaper(R.drawable.iphone_14, "iPhone 14"),
                Wallpaper(R.drawable.iphone_13, "iPhone 13"),
                Wallpaper(R.drawable.iphone12, "iPhone 12"),
                Wallpaper(R.drawable.iphone11, "iPhone 11"),
                Wallpaper(R.drawable.iphone10, "iPhone 10")
            )

            "HD" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1, "HD Wallpaper 1"),
                Wallpaper(R.drawable.hd_wallpaper2, "HD Wallpaper 2"),
                Wallpaper(R.drawable.hd_wallpaper3, "HD Wallpaper 3"),
                Wallpaper(R.drawable.hd_wallpaper4, "HD Wallpaper 4"),
                Wallpaper(R.drawable.hd_wallpaper5, "HD Wallpaper 5")
            )

            "iOS" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1, "iOS Wallpaper 1"),
                Wallpaper(R.drawable.ios_wallpaper2, "iOS Wallpaper 2"),
                Wallpaper(R.drawable.ios_wallpaper3, "iOS Wallpaper 3"),
                Wallpaper(R.drawable.ios_wallpaper4, "iOS Wallpaper 4"),
                Wallpaper(R.drawable.ios_wallpaper5, "iOS Wallpaper 5")
            )

            "Samsung" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1, "Samsung Wallpaper 1"),
                Wallpaper(R.drawable.samsung_wallpaper2, "Samsung Wallpaper 2"),
                Wallpaper(R.drawable.samsung_wallpaper3, "Samsung Wallpaper 3"),
                Wallpaper(R.drawable.samsung_wallpaper4, "Samsung Wallpaper 4"),
                Wallpaper(R.drawable.samsung_wallpaper, "Samsung Wallpaper 5")
            )

            "Ringtones" -> listOf(
                Wallpaper(R.drawable.ringtones, "Ringtone Wallpaper 1"),
                Wallpaper(R.drawable.ringtone_wallpaper1, "Ringtone Wallpaper 2")
            )

            else -> emptyList()
        }
    }
}
