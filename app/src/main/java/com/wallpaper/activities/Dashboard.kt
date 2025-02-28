package com.wallpaper.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.databinding.ActivityDashboardBinding
import com.wallpaper.setting.Setting

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
        binding.setting.setOnClickListener {
            val intent=Intent(this, Setting::class.java)
            startActivity(intent)
        }
    }

    private fun setupButtonListeners() {
        binding.iphone.setOnClickListener { navigateToWallpaper("iPhone") }
        binding.hd.setOnClickListener { navigateToWallpaper("HD") }
        binding.ios.setOnClickListener { navigateToWallpaper("iOS") }
        binding.samsung.setOnClickListener { navigateToWallpaper("Samsung") }
        binding.ringtone.setOnClickListener { navigateToRingtone() }
        binding.setting.setOnClickListener { navigateToSetting() }
    }

    private fun navigateToWallpaper(category: String) {
        val intent = Intent(this, Wallpaper::class.java)
        intent.putExtra("CATEGORY_NAME", category)
        startActivity(intent)
    }

    private fun navigateToSetting() {
        val intent = Intent(this, Setting::class.java)
        startActivity(intent)
    }
    private fun navigateToRingtone() {
        val intent = Intent(this, Ringtone::class.java)
        startActivity(intent)
    }

}
