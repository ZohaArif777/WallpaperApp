package com.wallpaper.base_app.activities

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wallpaper.R
import com.wallpaper.base_app.setting.Setting
import com.wallpaper.databinding.ActivityDashboardBinding
import com.wallpaper.features.ringtons.Ringtone
import com.wallpaper.features.wallpapers.Wallpaper
import java.util.Objects

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var exitDialog: BottomSheetDialog? = null
    private var noInternetDialog: BottomSheetDialog? = null
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isInternetAvailable()) {
                noInternetDialog?.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        setupButtonListeners()
    }
    override fun onBackPressed() {
        showExitDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        exitDialog?.dismiss()
    }

    private fun setupButtonListeners() {
        binding.iphoneWallpaper.setOnClickListener {
            checkInternetAndNavigate(getString(R.string.iphone))
        }
        binding.hd.setOnClickListener {
            checkInternetAndNavigate(getString(R.string.hd))
        }
        binding.IosButton.setOnClickListener {
            checkInternetAndNavigate(getString(R.string.ios))
        }
        binding.samsung.setOnClickListener {
            checkInternetAndNavigate(getString(R.string.samsung))
        }
        binding.ringtonesButton.setOnClickListener {
            navigateToRingtone()
        }
        binding.setting.setOnClickListener {
            navigateToSetting()
        }
    }

    private fun checkInternetAndNavigate(category: String) {
        if (isInternetAvailable()) {
            navigateToWallpaper(category)
        } else {
            showNoInternetDialog()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    private fun showNoInternetDialog() {
        if (noInternetDialog?.isShowing == true) return
        val dialogView = layoutInflater.inflate(R.layout.dialog_no_internet, null)
        noInternetDialog = BottomSheetDialog(this).apply {
            setContentView(dialogView)
            setCancelable(true)
        }

        val btnClose = dialogView.findViewById<Button>(R.id.close)
        val btnInternetSettings = dialogView.findViewById<Button>(R.id.internet)

        btnClose.setOnClickListener { noInternetDialog?.dismiss() }
        btnInternetSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        noInternetDialog?.show()
    }

    private fun navigateToWallpaper(category: String) {
        val intent = Intent(this, Wallpaper::class.java)
        intent.putExtra("CATEGORY_NAME", category)
        startActivity(intent)
    }

    private fun navigateToSetting() {
        val intent = Intent(this, Setting::class.java)
        startActivity(intent)
    }

    private fun navigateToRingtone() {
        val intent = Intent(this, Ringtone::class.java)
        startActivity(intent)
    }

    private fun showExitDialog() {
        val dialog = BottomSheetDialog(this, R.layout.exit_dialog)
        dialog.setContentView(R.layout.exit_dialog)
        Objects.requireNonNull(dialog.window)?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val exitBtn = dialog.findViewById<Button>(R.id.btnExit)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)

        exitBtn?.visibility = View.VISIBLE
        cancelBtn?.visibility = View.VISIBLE
        cancelBtn?.setOnClickListener {
            dialog.dismiss()
        }
        exitBtn?.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        dialog.show()
    }

}
