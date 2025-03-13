package com.wallpaper.features.wallpapers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.wallpaper.R
import com.wallpaper.features.adapters.RecyclerviewAdapter
import com.wallpaper.databinding.ActivityWallpaperBinding
import com.wallpaper.features.data_class.Wallpaper

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

    private fun getSubCategories(category: String): List<Wallpaper> {
        return when (category) {
            "iPhone" -> listOf(
                Wallpaper(R.drawable.iphone16_1, "iPhone 16"),
                Wallpaper(R.drawable.iphone15_1, "iPhone 15"),
                Wallpaper(R.drawable.iphone14_1, "iPhone 14"),
                Wallpaper(R.drawable.iphone13_1, "iPhone 13"),
                Wallpaper(R.drawable.iphone12_1, "iPhone 12"),
                Wallpaper(R.drawable.iphone11_1, "iPhone 11")
            )

            "HD" -> listOf(
                Wallpaper(R.drawable.car, "Car"),
                Wallpaper(R.drawable.animal, "Animal"),
                Wallpaper(R.drawable.nature, "Nature"),
                Wallpaper(R.drawable.aesthetics, "Aesthetics"),
                Wallpaper(R.drawable.city, "City"),
                Wallpaper(R.drawable.abstracts, "Abstract")
            )

            "iOS" -> listOf(
                Wallpaper(R.drawable.ios18, "iOS 18"),
                Wallpaper(R.drawable.ios17, "iOS 17"),
                Wallpaper(R.drawable.ios16, "iOS 16"),
                Wallpaper(R.drawable.ios15, "iOS 15"),
                Wallpaper(R.drawable.ios14, "iOS 14"),
                Wallpaper(R.drawable.ios13, "iOS 13")
            )

            "Samsung" -> listOf(
                Wallpaper(R.drawable.s25, "S25"),
                Wallpaper(R.drawable.s24, "S24"),
                Wallpaper(R.drawable.s23, "S23"),
                Wallpaper(R.drawable.s22, "S22"),
                Wallpaper(R.drawable.s21, "S21"),
                Wallpaper(R.drawable.s20, "S20")
            )

            else -> emptyList()
        }
    }
}
