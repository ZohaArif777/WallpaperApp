package com.wallpaper.features

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
        binding.wallpaperText.text = category

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView(category)
    }

    private fun setupRecyclerView(category: String) {
        val wallpaperList = getSubCategories(category)

        adapter = RecyclerviewAdapter(wallpaperList, { selectedCategory ->
            val intent = Intent(this, WallpaperList::class.java)
            intent.putExtra("WALLPAPER_SUBCATEGORY", selectedCategory.txt)
            startActivity(intent)
        }, isDetailedView = false)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun getSubCategories(category: String): List<Wallpaper> {
        return when (category) {
            "iPhone" -> listOf(
                Wallpaper(R.drawable.iphone_16, "iPhone 16"),
                Wallpaper(R.drawable.iphone_15, "iPhone 15"),
                Wallpaper(R.drawable.iphone_14, "iPhone 14"),
                Wallpaper(R.drawable.iphone_13, "iPhone 13"),
                Wallpaper(R.drawable.iphone12, "iPhone 12"),
                Wallpaper(R.drawable.iphone11, "iPhone 11")
            )

            "HD" -> listOf(
                Wallpaper(R.drawable.hd_wallpaper1, "HD Wallpaper 1"),
                Wallpaper(R.drawable.hd_wallpaper2, "HD Wallpaper 2"),
                Wallpaper(R.drawable.hd_wallpaper3, "HD Wallpaper 3"),
                Wallpaper(R.drawable.hd_wallpaper4, "HD Wallpaper 4"),
                Wallpaper(R.drawable.hd_wallpaper5, "HD Wallpaper 5")
            )

            "iOS" -> listOf(
                Wallpaper(R.drawable.ios_wallpaper1, "iOS 18"),
                Wallpaper(R.drawable.ios_wallpaper2, "iOS 17"),
                Wallpaper(R.drawable.ios_wallpaper3, "iOS 16"),
                Wallpaper(R.drawable.ios_wallpaper4, "iOS 15"),
                Wallpaper(R.drawable.ios_wallpaper5, "iOS 14")
            )

            "Samsung" -> listOf(
                Wallpaper(R.drawable.samsung_wallpaper1, "S25"),
                Wallpaper(R.drawable.samsung_wallpaper2, "S24"),
                Wallpaper(R.drawable.samsung_wallpaper3, "S23"),
                Wallpaper(R.drawable.samsung_wallpaper4, "S22"),
                Wallpaper(R.drawable.samsung_wallpaper, "S21"),
                Wallpaper(R.drawable.samsung_wallpaper, "S20")
            )

            else -> emptyList()
        }
    }
}
