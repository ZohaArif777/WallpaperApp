package com.wallpaper.base_app.localization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wallpaper.R
import com.wallpaper.base_app.language.Language
import com.wallpaper.databinding.ActivityLocalizationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalizationActivity : AppCompatActivity(), LocaleAdapter.OnItemsClickListener1 {
    private val binding by lazy { ActivityLocalizationBinding.inflate(layoutInflater) }

    private lateinit var adapter: LocaleAdapter
    private val localeList: ArrayList<LocaleAdapter.LocaleModel> = ArrayList()

    companion object {
        var selectedLanguage = "en"
        private const val fromKey = "from"
        fun startLocalizationActivity(context: Context, from: String) {
            context.startActivity(
                Intent(
                    context,
                    LocalizationActivity::class.java
                ).putExtra(fromKey, from).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

        fun setLocale(context: Context) {
            val getSavedKey = SharedPrefs.getPrefsString(
                context,
                SharedPrefs.languageKey,
                SharedPrefs.defaultLang
            )
            LocalizationActivity().setLocalLanguages(getSavedKey)
        }
    }
    private var fromActivity = "splash"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val tempLocaleList = countriesArrayList()
        tempLocaleList.sortBy { it.language }
        val selectedLang = SharedPrefs.getPrefsString(this, SharedPrefs.languageKey, SharedPrefs.defaultLang)
        lifecycleScope.launch {
            for (temp in tempLocaleList) {
                if (temp.langCode == selectedLang) {
                    localeList.add(
                        LocaleAdapter.LocaleModel(
                            temp.language,
                            temp.langCode,
                            true
                        )
                    )
                    SharedPrefs.setPrefsString(this@LocalizationActivity,temp.language,"selectedLanguageName")
                } else {
                    localeList.add(
                        LocaleAdapter.LocaleModel(
                            temp.language,
                            temp.langCode,
                            false
                        )
                    )
                }
            }
            withContext(Dispatchers.Main) {
                binding.recLanguage.layoutManager = LinearLayoutManager(this@LocalizationActivity)
                adapter = LocaleAdapter(this@LocalizationActivity, localeList, this@LocalizationActivity)
                binding.recLanguage.adapter = adapter
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSetLanguage.setOnClickListener {
            SharedPrefs.setPrefsString(this, selectedLanguage, SharedPrefs.languageKey)
            setLocalLanguages(selectedLanguage)

            val index = localeList.indexOfFirst {
                it.langCode.contains(selectedLanguage, ignoreCase = true)
            }

            Toast.makeText(
                this@LocalizationActivity,
                "${localeList[index].language} ${getString(R.string.LanguageApplySuccessfully)}",
                Toast.LENGTH_SHORT
            ).show()

            if (fromActivity == "splash") {
                SharedPrefs.setPrefsBoolean(this@LocalizationActivity, true, SharedPrefs.localizationShownKey)
                setResult(RESULT_OK)
                finish()
            } else {
                finish()
            }
        }

    }

    private fun countriesArrayList(): ArrayList<LocaleAdapter.LocaleModel> {
        val tempList = arrayListOf<LocaleAdapter.LocaleModel>()
        tempList.add(LocaleAdapter.LocaleModel("English", "en", false))
        tempList.add(LocaleAdapter.LocaleModel("Arabic", "ar", false))
        tempList.add(LocaleAdapter.LocaleModel("Czech", "cs", false))
        tempList.add(LocaleAdapter.LocaleModel("Finnish", "fi", false))
        tempList.add(LocaleAdapter.LocaleModel("French", "fr", false))
        tempList.add(LocaleAdapter.LocaleModel("German", "de", false))
        tempList.add(LocaleAdapter.LocaleModel("Greek", "el", false))
        tempList.add(LocaleAdapter.LocaleModel("Hindi", "hi", false))
        tempList.add(LocaleAdapter.LocaleModel("Hungarian", "hu", false))
        tempList.add(LocaleAdapter.LocaleModel("Indonesian", "in", false))
        tempList.add(LocaleAdapter.LocaleModel("Italian", "it", false))
        tempList.add(LocaleAdapter.LocaleModel("Japanese", "ja", false))
        tempList.add(LocaleAdapter.LocaleModel("Korean", "ko", false))
        tempList.add(LocaleAdapter.LocaleModel(  "Luxembourg","lb", false ))
        tempList.add(LocaleAdapter.LocaleModel("Norwegian", "no", false))
        tempList.add(LocaleAdapter.LocaleModel("Persian", "fa", false))
        tempList.add(LocaleAdapter.LocaleModel( "Polish", "pl", false))
        tempList.add(LocaleAdapter.LocaleModel("Portuguese", "pt", false))
        tempList.add(LocaleAdapter.LocaleModel( "Russian", "ru", false))
        tempList.add(LocaleAdapter.LocaleModel("Spanish", "es", false))
        tempList.add(LocaleAdapter.LocaleModel("Swedish", "sv", false))
        tempList.add(LocaleAdapter.LocaleModel("Turkish", "tr", false))
        tempList.add(LocaleAdapter.LocaleModel("Urdu", "ur", false))
        return tempList
    }

    fun setLocalLanguages(language: String) {
        val appLocale = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

    }

    override fun onItemClick(pos: Int) {
        selectedLanguage = localeList[pos].langCode
    }
}
