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
import edu.uoc.pac3.data.streams.Cursor
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsListAdapter
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.oauth.OAuthActivity
import io.ktor.client.request.*
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var streamListAdapter: StreamsListAdapter
    private lateinit var cursorPagination: String // Cursor pagination
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var streams: MutableList<Stream> // List of Streams


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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Init Cursor Pagination
        cursorPagination = ""

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

        var cursor: Cursor // Cursor pagination


        GlobalScope.launch {
            // DownLoading Streams and Cursor
            val response = loadStreams()

//            streams = response?.data as MutableList<Stream>
            streams.addAll(response?.data as MutableList<Stream>)
            cursor = response?.pagination as Cursor
            cursorPagination = cursor.toString()

            Log.i("STREAMS", streams.toString())
            Log.i("CURSOR", cursor.toString())

            // Loading Streams in RecyclerView
            runOnUiThread {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                streamListAdapter = StreamsListAdapter(streams)
//                layoutManager.onAdapterChanged(recyclerView.adapter, streamListAdapter)
                recyclerView.adapter = streamListAdapter

            }
        }

    }

    // Load Streams
    private suspend fun loadStreams(): StreamsResponse? {
        return TwitchApiService(Network.createHttpClient(this)).getStreams(cursorPagination)
    }


    // Load Next 20 Streams
    private fun getNextStreams() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                getStreams()

                val itemCount = recyclerView.layoutManager?.itemCount
                val childCount = recyclerView.layoutManager?.childCount

                val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val a = layoutManager.childCount
                val itemCountAdapter = recyclerView.adapter?.itemCount
                val b = recyclerView.childCount


//                Log.i("ITEM COUNT", itemCount.toString())
//                Log.i("CHILD COUNT", childCount.toString())
//                Log.i("LAST VISIBLE ITEM", lastVisibleItem.toString())
//                Log.i("A", a.toString())
//                Log.i("ITEM COUNT ADAPTER", itemCountAdapter.toString())
//                Log.i("B", b.toString())
//                Log.i("swipeRefreshLayout", swipeRefreshLayout.childCount.toString())
//                Log.i("CURSOR", cursorPagination)
                if (itemCountAdapter?.minus(lastVisibleItem) == 1) {
                    recyclerView.post {
                        getStreams()

                    }
                }
//                Log.i("DX", dx.toString())
//                Log.i("DY", dy.toString())
//                Log.i("T", t.toString())


            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                getStreams()
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