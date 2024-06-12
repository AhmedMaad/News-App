package com.maad.newsapp.admin

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maad.newsapp.R
import com.maad.newsapp.databinding.OtherNewsListItemBinding

class OtherNewsAdapter(val a: Activity, val articles: List<AdminArticle>) :
    RecyclerView.Adapter<OtherNewsAdapter.ArticleVH>() {

    class ArticleVH(val binding: OtherNewsListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        val binding = OtherNewsListItemBinding.inflate(a.layoutInflater, parent, false)
        return ArticleVH(binding)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        holder.binding.articleText.text = articles[position].title
        holder.binding.articleDescription.text = articles[position].desc
        Glide
            .with(holder.binding.image.context)
            .load(articles[position].pic)
            .placeholder(R.drawable.ic_downloading)
            .error(R.drawable.broken_image)
            .into(holder.binding.image)
    }


}