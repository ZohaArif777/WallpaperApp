package com.wallpaper.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.databinding.ActivitySplashscreenBinding

class Splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simulateSeekBarProgress()
    }

    private fun simulateSeekBarProgress() {
        val runnable = object : Runnable {
            override fun run() {
                if (progress <= 100) {
                    binding.seekBar.progress = progress
                    binding.percentageText.text = "$progress%"
                    progress += 5
                    handler.postDelayed(this, 100)
                } else {
                    checkOnboardingStatus()
                }
            }
        }
        handler.post(runnable)
    }

    private fun checkOnboardingStatus() {
        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val hasSeenOnboarding = prefs.getBoolean("OnboardingCompleted", false)

        if (!hasSeenOnboarding) {
            startActivity(Intent(this, OnBoardingScreens::class.java))
        } else {
            startActivity(Intent(this, Dashboard::class.java))
        }
        finish()
    }
}
