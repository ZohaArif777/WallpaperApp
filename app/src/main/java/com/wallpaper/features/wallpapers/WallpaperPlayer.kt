package com.wallpaper.features.wallpapers

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wallpaper.R
import com.wallpaper.databinding.ActivityWallpaperPlayerBinding
import com.wallpaper.features.lockScreens.LockscreenPreview
import java.io.IOException

class WallpaperPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperPlayerBinding
    private var isFullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wallpaperUrl = intent.getStringExtra("WALLPAPER_IMAGE")

        if (!wallpaperUrl.isNullOrEmpty()) {
            Glide.with(this).load(wallpaperUrl)
                .into(binding.img)
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnHome.setOnClickListener {
            openPreviewActivity(wallpaperUrl)
        }
        binding.btnLock.setOnClickListener {
            openLockPreviewActivity(wallpaperUrl)
        }
        binding.btnWallpaper.setOnClickListener {
            showBottomSheetDialog(wallpaperUrl)
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

    private fun openPreviewActivity(wallpaperUrl: String?) {
        val intent = Intent(this, wallpaperPreview::class.java)
        intent.putExtra("WALLPAPER_IMAGE", wallpaperUrl)
        startActivity(intent)
    }

    private fun openLockPreviewActivity(wallpaperUrl: String?) {
        val intent = Intent(this, LockscreenPreview::class.java)
        intent.putExtra("WALLPAPER_IMAGE", wallpaperUrl)
        startActivity(intent)
    }

    private fun showBottomSheetDialog(wallpaperUrl: String?) {
        if (wallpaperUrl.isNullOrEmpty()) {
            Toast.makeText(this, "No wallpaper selected!", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)

        val btnHomeScreen = view.findViewById<ImageButton>(R.id.btnHomeScreen)
        val btnLockScreen = view.findViewById<ImageButton>(R.id.btnLockScreen)
        val btnBothScreens = view.findViewById<ImageButton>(R.id.btnBothScreens)

        btnHomeScreen.setOnClickListener {
            setWallpaper(wallpaperUrl, WallpaperManager.FLAG_SYSTEM)
            dialog.dismiss()
        }

        btnLockScreen.setOnClickListener {
            setWallpaper(wallpaperUrl, WallpaperManager.FLAG_LOCK)
            dialog.dismiss()
        }

        btnBothScreens.setOnClickListener {
            setWallpaper(wallpaperUrl, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun setWallpaper(wallpaperUrl: String?, flag: Int) {
        if (wallpaperUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid wallpaper URL!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            Glide.with(this)
                .asBitmap()
                .load(wallpaperUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val wallpaperManager = WallpaperManager.getInstance(this@WallpaperPlayer)
                        wallpaperManager.setBitmap(resource, null, true, flag)
                        showSuccessDialog()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle if needed
                    }
                })
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
