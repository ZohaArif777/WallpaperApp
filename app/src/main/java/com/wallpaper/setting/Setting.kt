package com.wallpaper.setting

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.R
import com.wallpaper.databinding.ActivitySettingBinding
import java.io.IOException

class Setting : AppCompatActivity() {
    private lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rateButton.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                startActivity(intent)
            }
        }
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName

        binding.versionButton.text= "Version $versionName"

        binding.shareButton.setOnClickListener {
            val appPackageName = packageName
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out this amazing app: https://play.google.com/store/apps/details?id=$appPackageName"
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share app via"))
        }
        binding.wallpaperButton.setOnClickListener {
           resetToDefaultWallpaper()
        }
    }
    private fun resetToDefaultWallpaper() {
        try {
            val wallpaperManager = WallpaperManager.getInstance(this)
            wallpaperManager.clear() // Reset to system default wallpaper

            Toast.makeText(this, "Wallpaper reset to default!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to reset wallpaper", Toast.LENGTH_SHORT).show()
        }
    }


}
