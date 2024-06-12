package com.maad.newsapp.settings

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maad.newsapp.R
import com.maad.newsapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.group.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.de_rb -> changeCountry("de")
                R.id.us_rb -> changeCountry("us")
            }
        }

    }

    private fun changeCountry(code: String) {
        val editor = getSharedPreferences("settings", MODE_PRIVATE).edit()
        editor.putString("code", code)
        editor.apply()
    }

}