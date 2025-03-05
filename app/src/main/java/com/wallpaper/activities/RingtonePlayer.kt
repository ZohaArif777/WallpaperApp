package com.wallpaper.activities

import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.wallpaper.R
import com.wallpaper.databinding.ActivityRingtonePlayerBinding
import java.util.concurrent.TimeUnit

class RingtonePlayer : AppCompatActivity() {

    private lateinit var binding: ActivityRingtonePlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var currentSoundType = ""

    private val ringtoneList = listOf(
        R.raw.ringtone1,
        R.raw.ringtone2,
        R.raw.ringtone3,
        R.raw.ringtone4,
        R.raw.ringtone5,
        R.raw.ringtone6,
        R.raw.ringtone6,
        R.raw.ringtone8,
        R.raw.ringtone9,
        R.raw.ringtone10
    )

    private val notificationList = listOf(
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

    private var currentIndex: Int = 0
    private var selectedList: List<Int> = ringtoneList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRingtonePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ringtoneName = intent.getStringExtra("RINGTONE_NAME")
        val ringtoneResId = intent.getIntExtra("RINGTONE_RES_ID", -1)
        val soundType = intent.getStringExtra("SOUND_TYPE") ?: "ringtone"

        selectedList = if (soundType == "notification") notificationList else ringtoneList
        currentIndex = selectedList.indexOf(ringtoneResId).takeIf { it != -1 } ?: 0

        updateSoundName()

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

        binding.btnRingtone.setOnClickListener { showSetAsDialog("Ringtone") }
        binding.btnNotification.setOnClickListener { showSetAsDialog("Notification") }
        binding.btnAlarm.setOnClickListener { showSetAsDialog("Alarm") }
    }

    private fun togglePlayPause() {
        if (mediaPlayer == null) {
            playSound()
        } else {
            mediaPlayer?.let {
                try {
                    if (it.isPlaying) {
                        it.pause()
                        binding.btnPlayPause.setImageResource(R.drawable.play_button)
                    } else {
                        it.start()
                        updateSeekBar()
                        binding.btnPlayPause.setImageResource(R.drawable.pause)
                    }
                } catch (e: IllegalStateException) {
                    restartMediaPlayer()
                }
            }
        }
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

            handler.post(object : Runnable {
                override fun run() {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            binding.seekBar.progress = it.currentPosition
                            binding.startTime.text = formatTime(it.currentPosition)
                            handler.postDelayed(this, 500)
                        }
                    }
                }
            })
        }
    }

    private fun resetSeekBar() {
        binding.seekBar.progress = 0
        binding.startTime.text = "00:00"
        binding.btnPlayPause.setImageResource(R.drawable.play_button)
    }

    private fun restartMediaPlayer() {
        stopAndReleaseMediaPlayer()
        playSound()
    }

    private fun stopAndReleaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAndReleaseMediaPlayer()
    }

    private fun showSetAsDialog(type: String) {
        currentSoundType = type
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set as $type")
        builder.setMessage("Are you sure you want to set this sound as your $type?")

        builder.setPositiveButton("Yes") { _, _ ->
            if (Settings.System.canWrite(this)) {
                setSoundAs(type)
            } else {
                requestModifySettingsPermission()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun requestModifySettingsPermission() {
        Toast.makeText(this, "Please allow system settings modification", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (Settings.System.canWrite(this)) {
            setSoundAs(currentSoundType)
        }
    }

    private fun setSoundAs(type: String) {
        val soundUri = Uri.parse("android.resource://$packageName/${selectedList[currentIndex]}")
        RingtoneManager.setActualDefaultRingtoneUri(
            this, when (type) {
                "Ringtone" -> RingtoneManager.TYPE_RINGTONE
                "Notification" -> RingtoneManager.TYPE_NOTIFICATION
                "Alarm" -> RingtoneManager.TYPE_ALARM
                else -> return
            }, soundUri
        )

        Toast.makeText(this, "$type set successfully!", Toast.LENGTH_SHORT).show()
    }
}
