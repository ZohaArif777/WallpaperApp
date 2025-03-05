package com.wallpaper.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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