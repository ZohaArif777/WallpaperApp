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
import com.wallpaper.databinding.FragmentApplyRingtoneBinding
import com.wallpaper.dataclass.Ringtones

class ApplyRingtone : Fragment() {
    private lateinit var ringtoneAdapter: RingtoneAdapter
    private val ringtoneList = mutableListOf<Ringtones>()
    private var _binding: FragmentApplyRingtoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplyRingtoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ringtoneAdapter = RingtoneAdapter(requireContext(), ringtoneList, "ringtone")
        binding.recyRingtone.layoutManager = LinearLayoutManager(requireContext())
        binding.recyRingtone.adapter = ringtoneAdapter

        loadRingtones()
    }

    private fun loadRingtones() {
        val rawResources = listOf(
            R.raw.ringtone1,
            R.raw.ringtones2,
            R.raw.ringtones3,
            R.raw.ringtone4,
            R.raw.ringtone5,
            R.raw.ringtones6,
            R.raw.ringtones6,
            R.raw.ringtones2,
            R.raw.ringtone7,
            R.raw.ringtone8,
            R.raw.ringtone9,
            R.raw.ringtone10,

            )

        rawResources.forEach { resId ->
            try {
                val ringtoneName = resources.getResourceEntryName(resId)
                val ringtoneUri = Uri.parse("android.resource://${requireContext().packageName}/$resId")

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(requireContext(), ringtoneUri)

                val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                val durationFormatted = formatDuration(durationMs)

                val fileSize = getRawFileSize(requireContext(), resId)
                ringtoneList.add(Ringtones(ringtoneName, fileSize, durationFormatted, resId))

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
