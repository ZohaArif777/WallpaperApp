package com.wallpaper.base_app.setting

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.databinding.ActivitySettingBinding
import com.wallpaper.base_app.language.Language
import com.wallpaper.base_app.localization.LocalizationActivity
import com.wallpaper.base_app.localization.SharedPrefs
import java.io.IOException

class Setting : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            arrowIcon.setOnClickListener { navigateToLanguage() }

            languageButton.setOnClickListener { navigateToLanguage() }
            rateButton.setOnClickListener { openAppInStore() }
            shareButton.setOnClickListener { shareApp() }
            wallpaperButton.setOnClickListener { resetToDefaultWallpaper() }

            val selectedLanguageName = SharedPrefs.getPrefsString(this@Setting, "selectedLanguageName", "English")
            languageSelectedText.text = selectedLanguageName

            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            versionText.text = versionName
        }
    }



    private fun openAppInStore() {
        val appUri = Uri.parse("market://details?id=$packageName")
        val webUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        try {
            startActivity(Intent(Intent.ACTION_VIEW, appUri))
        } catch (e: ActivityNotFoundException) {

            startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this amazing app: https://play.google.com/store/apps/details?id=$packageName"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share app via"))
    }

    private fun resetToDefaultWallpaper() {
        try {
            WallpaperManager.getInstance(this).clear()
            showToast("Wallpaper reset to default!")
        } catch (e: IOException) {
            showToast("Failed to reset Wallpaper")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLanguage() {
        val intent = Intent(this, LocalizationActivity::class.java)
        startActivity(intent)
        finish()
    }
}
