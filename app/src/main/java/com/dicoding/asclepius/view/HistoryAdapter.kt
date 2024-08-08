package com.dicoding.asclepius.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.History
import java.text.NumberFormat

class HistoryAdapter(private var historyList: ArrayList<History>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val historyImg: ImageView = item.findViewById(R.id.imageView)
        val historyLabel: TextView = item.findViewById(R.id.tv_item_title)
        val historyScore: TextView = item.findViewById(R.id.tv_item_description)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = historyList[position]
        holder.historyImg.setImageURI(Uri.parse(history.uri))
        holder.historyLabel.text = holder.itemView.context.getString(R.string.label, history.label)
        holder.historyScore.text = holder.itemView.context.getString(
            R.string.score,
            NumberFormat.getPercentInstance().format(history.confidence).toString()
        )
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}