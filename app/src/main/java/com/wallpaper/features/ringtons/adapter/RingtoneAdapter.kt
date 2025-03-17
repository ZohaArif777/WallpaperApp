package com.wallpaper.features.ringtons.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.wallpaper.R
import com.wallpaper.databinding.RingtoneListBinding
import com.wallpaper.features.data_class.RingtonesModel
import com.wallpaper.features.ringtons.RingtonePlayer

class RingtoneAdapter(
    private val context: Context,
    private val ringtoneList: List<RingtonesModel>,
    private val soundType: String
) : RecyclerView.Adapter<RingtoneAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RingtoneListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RingtoneListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ringtone = ringtoneList[position]
        with(holder.binding) {
            text.text = ringtone.name
            size.text = ringtone.size
            duration.text = ringtone.duration


            text.setOnClickListener {
                val intent = Intent(context, RingtonePlayer::class.java).apply {
                    putExtra("RINGTONE_NAME", ringtone.name)
                    putExtra("RINGTONE_SIZE", ringtone.size)
                    putExtra("RINGTONE_DURATION", ringtone.duration)
                    putExtra("RINGTONE_RES_ID", ringtone.rawResId)
                    putExtra("SOUND_TYPE", soundType)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = ringtoneList.size
}
