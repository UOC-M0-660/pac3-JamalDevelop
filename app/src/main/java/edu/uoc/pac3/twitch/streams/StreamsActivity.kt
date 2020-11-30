package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsListAdapter
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.oauth.OAuthActivity
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var streamListAdapter: StreamsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()

        // TODO: Get Streams
        getStreams()

    }

    // Init RecyclerView
    private fun initRecyclerView() {

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Set Layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Init Adapter
        streamListAdapter = StreamsListAdapter(mutableListOf())
        recyclerView.adapter = streamListAdapter
    }


    private fun getStreams() {

        var streams: MutableList<Stream> = mutableListOf()

        GlobalScope.launch {
            // DownLoading Streams
            streams = loadStreams()?.data as MutableList<Stream>
            Log.i("STREAMS", streams.toString())

            // Loading Streams in RecyclerView
            runOnUiThread {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                streamListAdapter = StreamsListAdapter(streams)
                recyclerView.adapter = streamListAdapter
            }
        }

    }

    // Load Streams
    private suspend fun loadStreams(): StreamsResponse? {
        return TwitchApiService(Network.createHttpClient(this)).getStreams()
    }

}