package com.wallpaper.features.wallpapers

import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wallpaper.R
import com.wallpaper.base_app.Constants
import com.wallpaper.databinding.ActivityWallpaperBinding
import com.wallpaper.databinding.DialogNoInternetBinding
import com.wallpaper.features.data_class.WallpaperModel
import com.wallpaper.features.wallpapers.adapter.RecyclerviewAdapter

class Wallpaper : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperBinding
    private lateinit var adapter: RecyclerviewAdapter
    private var dialog: BottomSheetDialog? = null
    private lateinit var networkReceiver: BroadcastReceiver
    private var category: String = "Wallpapers"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        category = intent.getStringExtra("CATEGORY_NAME") ?: "Wallpapers"
        Log.d("Wallpaper", "Received category: $category")
        binding.wallpaperText.text = category

        setupRecyclerView(category)

        // Register BroadcastReceiver to monitor internet connectivity
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (isInternetAvailable()) {
                    Log.d("Wallpaper", "Internet is back! Reloading wallpapers...")
                    dialog?.dismiss() // Dismiss the no-internet dialog
                    setupRecyclerView(category)
                }
            }
        }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(networkReceiver) // Unregister to prevent crashes
        } catch (e: Exception) {
            Log.e("Wallpaper", "Receiver not registered: ${e.message}")
        }
    }

    private fun setupRecyclerView(category: String) {
        if (!isInternetAvailable()) {
            showNoInternetDialog()
            return
        }

        val wallpaperList = getSubCategories(category)

        if (wallpaperList.isEmpty()) {
            Log.e("Wallpaper", "No wallpapers available for: $category")
            Toast.makeText(this, "No wallpapers found for this category", Toast.LENGTH_SHORT).show()
            return
        }

        adapter = RecyclerviewAdapter(this, wallpaperList, { selectedCategory ->
            if (!isInternetAvailable()) {
                showNoInternetDialog()
                return@RecyclerviewAdapter
            }

            val intent = Intent(this, WallpaperList::class.java)
            intent.putExtra("WALLPAPER_SUBCATEGORY", selectedCategory.txt)
            startActivity(intent)
        }, isDetailedView = false)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetDialog() {
        val dialogBinding = DialogNoInternetBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(this)
        dialog?.setContentView(dialogBinding.root)

        dialogBinding.titleText.text = getString(R.string.check_your_internet_connection)

        dialogBinding.internet.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        dialogBinding.close.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    private fun getSubCategories(category: String): List<WallpaperModel> {
        return when (category) {
            "iPhone" -> listOf(
                WallpaperModel(imageUrl = "${Constants.BASE_URL}iphone16/1.png", txt = "iPhone 16"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}iphone15/1.png", txt = "iPhone 15"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}iphone14/1.png", txt = "iPhone 14"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}iphone13/1.png", txt = "iPhone 13"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}iphone12/1.png", txt = "iPhone 12"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}iphone11/1.png", txt = "iPhone 11")
            )

            "HD" -> listOf(
                WallpaperModel(imageUrl = "${Constants.BASE_URL}car/1.png", txt = "Car"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}animal/1.png", txt = "Animal"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}nature/1.png", txt = "Nature"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}aesthetics/1.png", txt = "Aesthetics"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}city/1.png", txt = "City"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}abstract/1.png", txt = "Abstract")
            )

            "iOS" -> listOf(
                WallpaperModel(imageUrl = "${Constants.BASE_URL}ios18/1.png", txt = "iOS 18"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}ios17/1.png", txt = "iOS 17"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}ios16/1.png", txt = "iOS 16"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}ios15/1.png", txt = "iOS 15"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}ios14/1.png", txt = "iOS 14"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}ios13/1.png", txt = "iOS 13")
            )

            "Samsung" -> listOf(
                WallpaperModel(imageUrl = "${Constants.BASE_URL}s25/1.png", txt = "S25"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}s24/1.png", txt = "S24"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}s23/1.png", txt = "S23"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}s22/1.png", txt = "S22"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}s21/1.png", txt = "S21"),
                WallpaperModel(imageUrl = "${Constants.BASE_URL}s20/1.png", txt = "S20")
            )

            else -> emptyList()
        }
    }
}
