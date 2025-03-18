package com.wallpaper.features.wallpapers

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.R
import com.wallpaper.base_app.Constants
import com.wallpaper.features.wallpapers.adapter.RecyclerviewAdapter
import com.wallpaper.databinding.ActivityWallpaperListBinding
import com.wallpaper.features.data_class.WallpaperModel

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
        if (!isInternetAvailable()) {
            Toast.makeText(
                this,
                getString(R.string.internet_error),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val wallpaperList = getWallpapersBySubcategory(subcategory)

        if (wallpaperList.isEmpty()) {
            Log.e("WallpaperList", "No wallpapers available for: $subcategory")
            Toast.makeText(this, "No wallpapers found for this category", Toast.LENGTH_SHORT).show()
            return
        }

        val mainCategory = getMainCategory(subcategory)

        adapter = RecyclerviewAdapter(this, wallpaperList, { selectedWallpaper ->
            selectedWallpaper.let { wallpaper ->
                if (!isInternetAvailable()) {
                    Toast.makeText(this, "Internet required to load wallpaper", Toast.LENGTH_SHORT)
                        .show()
                    return@let
                }

                if (wallpaper.imageUrl?.isNotEmpty() == true) {
                    val intent = Intent(this, WallpaperPlayer::class.java).apply {
                        putExtra("WALLPAPER_IMAGE", wallpaper.imageUrl)
                        putExtra("WALLPAPER_MAIN_CATEGORY", mainCategory)
                        putExtra("WALLPAPER_CATEGORY", subcategory)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid wallpaper URL", Toast.LENGTH_SHORT).show()
                }
            }
        }, isDetailedView = true)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getMainCategory(subcategory: String): String {
        return when (subcategory) {
            "iPhone 16", "iPhone 15", "iPhone 14", "iPhone 13", "iPhone 12", "iPhone 11" -> "iPhone"
            "S25", "S24", "S23", "S22", "S21", "S20" -> "Samsung"
            "iOS 18", "iOS 17", "iOS 16", "iOS 15", "iOS 14", "iOS 13" -> "iOS"
            "Car", "Animal", "Nature", "Aesthetics", "City", "Abstract" -> "HD"
            else -> "Wallpapers"
        }
    }

    private fun getWallpapersBySubcategory(subcategory: String): List<WallpaperModel> {
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
            WallpaperModel(url)
        }
        return wallpapers
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
