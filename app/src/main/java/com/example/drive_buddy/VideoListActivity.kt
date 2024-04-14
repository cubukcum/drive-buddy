package com.example.drive_buddy
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
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
        val videoList = getVideoListFromDirectory(videoDirectoryPath)

        videoListAdapter = VideoListAdapter(videoList, this)
        recyclerView.adapter = videoListAdapter
    }

    override fun onItemClick(videoUri: String) {
        print("buraya geldi empty")
        playVideo(Uri.parse(videoUri))
    }

    private fun playVideo(videoUri: Uri) {
        print("buraya geldi empty")
        // ExoPlayer ile videoyu oynatma kodu buraya gelecek
        // Örnek kod ExoPlayer kullanarak video oynatma bölümündeki kod ile benzer olacaktır
        Toast.makeText(this, "Playing video: $videoUri", Toast.LENGTH_SHORT).show()
    }

    private fun getVideoListFromDirectory(directoryPath: String): List<String> {
        print("buraya geldi empty")
        // Verilen dizindeki video dosyalarını listeleme kodu buraya gelecek
        // Dizindeki video dosyalarını bir listeye ekleyin ve döndürün
        return emptyList() // Örnek olarak boş bir liste döndürüldü
    }
}
