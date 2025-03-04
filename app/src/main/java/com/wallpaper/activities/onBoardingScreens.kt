package com.wallpaper.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.wallpaper.R
import com.wallpaper.adapter.ViewPagerAdapter
import com.wallpaper.adapter.ViewPagerItem
import com.wallpaper.databinding.OnboardingScreensBinding

class OnBoardingScreens : AppCompatActivity() {

    private lateinit var binding: OnboardingScreensBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnboardingScreensBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewPager()
        setupButtons()
    }

    private fun setupViewPager() {
        val items = listOf(
            ViewPagerItem(
                R.drawable.wallpaper1,
                getString(R.string.wallpaper_title),
                getString(R.string.wallpaper_description)
            ),
            ViewPagerItem(
                R.drawable.hdwallpapaer,
                getString(R.string.ringtones_title),
                getString(R.string.ringtones_description)
            ),
            ViewPagerItem(
                R.drawable.wallapaper_4,
                getString(R.string.hd_wallpaper_title),
                getString(R.string.hd_wallpaper_description)
            ),
            ViewPagerItem(
                R.drawable.ringtones,
                getString(R.string.exclusive_content_title),
                getString(R.string.exclusive_content_description)
            )
        )

        val adapter = ViewPagerAdapter(items)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.indicator.setViewPager(binding.viewPager)

        // Page change listener to update button text
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text = if (position == items.lastIndex) "Done" else "Next"
            }
        })
    }


    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            if (nextItem < 4) {
                binding.viewPager.currentItem = nextItem
            } else {
                completeOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener {
            completeOnboarding()
        }
    }

    private fun completeOnboarding() {
        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("OnboardingCompleted", true)
        editor.apply()

        // Navigate to Dashboard
        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

}
