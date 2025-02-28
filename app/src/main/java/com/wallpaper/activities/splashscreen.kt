package com.wallpaper.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.databinding.ActivitySplashscreenBinding

class splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private val handler = Handler(Looper.getMainLooper())
    private var progress=0

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
                    binding.percentageText.text = "$progress%"  // Update percentage text
                    progress += 5
                    handler.postDelayed(this, 100)
                } else {
                    startActivity(Intent(this@splashscreen, MainActivity::class.java))
                    finish()
                }
            }
        }
        handler.post(runnable)
    }


}