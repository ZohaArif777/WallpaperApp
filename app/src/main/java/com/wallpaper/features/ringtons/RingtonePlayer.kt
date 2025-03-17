package com.wallpaper.features.ringtons

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
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
        R.raw.software_interface_remove,
        R.raw.magic_marimba,
        R.raw.tile_game_reveal,
        R.raw.sci_fi_reject,
        R.raw.magic_drop,
        R.raw.door_bell
    )

    private var currentIndex: Int = 0
    private var selectedList: List<Int> = ringtoneList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRingtonePlayerBinding.inflate(layoutInflater)
        binding.btn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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
        binding.textName.text = ringtoneName

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        playSound()

        binding.btnRingtone.setOnClickListener { setSoundAs("Ringtone") }
        binding.btnNotification.setOnClickListener { setSoundAs("Notification") }
        binding.btnAlarm.setOnClickListener { setSoundAs("Alarm") }
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

    override fun onResume() {
        super.onResume()
        if (Settings.System.canWrite(this)) {
            setSoundAs(currentSoundType)
        }
    }

    private fun checkAndRequestPermission(): Boolean {
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
            return false
        }
        return true
    }

    private fun setSoundAs(type: String) {
        if (!checkAndRequestPermission()) {
            return
        }

        val soundUri = Uri.parse("android.resource://$packageName/${selectedList[currentIndex]}")

        RingtoneManager.setActualDefaultRingtoneUri(
            this, when (type) {
                "Ringtone" -> RingtoneManager.TYPE_RINGTONE
                "Notification" -> RingtoneManager.TYPE_NOTIFICATION
                "Alarm" -> RingtoneManager.TYPE_ALARM
                else -> return
            }, soundUri
        )

        showSuccessDialog(type)
    }

    private fun showSuccessDialog(type: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_done)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setGravity(Gravity.CENTER)

        val btnClose = dialog.findViewById<ImageButton>(R.id.btnClose)
        val textMessage = dialog.findViewById<TextView>(R.id.successMessage)

        textMessage.text = "$type set successfully!"

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
