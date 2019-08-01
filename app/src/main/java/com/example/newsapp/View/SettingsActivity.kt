package com.example.newsapp.View

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.example.newsapp.R


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

    }

    class ArticlesPreferenceFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings_main)

            val filterBy: Preference = findPreference(getString(R.string.settings_filter_by_key))
            bindPreferenceSummaryToValue(filterBy)


            val orderBy: Preference = findPreference(getString(R.string.settings_order_by_key))
            bindPreferenceSummaryToValue(orderBy)

            val switch: Preference = findPreference("switch_preference_1")
            bindPreferenceSummaryToValue(switch)
        }

        override fun onPreferenceChange(preference: Preference, value: Any): Boolean {
            val stringValue: String = value.toString()

            if (preference is ListPreference) {
                val prefIndex: Int = preference.findIndexOfValue(stringValue)
                if (prefIndex >= 0) {
                    val labels = preference.entries
                    preference.setSummary(labels[prefIndex])
                }
            } else {
                preference.summary = stringValue
            }
            return true
        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
        preference.onPreferenceChangeListener = this
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.context)
        lateinit var rez: Any
        if (preference.key == "switch_preference_1")
        {       rez = preferences.getBoolean(preference.key, false)

        }
            else
                rez = preferences.getString(preference.key, "") as String

            onPreferenceChange(preference, rez)
        }
    }
}
