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
    private var _binding: FragmentApplyRingtoneBinding? = null
    private val binding get() = _binding!!
    private val ringtoneList = mutableListOf<Ringtones>()
    private lateinit var ringtoneAdapter: RingtoneAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            R.raw.ringtone1, R.raw.ringtones2, R.raw.ringtones3,
            R.raw.ringtone4, R.raw.ringtone5, R.raw.ringtones6,
            R.raw.ringtone7, R.raw.ringtone8, R.raw.ringtone9,
            R.raw.ringtone10
        )

        ringtoneList.clear()
        ringtoneList.addAll(rawResources.mapNotNull { resId -> getRingtoneInfo(resId) })
        ringtoneAdapter.notifyDataSetChanged()
    }

    private fun getRingtoneInfo(resId: Int): Ringtones? {
        return try {
            val context = requireContext()
            val ringtoneName = resources.getResourceEntryName(resId)
            val ringtoneUri = Uri.parse("android.resource://${context.packageName}/$resId")

            val durationMs = getAudioDuration(context, ringtoneUri)
            val formattedDuration = formatDuration(durationMs)
            val fileSize = getRawFileSize(context, resId)

            Ringtones(ringtoneName, fileSize, formattedDuration, resId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getAudioDuration(context: Context, uri: Uri): Long {
        return MediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(context, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
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
