package com.wallpaper.features.lockScreens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wallpaper.R
import com.wallpaper.databinding.ActivityLockscreenPreviewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LockscreenPreview : AppCompatActivity() {
    private lateinit var binding: ActivityLockscreenPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLockscreenPreviewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val wallpaperUrl = intent.getStringExtra("WALLPAPER_IMAGE") ?: ""
        if (wallpaperUrl.isNotEmpty()) {
            Glide.with(this).load(wallpaperUrl).into(binding.imgWallpaper)
        }
        updateDateTime()
    }

    private fun updateDateTime() {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())

        binding.txtTime.text = timeFormat.format(Date())
        binding.txtDate.text = dateFormat.format(Date())
    }
}