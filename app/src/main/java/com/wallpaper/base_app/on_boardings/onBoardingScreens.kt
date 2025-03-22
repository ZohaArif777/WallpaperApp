package com.wallpaper.base_app.on_boardings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.wallpaper.R
import com.wallpaper.base_app.on_boardings.adapter.ViewPagerItem
import com.wallpaper.base_app.activities.Dashboard
import com.wallpaper.base_app.localization.SharedPrefs
import com.wallpaper.base_app.on_boardings.adapter.OnBoardingAdapter
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

        val adapter = OnBoardingAdapter(items)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text = if (position == items.lastIndex)
                    getString(R.string.start)
                else
                    getString(R.string.next)
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
        SharedPrefs.setPrefsBoolean(this@OnBoardingScreens,false,SharedPrefs.firstTimeKey)
        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

}
