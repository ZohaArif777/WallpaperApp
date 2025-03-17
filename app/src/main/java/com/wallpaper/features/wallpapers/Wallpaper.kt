package com.wallpaper.features.wallpapers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.wallpaper.R
import com.wallpaper.features.wallpapers.adapter.RecyclerviewAdapter
import com.wallpaper.databinding.ActivityWallpaperBinding
import com.wallpaper.features.data_class.WallpaperModel

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

        adapter = RecyclerviewAdapter(this,wallpaperList, { selectedCategory ->
            val intent = Intent(this, WallpaperList::class.java)
            intent.putExtra("WALLPAPER_SUBCATEGORY", selectedCategory.txt)
            startActivity(intent)
        }, isDetailedView = false)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun getSubCategories(category: String): List<WallpaperModel> {
        return when (category) {
            "iPhone" -> listOf(
                WallpaperModel(R.drawable.iphone16_1, "iPhone 16"),
                WallpaperModel(R.drawable.iphone15_1, "iPhone 15"),
                WallpaperModel(R.drawable.iphone14_1, "iPhone 14"),
                WallpaperModel(R.drawable.iphone13_1, "iPhone 13"),
                WallpaperModel(R.drawable.iphone12_1, "iPhone 12"),
                WallpaperModel(R.drawable.iphone11_1, "iPhone 11")
            )

            "HD" -> listOf(
                WallpaperModel(R.drawable.car, "Car"),
                WallpaperModel(R.drawable.animal, "Animal"),
                WallpaperModel(R.drawable.nature, "Nature"),
                WallpaperModel(R.drawable.aesthetics, "Aesthetics"),
                WallpaperModel(R.drawable.city, "City"),
                WallpaperModel(R.drawable.abstracts, "Abstract")
            )

            "iOS" -> listOf(
                WallpaperModel(R.drawable.ios18, "iOS 18"),
                WallpaperModel(R.drawable.ios17, "iOS 17"),
                WallpaperModel(R.drawable.ios16, "iOS 16"),
                WallpaperModel(R.drawable.ios15, "iOS 15"),
                WallpaperModel(R.drawable.ios14, "iOS 14"),
                WallpaperModel(R.drawable.ios13, "iOS 13")
            )

            "Samsung" -> listOf(
                WallpaperModel(R.drawable.s25, "S25"),
                WallpaperModel(R.drawable.s24, "S24"),
                WallpaperModel(R.drawable.s23, "S23"),
                WallpaperModel(R.drawable.s22, "S22"),
                WallpaperModel(R.drawable.s21, "S21"),
                WallpaperModel(R.drawable.s20, "S20")
            )

            else -> emptyList()
        }
    }
}
