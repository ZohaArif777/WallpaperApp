package com.wallpaper.base_app.on_boardings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.wallpaper.R
import com.wallpaper.features.adapters.ViewPagerAdapter
import com.wallpaper.features.adapters.ViewPagerItem
import com.wallpaper.base_app.activites.Dashboard
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
                R.drawable.group_272,
                getString(R.string.iphone_wallpaper),
                getString(R.string.explore_stunning_iphone_wallpaper_to_n_personalize_your_screen_effortlessly)
            ),
            ViewPagerItem(
                R.drawable.iphone_screen3,
                getString(R.string.HdWallpapers),
                getString(R.string.Discoverhighqualitywallpapersforastunningscreenexperience)
            ),
            ViewPagerItem(
                R.drawable.iphone_screen2,
                getString(R.string.iPhonrRindtones),
                getString(R.string.EnjoythebestiPhoneringtonesforapremiumsoundexperience)
            )
        )

        val adapter = ViewPagerAdapter(items)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text = if (position == items.lastIndex) "Start" else "Next"
            }
        })
    }


    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            if (nextItem < 3) {
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
        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

}
