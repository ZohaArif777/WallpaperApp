package com.wallpaper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wallpaper.databinding.LanguageListBinding

class LanguageAdapter(
    private val languages: List<String>,
    private var selectedLanguage: String,
    private val onLanguageSelected: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(private val binding: LanguageListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(language: String) {
            binding.languageName.text = language
            binding.radioButton.isChecked = language == selectedLanguage


            binding.root.setOnClickListener {
                selectedLanguage = language
                onLanguageSelected(language)
                notifyDataSetChanged()
            }

            binding.radioButton.setOnClickListener {
                selectedLanguage = language
                onLanguageSelected(language)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding =
           LanguageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int = languages.size
}
