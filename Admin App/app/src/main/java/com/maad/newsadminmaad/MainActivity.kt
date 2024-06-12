package com.maad.newsadminmaad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.maad.newsadminmaad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var articles = mutableListOf<Article>()
    private lateinit var adapter: ArticleAdapter

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

        binding.addFab.setOnClickListener {
            startActivity(Intent(this, DetailsActivity::class.java))
        }

        prepareSwipeToDelete()

    }

    override fun onResume() {
        super.onResume()
        getFirebaseArticles()
    }

    private fun getFirebaseArticles() {
        Firebase.firestore
            .collection("articles")
            .get()
            .addOnSuccessListener {
                binding.loadingProgress.isVisible = false
                articles = it.toObjects(Article::class.java)
                adapter = ArticleAdapter(this, articles)
                binding.newsRv.adapter = adapter
            }
    }

    private fun prepareSwipeToDelete() {
        val touchCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                binding.loadingProgress.isVisible = true
                val position = viewHolder.getAdapterPosition()
                deleteDocument(position)
            }

        }
        ItemTouchHelper(touchCallback).attachToRecyclerView(binding.newsRv)

    }

    private fun deleteDocument(position: Int) {
        Firebase.firestore
            .collection("articles")
            .document(articles[position].id)
            .delete()
            .addOnSuccessListener {
                deleteImage(articles[position].pic, position)
            }
    }

    private fun deleteImage(imageUrL: String, position: Int) {
        Firebase.storage
            .getReferenceFromUrl(imageUrL)
            .delete()
            .addOnSuccessListener {
                articles.removeAt(position)
                adapter.notifyItemRemoved(position)
                binding.loadingProgress.isVisible = false
                Toast.makeText(this@MainActivity, "Deleted!", Toast.LENGTH_SHORT).show()
            }
    }

}