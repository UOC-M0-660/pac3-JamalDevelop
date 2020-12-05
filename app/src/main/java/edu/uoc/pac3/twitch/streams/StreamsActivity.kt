package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.streams.Cursor
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamsListAdapter
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.twitch.profile.ProfileActivity
import io.ktor.client.features.*
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

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

        swipeRefreshLayout.isRefreshing = true

        // Run in background
        lifecycleScope.launch {

            val response: StreamsResponse?

            try {
                response = loadStreams()  // DownLoading Data Streams and Pagination
                val data = response?.data as MutableList<Stream> // Data streams
                val pagination = response?.pagination as Cursor // Cursor pagination

                streams.addAll(data) // Add data streams to stream list
                cursorPagination = pagination.cursor

                Log.i("STREAMS", streams.toString())
                Log.i("CURSOR", pagination.toString())
                Log.i("ITEM-COUNT-NEW", "${streamListAdapter.itemCount}")

                // Loading Streams in RecyclerView
                runOnUiThread {
                    streamListAdapter.setStreams(streams)
                    swipeRefreshLayout.isRefreshing = false
                }

            } catch (e: ClientRequestException) {
                // Update tokens
//                getNewTokens()
                Log.i(TAG, "PAPAPAPAPAPPAPAPAPAPPAPAPAAPAPAPA")
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
            // Clear stream list, the adapter and Pagination Cursor to refresh
            streams.clear()
            streamListAdapter.setStreams(streams)
            cursorPagination = null
            getStreams()
        }
    }


    // Options Menu
    // Inflate Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    // On options item selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.item_profile -> {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Get new tokens
    private fun getNewTokens() {

        lifecycleScope.launch {
            var newTokens: OAuthTokensResponse? = null
            try {
                newTokens = loadNewTokens()
                Log.d(
                    TAG,
                    "-NEW TOKEN- is ${newTokens?.accessToken} and -NEW REFRESH TOKEN- is ${newTokens?.refreshToken}"
                )

                //Replace tokens
                saveTokens(newTokens)

            } catch (e: ClientRequestException) {
                Log.d(TAG, "Error getting new tokens")
            } finally {
                // Reload Streams or Return Login Ativity
                if (newTokens == null) {
                    // Clear Tokens and return Login page
                    clearTokens()
                    goToLaunchActivity()
                } else {
                    goToLaunchActivity()
                }
            }

        }

    }


    // Load new Tokens
    private suspend fun loadNewTokens(): OAuthTokensResponse? {
        return TwitchApiService(Network.createHttpClient(this)).getNewTokens(SessionManager(this).getRefreshToken())
    }

    // Clear tokens
    private fun clearTokens() {
        SessionManager(this).clearAccessToken() // Clear AccessToken
        SessionManager(this).clearRefreshToken() // Clear RefreshToken
    }

    //Replace tokens
    private fun saveTokens(tokens: OAuthTokensResponse?) {
        SessionManager(this).saveAccessToken(tokens?.accessToken.toString())
        SessionManager(this).saveRefreshToken(tokens?.refreshToken.toString())
    }

    private fun goToLaunchActivity() {
        runOnUiThread { // Return to login activity
            val intent = Intent(this, LaunchActivity::class.java)
            this.startActivity(intent)
            finish()
        }
    }


}