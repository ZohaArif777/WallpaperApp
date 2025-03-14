package com.wallpaper.features.wallpapers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.R
import com.wallpaper.base_app.Constants
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
        Log.d("WallpaperList", "Received subcategory: $subcategory")
        binding.wallpaperText.text = subcategory

        setupRecyclerView(subcategory)
    }

    private fun setupRecyclerView(subcategory: String) {
        val wallpaperList = getWallpapersBySubcategory(subcategory)
        val mainCategory = getMainCategory(subcategory) // Get the main category

        adapter = RecyclerviewAdapter(this, wallpaperList, { selectedWallpaper ->
            selectedWallpaper.let { wallpaper ->  // Ensure wallpaper is not null
                val intent = Intent(this, WallpaperPlayer::class.java).apply {
                    putExtra("WALLPAPER_IMAGE", wallpaper.imageUrl)
                    putExtra("WALLPAPER_MAIN_CATEGORY", mainCategory) // Corrected intent
                    putExtra("WALLPAPER_CATEGORY", subcategory)
                }
                startActivity(intent)
            }
        }, isDetailedView = true)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Function to get the main category based on the subcategory
    private fun getMainCategory(subcategory: String): String {
        return when (subcategory) {
            "iPhone 16", "iPhone 15", "iPhone 14", "iPhone 13", "iPhone 12", "iPhone 11" -> "iPhone"
            "S25", "S24", "S23", "S22", "S21", "S20" -> "Samsung"
            "iOS 18", "iOS 17", "iOS 16", "iOS 15", "iOS 14", "iOS 13" -> "iOS"
            "Car", "Animal", "Nature", "Aesthetics", "City", "Abstract" -> "HD"
            else -> "Wallpapers"
        }
    }

    private fun getWallpapersBySubcategory(subcategory: String): List<Wallpaper> {
        val categoryMap = mapOf(
            "iPhone 16" to Constants.iphone16,
            "iPhone 15" to Constants.iphone15,
            "iPhone 14" to Constants.iphone14,
            "iPhone 13" to Constants.iphone13,
            "iPhone 12" to Constants.iphone12,
            "iPhone 11" to Constants.iphone11,
            "Abstract" to Constants.abstract,
            "Car" to Constants.car,
            "Animal" to Constants.animal,
            "Nature" to Constants.nature,
            "Aesthetics" to Constants.aesthetics,
            "City" to Constants.city,
            "S25" to Constants.s25,
            "S24" to Constants.s24,
            "S23" to Constants.s23,
            "S22" to Constants.s22,
            "S21" to Constants.s21,
            "S20" to Constants.s20,
            "iOS 18" to Constants.ios18,
            "iOS 17" to Constants.ios17,
            "iOS 16" to Constants.ios16,
            "iOS 15" to Constants.ios15,
            "iOS 14" to Constants.ios14,
            "iOS 13" to Constants.ios13,
        )

        val categoryKey = categoryMap[subcategory]

        if (categoryKey == null) {
            Log.e("WallpaperList", "No wallpapers found for: $subcategory")
            return emptyList()
        }

        val wallpapers = List(5) { index ->
            val url = "${Constants.BASE_URL}$categoryKey/${index + 1}.png"
            Log.d("WallpaperList", "Generated Wallpaper URL: $url")
            Wallpaper(url)
        }
        return wallpapers
    }
}
