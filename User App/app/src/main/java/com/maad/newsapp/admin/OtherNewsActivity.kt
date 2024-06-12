package com.maad.newsapp.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maad.newsapp.R
import com.maad.newsapp.databinding.ActivityOtherNewsBinding

class OtherNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtherNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getAdminNews()

    }

    private fun getAdminNews() {
        Firebase.firestore.collection("articles")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val articles = snapshot.toObjects(AdminArticle::class.java)
                    val adapter = OtherNewsAdapter(this, articles)
                    binding.otherNewsRv.adapter = adapter
                    binding.progress.isVisible = false
                }
            }
    }


}