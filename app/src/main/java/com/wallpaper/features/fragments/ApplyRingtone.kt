package com.wallpaper.features.fragments

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
import com.wallpaper.features.ringtons.adapter.RingtoneAdapter
import com.wallpaper.databinding.FragmentApplyRingtoneBinding
import com.wallpaper.features.data_class.RingtonesModel

class ApplyRingtone : Fragment() {
    private var _binding: FragmentApplyRingtoneBinding? = null
    private val binding get() = _binding!!
    private val ringtoneList = mutableListOf<RingtonesModel>()
    private lateinit var ringtoneAdapter: RingtoneAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApplyRingtoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ringtoneAdapter = RingtoneAdapter(requireContext(), ringtoneList, "ringtone")
        binding.recyRingtone.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ringtoneAdapter
        }
        loadRingtones()
    }

    private fun loadRingtones() {
        val rawResources = listOf(
            R.raw.vintage_telephone, R.raw.urgent_simple_tone, R.raw.old_ringtone,
            R.raw.telephone_ring, R.raw.on_hold_ringtone, R.raw.waiting_ringtone,
            R.raw.waiting_ringtone, R.raw.music_ringtone, R.raw.office_ringtone,
            R.raw.toy_telephone
        )

        ringtoneList.clear()
        ringtoneList.addAll(rawResources.mapNotNull { resId -> getRingtoneInfo(resId) })
        ringtoneAdapter.notifyDataSetChanged()
    }

    private fun getRingtoneInfo(resId: Int): RingtonesModel? {
        return try {
            val context = requireContext()
            val ringtoneName = resources.getResourceEntryName(resId)
            val ringtoneUri = Uri.parse("android.resource://${context.packageName}/$resId")

            val durationMs = getAudioDuration(context, ringtoneUri)
            val formattedDuration = formatDuration(durationMs)
            val fileSize = getRawFileSize(context, resId)

            RingtonesModel(ringtoneName, fileSize, formattedDuration, resId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getAudioDuration(context: Context, uri: Uri): Long {
        return MediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(context, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
                ?: 0L
        }
    }

    private fun formatDuration(durationMs: Long): String {
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    private fun getRawFileSize(context: Context, resId: Int): String {
        return try {
            context.resources.openRawResource(resId).use { inputStream ->
                "${inputStream.available() / 1024} KB"
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
