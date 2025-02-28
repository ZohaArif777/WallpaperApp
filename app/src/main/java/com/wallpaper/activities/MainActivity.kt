package com.wallpaper.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.wallpaper.R
import com.wallpaper.adapter.FragmentAdapter
import com.wallpaper.databinding.ActivityMainBinding
import com.wallpaper.fragments.HdRingtones
import com.wallpaper.fragments.HdWallpaper
import com.wallpaper.fragments.Ringtones
import com.wallpaper.fragments.Wallpaper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = listOf(
            Wallpaper(),
            HdWallpaper(),
            Ringtones(),
            HdRingtones()
        )

        val adapter = FragmentAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, _ ->
            tab.customView = LayoutInflater.from(this).inflate(R.layout.custom_layout, null)
        }.attach()

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < fragments.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            }
        }

        binding.btnSkip.setOnClickListener {
            val intent=Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == fragments.size - 1) {
                    binding.btnNext.text = "Done"
                } else {
                    binding.btnNext.text = "Next"
                }
            }
        })
    }
}
