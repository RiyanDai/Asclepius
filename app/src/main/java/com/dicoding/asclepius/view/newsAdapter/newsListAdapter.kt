package com.dicoding.asclepius.view.newsAdapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.model.newsData
import com.squareup.picasso.Picasso

class newsListAdapter(private val newsItems: List<newsData>) : RecyclerView.Adapter<newsListAdapter.viewHolder>() {

    inner class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.news_title)
        val content: TextView = itemView.findViewById(R.id.news_content)
        val image: ImageView = itemView.findViewById(R.id.news_image)
        val publishedAt: TextView = itemView.findViewById(R.id.news_publishedAt)

        fun bind(newsItem: newsData) {
            title.text = newsItem.title
            content.text = newsItem.content
            publishedAt.text = newsItem.publishedAt
            Picasso.get().load(newsItem.urlToImage).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val newsItem = newsItems[position]
        holder.bind(newsItem)

        // Handle click on card view
        holder.itemView.setOnClickListener {
            val context: Context = holder.itemView.context
            val url = newsItem.url  // Assuming 'url' field holds the news website URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }
}
