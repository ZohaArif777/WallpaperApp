package com.wallpaper.features.wallpapers

import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wallpaper.R
import com.wallpaper.base_app.Constants
import com.wallpaper.databinding.ActivityWallpaperListBinding
import com.wallpaper.databinding.DialogNoInternetBinding
import com.wallpaper.features.wallpapers.adapter.RecyclerviewAdapter
import com.wallpaper.features.data_class.WallpaperModel

class WallpaperList : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperListBinding
    private lateinit var adapter: RecyclerviewAdapter
    private var dialog: BottomSheetDialog? = null
    private lateinit var networkReceiver: BroadcastReceiver
    private var subcategory: String = "Wallpaper"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        subcategory =
            intent.getStringExtra("WALLPAPER_SUBCATEGORY") ?: getString(R.string.wallpaper)
        Log.d("WallpaperList", "Received subcategory: $subcategory")
        binding.wallpaperText.text = subcategory
        setupRecyclerView(subcategory)
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (isInternetAvailable()) {
                    Log.d("WallpaperList", "Internet is back! Reloading wallpapers...")
                    dialog?.dismiss()
                    setupRecyclerView(subcategory)
                }
            }
        }
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: Exception) {
            Log.e("WallpaperList", "Receiver not registered: ${e.message}")
        }
    }

    private fun setupRecyclerView(subcategory: String) {
        if (!isInternetAvailable()) {
            showNoInternetDialog()
            return
        }

        val wallpaperList = getWallpapersBySubcategory(subcategory)

        if (wallpaperList.isEmpty()) {
            Log.e("WallpaperList", "No wallpapers available for: $subcategory")
            Toast.makeText(this, "No wallpapers found for this category", Toast.LENGTH_SHORT).show()
            return
        }

        val mainCategory = getMainCategory(this, subcategory)

        adapter = RecyclerviewAdapter(this, wallpaperList, { selectedWallpaper ->
            selectedWallpaper.let { wallpaper ->
                if (!isInternetAvailable()) {
                    showNoInternetDialog()
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

    private fun getMainCategory(context: Context, subcategory: String): String {
        return when (subcategory) {
            context.getString(R.string.iphone16_label),
            context.getString(R.string.iphone15_label),
            context.getString(R.string.iphone14_label),
            context.getString(R.string.iphone13_label),
            context.getString(R.string.iphone12_label),
            context.getString(R.string.iphone11_label) ->
                context.getString(R.string.iphone)

            context.getString(R.string.s25_label),
            context.getString(R.string.s24_label),
            context.getString(R.string.s23_label),
            context.getString(R.string.s22_label),
            context.getString(R.string.s21_label),
            context.getString(R.string.s20_label) ->
                context.getString(R.string.samsung)

            context.getString(R.string.ios18_label),
            context.getString(R.string.ios17_label),
            context.getString(R.string.ios16_label),
            context.getString(R.string.ios15_label),
            context.getString(R.string.ios14_label),
            context.getString(R.string.ios13_label) ->
                context.getString(R.string.ios)

            context.getString(R.string.car_label),
            context.getString(R.string.animal_label),
            context.getString(R.string.nature_label),
            context.getString(R.string.aesthetics_label),
            context.getString(R.string.city_label),
            context.getString(R.string.abstract_label) ->
                context.getString(R.string.HD)

            else -> "Wallpapers"
        }
    }

    private fun getWallpapersBySubcategory(subcategory: String): List<WallpaperModel> {
        val categoryMap = mapOf(
            getString(R.string.iphone16_label) to Constants.iphone16,
            getString(R.string.iphone15_label) to Constants.iphone15,
            getString(R.string.iphone14_label) to Constants.iphone14,
            getString(R.string.iphone13_label) to Constants.iphone13,
            getString(R.string.iphone12_label) to Constants.iphone12,
            getString(R.string.iphone11_label) to Constants.iphone11,
            getString(R.string.abstract_label) to Constants.abstract,
            getString(R.string.car_label) to Constants.car,
            getString(R.string.animal_label) to Constants.animal,
            getString(R.string.nature_label) to Constants.nature,
            getString(R.string.aesthetics_label) to Constants.aesthetics,
            getString(R.string.city_label) to Constants.city,
            getString(R.string.s25_label) to Constants.s25,
            getString(R.string.s24_label) to Constants.s24,
            getString(R.string.s23_label) to Constants.s23,
            getString(R.string.s22_label) to Constants.s22,
            getString(R.string.s21_label) to Constants.s21,
            getString(R.string.s20_label) to Constants.s20,
            getString(R.string.ios18_label) to Constants.ios18,
            getString(R.string.ios17_label) to Constants.ios17,
            getString(R.string.ios16_label) to Constants.ios16,
            getString(R.string.ios15_label) to Constants.ios15,
            getString(R.string.ios14_label) to Constants.ios14,
            getString(R.string.ios13_label) to Constants.ios13,
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
}