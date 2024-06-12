package com.maad.newsadminmaad

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maad.newsadminmaad.databinding.ArticleListItemBinding

class ArticleAdapter(val a: Activity, val articles: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleVH>() {

    class ArticleVH(val binding: ArticleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        val binding = ArticleListItemBinding.inflate(a.layoutInflater, parent, false)
        return ArticleVH(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        holder.binding.articleText.text = articles[position].title
        Glide
            .with(holder.binding.articleIv.context)
            .load(articles[position].pic)
            .into(holder.binding.articleIv)
    }


}