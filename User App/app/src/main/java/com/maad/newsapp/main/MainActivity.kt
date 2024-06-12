package com.maad.newsapp.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.maad.newsapp.R
import com.maad.newsapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadNews()

        val adRequest = AdRequest.Builder().build()
        binding.bannerAd.loadAd(adRequest)

    }

    private fun loadNews() {

        val cat = intent.getStringExtra("cat")!!
        val code = getSharedPreferences("settings", MODE_PRIVATE)
            .getString("code", "us")!!

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val callable = retrofit.create(NewsCallable::class.java)
        callable.getNews(cat, code).enqueue(object : Callback<News> {
            override fun onResponse(p0: Call<News>, p1: Response<News>) {
                val articles = p1.body()!!.articles
                //Log.d("trace", "Data: ${articles[0].urlToImage}")
                val adapter = ArticleAdapter(this@MainActivity, articles)
                binding.newsRv.adapter = adapter
                binding.progress.visibility = View.GONE
            }

            override fun onFailure(p0: Call<News>, p1: Throwable) {
                Log.d("trace", "Error: ${p1.localizedMessage}")
            }
        })

    }

}
