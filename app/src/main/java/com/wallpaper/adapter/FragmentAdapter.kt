package com.wallpaper.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wallpaper.fragments.HdRingtones
import com.wallpaper.fragments.HdWallpaper
import com.wallpaper.fragments.Ringtones
import com.wallpaper.fragments.Wallpaper

class FragmentAdapter (private val fragment: List<Fragment>, fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Wallpaper()
            1 -> HdWallpaper()
            2 -> Ringtones()
            else -> HdRingtones()
        }
    }
}