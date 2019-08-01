package com.example.newsapp.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.testsettings.*
import android.content.SharedPreferences
import android.content.Context
import android.widget.Switch
import android.widget.ToggleButton
import com.example.newsapp.Adapter.ArticlesAdapter
import com.example.newsapp.R

class Settings : AppCompatActivity(),SharedPreferences.OnSharedPreferenceChangeListener{
       private lateinit var toggle: ToggleButton
    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        var editor = mPreferences?.edit()


        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ArticlesAdapter.sharedPreferenceAnimation = true

                editor?.putBoolean("isChecked", true);
                editor?.apply();

            } else {
                ArticlesAdapter.sharedPreferenceAnimation = false
                editor?.putBoolean("isChecked", false);
                editor?.apply();

            }

        }
    }

    private var mPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testsettings)
        setSupportActionBar(toolbar)
        toggle = findViewById(R.id.toggleButton)

        var editor = mPreferences?.edit()
         val switch = findViewById<Switch>(R.id.switch1)
        val sharedPrefs = getSharedPreferences("com.example.newsapp", Context.MODE_PRIVATE)
        switch1.setChecked(sharedPrefs.getBoolean("switch1", true))
        switch.setOnCheckedChangeListener { _, isChecked ->


            editor?.putBoolean("isChecked", isChecked);
            editor?.apply();

        }


        mPreferences = getSharedPreferences("filters", MODE_PRIVATE);


        toggle.setOnCheckedChangeListener { _, isChecked ->

                editor?.putBoolean("isChecked", isChecked);
                editor?.apply();

            }


    }
}