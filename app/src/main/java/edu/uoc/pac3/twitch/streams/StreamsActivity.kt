package edu.uoc.pac3.twitch.streams

import android.os.Bundle
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

    private lateinit var adapter: StreamsListAdapter

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
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Init Adapter
        adapter = StreamsListAdapter(mutableListOf())
    }


    private fun getStreams() {

        var streams: MutableList<Stream> = mutableListOf()

        GlobalScope.launch {
            streams = loadStreams()?.data as MutableList<Stream>

//            runOnUiThread {
//                adapter = StreamsListAdapter(streams)
//            }
        }

//        adapter = StreamsListAdapter(streams)
        adapter.apply { StreamsListAdapter(streams) }


    }

    private suspend fun loadStreams(): StreamsResponse? {
        var streamsResponse: StreamsResponse? = null

        return TwitchApiService(Network.createHttpClient(this)).getStreams()


//        return streamsResponse
    }

}