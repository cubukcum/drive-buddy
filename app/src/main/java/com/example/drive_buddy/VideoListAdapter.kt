package com.example.drive_buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VideoListAdapter(private val videoList: List<String>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(videoUri: String)
    }
    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.videoTitle)
        fun bind(videoUri: String) {
            val videoFile = File(videoUri)
            val lastModified = videoFile.lastModified()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(lastModified))
            textViewTitle.text = formattedDate
            textViewTitle.setTextColor(itemView.resources.getColor(R.color.yellow))
            itemView.setOnClickListener {
                listener.onItemClick(videoUri)
            }}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoUri = videoList[position]
        holder.bind(videoUri)
    }


    override fun getItemCount(): Int {
        return videoList.size
    }
}
