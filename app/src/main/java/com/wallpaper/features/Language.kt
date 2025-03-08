package com.wallpaper.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.adapter.LanguageAdapter
import com.wallpaper.databinding.ActivityLanguageBinding

class Language : AppCompatActivity() {
    private lateinit var binding: ActivityLanguageBinding
    private lateinit var languageAdapter: LanguageAdapter
    private var selectedLanguage = "English"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val languages = listOf(
            "English",
            "Chinese",
            "Russian",
            "Hindi",
            "Spanish",
            "French",
            "Arabic",
            "Portuguese",
            "Indonesian",
            "Italian",
            "German"
        )

        languageAdapter = LanguageAdapter(languages, selectedLanguage) { selected ->
            selectedLanguage = selected
        }

        binding.languageRecycler.apply {
            layoutManager = LinearLayoutManager(this@Language)
            adapter = languageAdapter
        }
    }
}
