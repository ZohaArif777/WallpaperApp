package com.wallpaper.activities

import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.databinding.ActivityWallpaperPlayerBinding
import java.io.IOException

class WallpaperPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperPlayerBinding
    private var isFullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wallpaperResId = intent.getIntExtra("WALLPAPER_IMAGE", 0)
        if (wallpaperResId != 0) {
            binding.img.setImageResource(wallpaperResId)
        } else {
            Toast.makeText(this, "No wallpaper selected!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnHome.setOnClickListener {
            openPreviewActivity(wallpaperResId)
        }
        binding.btnLock.setOnClickListener {
            openLockPreviewActivity(wallpaperResId)
        }
        binding.btnWallpaper.setOnClickListener {
            showWallpaperOptions(wallpaperResId)
        }

        binding.btnFit.setOnClickListener {
            toggleFullscreenMode()
        }

        binding.img.setOnClickListener {
            if (isFullscreen) toggleFullscreenMode()
        }
    }

    private fun toggleFullscreenMode() {
        isFullscreen = !isFullscreen

        val visibility = if (isFullscreen) View.GONE else View.VISIBLE
        binding.btnBack.visibility = visibility
        binding.btnHome.visibility = visibility
        binding.btnLock.visibility = visibility
        binding.btnWallpaper.visibility = visibility
        binding.btnFit.visibility = visibility
    }

    private fun openPreviewActivity(wallpaperId: Int) {
        val intent = Intent(this, wallpaperPreview::class.java)
        intent.putExtra("WALLPAPER_IMAGE", wallpaperId)
        startActivity(intent)
    }

    private fun openLockPreviewActivity(wallpaperId: Int) {
        val intent = Intent(this, lockscreenPreview::class.java)
        intent.putExtra("WALLPAPER_IMAGE", wallpaperId)
        startActivity(intent)
    }

    private fun showWallpaperOptions(wallpaperResId: Int) {
        if (wallpaperResId == 0) {
            Toast.makeText(this, "No wallpaper selected!", Toast.LENGTH_SHORT).show()
            return
        }

        val options = arrayOf("Home Screen", "Lock Screen", "Both")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Wallpaper")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> setWallpaper(wallpaperResId, WallpaperManager.FLAG_SYSTEM)
                    1 -> setWallpaper(wallpaperResId, WallpaperManager.FLAG_LOCK)
                    2 -> setWallpaper(
                        wallpaperResId,
                        WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                    )
                }
            }
            .show()
    }

    private fun setWallpaper(wallpaperResId: Int, flag: Int) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(this)
            val bitmap = BitmapFactory.decodeResource(resources, wallpaperResId)

            wallpaperManager.setBitmap(bitmap, null, true, flag)

            Toast.makeText(this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
        }
    }
}
