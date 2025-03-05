package com.wallpaper.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.R

import com.wallpaper.databinding.ActivityWallpaperPreviewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class wallpaperPreview : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val wallpaperResId = intent.getIntExtra("WALLPAPER_IMAGE", R.drawable.hd_wallpaper1)
        binding.imgWallpaper.setImageResource(wallpaperResId)
        binding.preImg.setImageResource(wallpaperResId)
        updateDateTime()
    }

    private fun updateDateTime() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())

        binding.txtTime.text = timeFormat.format(Date())
        binding.txtDate.text = dateFormat.format(Date())
    }
}
