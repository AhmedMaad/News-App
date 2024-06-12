package com.maad.newsapp.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maad.newsapp.R
import com.maad.newsapp.databinding.ArticleListItemBinding

class ArticleAdapter(val a: Activity, val articles: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleVH>() {

    class ArticleVH(val binding: ArticleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        val binding = ArticleListItemBinding.inflate(a.layoutInflater, parent, false)
        return ArticleVH(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        Log.d("trace", "Link: ${articles[position].urlToImage}")
        holder.binding.articleText.text = articles[position].title
        Glide
            .with(holder.binding.articleIv.context)
            .load(articles[position].urlToImage)
            .error(R.drawable.broken_image)
            .into(holder.binding.articleIv)

        holder.binding.articleContainer.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, articles[position].url.toUri())
            a.startActivity(i)
        }

    }


}