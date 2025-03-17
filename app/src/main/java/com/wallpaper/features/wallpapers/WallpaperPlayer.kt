package com.wallpaper.features.wallpapers

import android.app.WallpaperManager
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WallpaperPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityWallpaperPlayerBinding
    private var isFullscreen = false
    private var savedFile: File? = null // To store downloaded file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wallpaperUrl = intent.getStringExtra("WALLPAPER_IMAGE") ?: ""
        val subcategory = intent.getStringExtra("WALLPAPER_CATEGORY") ?: "Unknown"
        val category = intent.getStringExtra("WALLPAPER_MAIN_CATEGORY") ?: "Unknown"

        if (wallpaperUrl.isNotEmpty()) {
            Glide.with(this).load(wallpaperUrl).into(binding.img)
        }

        binding.btnWallpaper.text = getString(R.string.download)

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
            if (savedFile == null) {
                downloadWallpaper(wallpaperUrl, category, subcategory)
            } else {
                showSetWallpaperDialog(savedFile!!)
            }
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

    private fun downloadWallpaper(wallpaperUrl: String?, category: String, subCategory: String) {
        if (wallpaperUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid wallpaper URL!", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Downloading wallpaper...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Glide.with(this)
            .asBitmap()
            .load(wallpaperUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    progressDialog.dismiss()

                    savedFile = saveImageToCache(resource, wallpaperUrl, category, subCategory)
                    if (savedFile != null) {
                        // Update button text to "Apply"
                        binding.btnWallpaper.text = "Apply"
                        Toast.makeText(
                            this@WallpaperPlayer,
                            "Wallpaper downloaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@WallpaperPlayer,
                            "Failed to save wallpaper",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    progressDialog.dismiss()
                }
            })
    }

    private fun showSetWallpaperDialog(savedFile: File) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)

        val btnHomeScreen = view.findViewById<ImageButton>(R.id.btnHomeScreen)
        val btnLockScreen = view.findViewById<ImageButton>(R.id.btnLockScreen)
        val btnBothScreens = view.findViewById<ImageButton>(R.id.btnBothScreens)

        btnHomeScreen.setOnClickListener {
            applyWallpaper(savedFile, WallpaperManager.FLAG_SYSTEM)
            dialog.dismiss()
        }

        btnLockScreen.setOnClickListener {
            applyWallpaper(savedFile, WallpaperManager.FLAG_LOCK)
            dialog.dismiss()
        }

        btnBothScreens.setOnClickListener {
            applyWallpaper(savedFile, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun saveImageToCache(
        bitmap: Bitmap,
        wallpaperUrl: String,
        category: String,
        subCategory: String
    ): File? {
        val cacheDir = File(getExternalFilesDir(null), "Wallpapers/$category/$subCategory")
        if (!cacheDir.exists()) cacheDir.mkdirs()

        val fileName = wallpaperUrl.substringAfterLast("/")
        val file = File(cacheDir, fileName)
        if (file.exists()) return file
        return try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun applyWallpaper(file: File, flag: Int) {
        try {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val wallpaperManager = WallpaperManager.getInstance(this)
            wallpaperManager.setBitmap(bitmap, null, true, flag)

            showSuccessDialog()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to apply wallpaper", Toast.LENGTH_SHORT).show()
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
