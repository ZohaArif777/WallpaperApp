package com.wallpaper.features.ringtons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.tabs.TabLayoutMediator
import com.wallpaper.R
import com.wallpaper.features.ringtons.adapter.RingtoneViewpagerAdapter
import com.wallpaper.databinding.ActivityRingtoneBinding
import com.wallpaper.features.fragments.ApplyRingtone
import com.wallpaper.features.fragments.Notification

class Ringtone : AppCompatActivity() {
    private lateinit var binding: ActivityRingtoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingtoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerWithTabs()
        binding.btn.setOnClickListener {
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
                0 -> getString(R.string.notification)
                1 -> getString(R.string.ringtones)
                else -> "Tab $position"
            }
        }.attach()

    }
}
