package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.streams.Cursor
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsListAdapter
import edu.uoc.pac3.data.streams.StreamsResponse
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var streamListAdapter: StreamsListAdapter
    private var cursorPagination: String? = null // Cursor pagination
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var streams: MutableList<Stream> // List of Streams
    private lateinit var recyclerView: RecyclerView // RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()

        // TODO: Get Streams
        getStreams()

        getNextStreams()

        // Configure SwipeRefreshLayout
        refreshSwipeStreams()
    }

    // Init RecyclerView
    private fun initRecyclerView() {

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

//        // Init Cursor Pagination
//        cursorPagination = ""

        // Init Streams
        streams = mutableListOf()

        // Set Layout Manager
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Init Adapter
        streamListAdapter = StreamsListAdapter(mutableListOf())
        recyclerView.adapter = streamListAdapter

    }


    private fun getStreams() {

        GlobalScope.launch {

            val response = loadStreams()  // DownLoading Data Streams and Pagination
            val data = response?.data as MutableList<Stream> // Data streams
            val pagination = response?.pagination as Cursor // Cursor pagination

            streams.addAll(data) // Add data streams to stream list
            cursorPagination = pagination.cursor

            Log.i("STREAMS", streams.toString())
            Log.i("CURSOR", pagination.toString())
            Log.i("ITEM-COUNT-NEW","${streamListAdapter.itemCount}")

            // Loading Streams in RecyclerView
            runOnUiThread {
                streamListAdapter.setStreams(streams)
            }

        }

    }

    // Load Streams
    private suspend fun loadStreams(): StreamsResponse? {
        return TwitchApiService(Network.createHttpClient(this)).getStreams(cursorPagination)
    }


    // Load Next OAuthConstants.FIRST Streams
    private fun getNextStreams() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val itemCountAdapter = recyclerView.adapter?.itemCount

                if (itemCountAdapter?.minus(lastVisibleItem) == 1) {
                    recyclerView.post {
                        getStreams()
                    }
                }

            }

        })
    }

    // Refresh SwipeRefreshLayout with Streams
    private fun refreshSwipeStreams() {
        swipeRefreshLayout.setOnRefreshListener {
            getStreams()
            swipeRefreshLayout.isRefreshing = false
        }
    }

}