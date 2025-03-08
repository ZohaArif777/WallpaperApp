package com.wallpaper.features

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wallpaper.R
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
            showBottomSheetDialog(wallpaperResId)
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
        val intent = Intent(this, LockscreenPreview::class.java)
        intent.putExtra("WALLPAPER_IMAGE", wallpaperId)
        startActivity(intent)
    }

    private fun showBottomSheetDialog(wallpaperResId: Int) {
        if (wallpaperResId == 0) {
            Toast.makeText(this, "No wallpaper selected!", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)

        val btnHomeScreen = view.findViewById<ImageButton>(R.id.btnHomeScreen)
        val btnLockScreen = view.findViewById<ImageButton>(R.id.btnLockScreen)
        val btnBothScreens = view.findViewById<ImageButton>(R.id.btnBothScreens)

        btnHomeScreen.setOnClickListener {
            setWallpaper(wallpaperResId, WallpaperManager.FLAG_SYSTEM)
            dialog.dismiss()
        }

        btnLockScreen.setOnClickListener {
            setWallpaper(wallpaperResId, WallpaperManager.FLAG_LOCK)
            dialog.dismiss()
        }

        btnBothScreens.setOnClickListener {
            setWallpaper(wallpaperResId, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setWallpaper(wallpaperResId: Int, flag: Int) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(this)
            val bitmap = BitmapFactory.decodeResource(resources, wallpaperResId)
            wallpaperManager.setBitmap(bitmap, null, true, flag)

            showSuccessDialog()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSuccessDialog() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_done)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
