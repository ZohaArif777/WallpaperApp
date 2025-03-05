package com.wallpaper.fragments

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.R
import com.wallpaper.adapter.RingtoneAdapter
import com.wallpaper.databinding.FragmentNotificationBinding
import com.wallpaper.dataclass.Ringtones

class Notification : Fragment() {
    private lateinit var ringtoneAdapter: RingtoneAdapter
    private val notificationList = mutableListOf<Ringtones>()
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ringtoneAdapter = RingtoneAdapter(requireContext(), notificationList, "notification")
        binding.recyNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.recyNotification.adapter = ringtoneAdapter

        loadNotifications()
    }

    private fun loadNotifications() {
        val rawResources = listOf(
            R.raw.notification1,
            R.raw.notification8,
            R.raw.notification3,
            R.raw.notification4,
            R.raw.notification4,
            R.raw.notification6,
            R.raw.notification7,
            R.raw.notification8,
            R.raw.notification9,
            R.raw.notification10
        )

        rawResources.forEach { resId ->
            try {
                val notificationName = resources.getResourceEntryName(resId)
                val notificationUri = Uri.parse("android.resource://${requireContext().packageName}/$resId")

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(requireContext(), notificationUri)

                val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                val durationFormatted = formatDuration(durationMs)

                val fileSize = getRawFileSize(requireContext(), resId)
                notificationList.add(Ringtones(notificationName, fileSize, durationFormatted, resId))

                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        ringtoneAdapter.notifyDataSetChanged()
    }

    private fun formatDuration(durationMs: Long): String {
        val seconds = (durationMs / 1000) % 60
        val minutes = (durationMs / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getRawFileSize(context: Context, resId: Int): String {
        return try {
            context.resources.openRawResource(resId).use { inputStream ->
                val sizeInKB = inputStream.available() / 1024
                "$sizeInKB KB"
            }
        } catch (e: Exception) {
            "Unknown Size"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
