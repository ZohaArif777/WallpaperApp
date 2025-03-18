package com.wallpaper.features.ringtons

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.wallpaper.R
import com.wallpaper.databinding.ActivityRingtonePlayerBinding
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class RingtonePlayer : AppCompatActivity() {

    private lateinit var binding: ActivityRingtonePlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private var currentIndex: Int = 0
    private var selectedList: List<Int> = listOf()

    private val ringtoneList = listOf(
        R.raw.vintage_telephone,
        R.raw.urgent_simple_tone,
        R.raw.old_ringtone,
        R.raw.telephone_ring,
        R.raw.on_hold_ringtone,
        R.raw.waiting_ringtone,
        R.raw.music_ringtone,
        R.raw.office_ringtone,
        R.raw.toy_telephone
    )

    private val notificationList = listOf(
        R.raw.bell_notification,
        R.raw.sci_fi_reject,
        R.raw.positive_notification,
        R.raw.software_interface_remove,
        R.raw.magic_marimba,
        R.raw.tile_game_reveal,
        R.raw.sci_fi_reject,
        R.raw.magic_drop,
        R.raw.door_bell
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingtonePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val ringtoneName = intent.getStringExtra("RINGTONE_NAME")
        val ringtoneResId = intent.getIntExtra("RINGTONE_RES_ID", -1)
        val soundType = intent.getStringExtra("SOUND_TYPE") ?: "ringtone"

        selectedList = if (soundType == "notification") notificationList else ringtoneList
        currentIndex = selectedList.indexOf(ringtoneResId).takeIf { it != -1 } ?: 0

        updateSoundName()
        binding.textName.text = ringtoneName

        binding.btnPlayPause.setOnClickListener { togglePlayPause() }
        binding.btnNext.setOnClickListener { playNextSound() }
        binding.btnBack.setOnClickListener { playPreviousSound() }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        playSound()

        binding.btnRingtone.setOnClickListener { setRingtoneDirectly() }
        binding.btnNotification.setOnClickListener { setSoundAs(RingtoneManager.TYPE_NOTIFICATION) }
        binding.btnAlarm.setOnClickListener { setSoundAs(RingtoneManager.TYPE_ALARM) }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                binding.btnPlayPause.setImageResource(R.drawable.play_button)
            } else {
                it.start()
                updateSeekBar()
                binding.btnPlayPause.setImageResource(R.drawable.pause)
            }
        } ?: playSound()
    }

    private fun playSound() {
        stopAndReleaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(this, selectedList[currentIndex]).apply {
            setOnCompletionListener { resetSeekBar() }
            start()
        }
        updateSeekBar()
        binding.btnPlayPause.setImageResource(R.drawable.pause)
    }

    private fun playNextSound() {
        if (currentIndex < selectedList.size - 1) {
            currentIndex++
            playSound()
            updateSoundName()
        }
    }

    private fun playPreviousSound() {
        if (currentIndex > 0) {
            currentIndex--
            playSound()
            updateSoundName()
        }
    }

    private fun updateSoundName() {
        val resourceId = selectedList[currentIndex]
        val soundName = resources.getResourceEntryName(resourceId).replace("_", " ")
        binding.textName.text = soundName
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { player ->
            binding.seekBar.max = player.duration
            binding.endTime.text = formatTime(player.duration)
        }
    }

    private fun resetSeekBar() {
        binding.seekBar.progress = 0
        binding.startTime.text = "00:00"
        binding.btnPlayPause.setImageResource(R.drawable.play_button)
    }

    private fun stopAndReleaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun getCacheAudioUri(resId: Int): Uri? {
        val fileName = "${resources.getResourceEntryName(resId)}.mp3"
        val cacheFile = File(cacheDir, fileName)

        if (!cacheFile.exists()) {
            try {
                val inputStream: InputStream = resources.openRawResource(resId)
                val outputStream = FileOutputStream(cacheFile)
                inputStream.copyTo(outputStream)
                outputStream.close()
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        return FileProvider.getUriForFile(this, "$packageName.provider", cacheFile)
    }

    private fun setRingtoneDirectly() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            requestWriteSettingsPermission()
            return
        }

        val uri = getCacheAudioUri(selectedList[currentIndex]) ?: return
        RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, uri)
        showSuccessDialog("Ringtone")
    }

    private fun setSoundAs(type: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(this)) {
            requestWriteSettingsPermission()
            return
        }

        val uri = getCacheAudioUri(selectedList[currentIndex]) ?: return
        RingtoneManager.setActualDefaultRingtoneUri(this, type, uri)

        val typeString = when (type) {
            RingtoneManager.TYPE_NOTIFICATION -> "Notification"
            RingtoneManager.TYPE_ALARM -> "Alarm"
            else -> "Ringtone"
        }
        showSuccessDialog(typeString)
    }

    private fun requestWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        Toast.makeText(this, "Please enable Modify System Settings", Toast.LENGTH_LONG).show()
    }

    private fun showSuccessDialog(type: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_done)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnClose)
        val textMessage = dialog.findViewById<TextView>(R.id.successMessage)

        textMessage.text = getString(R.string.set_sound_success, type)
        btnClose.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}
