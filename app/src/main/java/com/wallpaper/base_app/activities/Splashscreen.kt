package com.wallpaper.base_app.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.wallpaper.base_app.localization.LocalizationActivity
import com.wallpaper.base_app.localization.SharedPrefs
import com.wallpaper.base_app.on_boardings.OnBoardingScreens
import com.wallpaper.databinding.ActivitySplashscreenBinding

class Splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )

            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        LocalizationActivity.setLocale(this)

        binding.getStarted.setOnClickListener {

            val isFirstTime = SharedPrefs.getPrefsBoolean(this, SharedPrefs.firstTimeKey, true)
            val isLocalizationShown =
                SharedPrefs.getPrefsBoolean(this, SharedPrefs.localizationShownKey, false)

            if (isFirstTime) {
                if (!isLocalizationShown) {
                    val intent = Intent(this, LocalizationActivity::class.java).apply {
                        putExtra("fromActivity", "splash")
                    }
                    startActivityForResult(intent, 1001)
                } else {
                   goToOnBoarding()
                }
            } else {
                goToMain()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            goToOnBoarding()
        }
    }

    private fun goToOnBoarding() {
        startActivity(Intent(this, OnBoardingScreens::class.java))
        finish()
    }

    private fun goToMain() {
        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

}
