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

        category = intent.getStringExtra("CATEGORY_NAME") ?: getString(R.string.wallpaper)
        Log.d("Wallpaper", "Received category: $category")
        binding.wallpaperText.text = category

        setupRecyclerView(category)
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (isInternetAvailable()) {
                    Log.d("Wallpaper", "Internet is back! Reloading wallpapers...")
                    dialog?.dismiss()
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
            getString(R.string.iphone) -> listOf(
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}iphone16/1.png",
                    txt = getString(R.string.iphone16_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}iphone15/1.png",
                    txt = getString(R.string.iphone15_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}iphone14/1.png",
                    txt = getString(R.string.iphone14_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}iphone13/1.png",
                    txt = getString(R.string.iphone13_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}iphone12/1.png",
                    txt = getString(R.string.iphone12_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}iphone11/1.png",
                    txt = getString(R.string.iphone11_label)
                )
            )

            getString(R.string.hd) -> listOf(
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}car/1.png",
                    txt = getString(R.string.car_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}animal/1.png",
                    txt = getString(R.string.animal_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}nature/1.png",
                    txt = getString(R.string.nature_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}aesthetics/1.png",
                    txt = getString(R.string.aesthetics_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}city/1.png",
                    txt = getString(R.string.city_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}abstract/1.png",
                    txt = getString(R.string.abstract_label)
                )
            )

            getString(R.string.ios) -> listOf(
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}ios18/1.png",
                    txt = getString(R.string.ios18_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}ios17/1.png",
                    txt = getString(R.string.ios17_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}ios16/1.png",
                    txt = getString(R.string.ios16_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}ios15/1.png",
                    txt = getString(R.string.ios15_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}ios14/1.png",
                    txt = getString(R.string.ios14_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}ios13/1.png",
                    txt = getString(R.string.ios13_label)
                )
            )


            getString(R.string.samsung) -> listOf(
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}s25/1.png",
                    txt = getString(R.string.s25_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}s24/1.png",
                    txt = getString(R.string.s24_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}s23/1.png",
                    txt = getString(R.string.s23_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}s22/1.png",
                    txt = getString(R.string.s22_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}s21/1.png",
                    txt = getString(R.string.s21_label)
                ),
                WallpaperModel(
                    imageUrl = "${Constants.BASE_URL}s20/1.png",
                    txt = getString(R.string.s20_label)
                )
            )

            else -> emptyList()
        }
    }
}
