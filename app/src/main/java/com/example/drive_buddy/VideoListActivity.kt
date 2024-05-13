package com.example.drive_buddy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class VideoListActivity : AppCompatActivity(), VideoListAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoListAdapter: VideoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        recyclerView = findViewById(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val videoDirectoryPath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}"
        print(videoDirectoryPath)
        val videoList = getVideoListFromDirectory(videoDirectoryPath)
        videoListAdapter = VideoListAdapter(videoList, this)
        recyclerView.adapter = videoListAdapter
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
        finish()
    }


    override fun onItemClick(videoUri: String) {
        playVideo(Uri.parse(videoUri))
    }

    private fun playVideo(videoUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, videoUri)
        intent.setDataAndType(videoUri, "video/*")
        startActivity(intent)
    }



    @SuppressLint("Range")
    fun getVideoListFromDirectory(directoryPath: String): List<String> {
        val videoList = mutableListOf<String>()
        val contentResolver = this.contentResolver
        val selection = "${MediaStore.Video.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("$directoryPath%")
        val projection = arrayOf(MediaStore.Video.Media.DATA)

        val cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                videoList.add(videoPath)
            }
            cursor.close()
        }

        return videoList.reversed()
    }



}
