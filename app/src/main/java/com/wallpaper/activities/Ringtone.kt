package com.wallpaper.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.tabs.TabLayoutMediator
import com.wallpaper.adapter.RingtoneViewpagerAdapter
import com.wallpaper.databinding.ActivityRingtoneBinding
import com.wallpaper.fragments.ApplyRingtone
import com.wallpaper.fragments.Notification

class Ringtone : AppCompatActivity() {
    private lateinit var binding: ActivityRingtoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingtoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerWithTabs()
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViewPagerWithTabs() {
        val fragments = listOf(
            Notification(),
            ApplyRingtone()
        )

        val adapter = RingtoneViewpagerAdapter(fragments, supportFragmentManager, lifecycle)
        binding.ringtoneViewpager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.ringtoneViewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "Notification"
                1 -> " Ringtone"
                else -> "Tab $position"
            }
        }.attach()
    }
}
