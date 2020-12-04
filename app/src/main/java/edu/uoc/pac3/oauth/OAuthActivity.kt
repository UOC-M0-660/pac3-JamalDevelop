package edu.uoc.pac3.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.launch
import java.util.*

class OAuthActivity : AppCompatActivity() {

    private val TAG = "OAuthActivity"
    private var uniqueState: String = ""

    //    private val uniqueState = UUID.randomUUID().toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    // Prepare URL
    fun buildOAuthUri(): Uri {
        // TODO: Create URI
        uniqueState = UUID.randomUUID().toString()
        // Prepare URL
        val uri = Uri.parse(Endpoints.authorization)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.CLIENT_ID)
            .appendQueryParameter("redirect_uri", OAuthConstants.REDIRECT_URI)
            .appendQueryParameter("response_type", OAuthConstants.RESPONSE_TYPE)
            .appendQueryParameter("scope", OAuthConstants.SCOPES)
            .appendQueryParameter("state", uniqueState)
            .build()
        return uri
    }

    // Launch OAuth Authorization
    private fun launchOAuthAuthorization() {
        val uri = buildOAuthUri() //  Create URI

        // Set webView Redirect Listener
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(OAuthConstants.REDIRECT_URI)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { authorizationCode ->
                                // Got it!
                                Log.d("OAuth", "Here is the authorization code! $authorizationCode")
                                onAuthorizationCodeRetrieved(authorizationCode)
                            } ?: run {
                                // User cancelled the login flow
                                // TODO: Handle error
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }


        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private fun onAuthorizationCodeRetrieved(authorizationCode: String) {

        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE
        webView.visibility = View.INVISIBLE

        lifecycleScope.launch {
            val tokens =
                TwitchApiService(Network.createHttpClient(applicationContext)).getTokens(
                    authorizationCode
                )
            Log.d(
                "OAuth-TOKENS",
                "Here is the TOKENS! ${tokens?.accessToken} and ${tokens?.refreshToken}"
            )

            //save tokens
            SessionManager(this@OAuthActivity).saveAccessToken(tokens?.accessToken.toString())
            SessionManager(this@OAuthActivity).saveRefreshToken(tokens?.refreshToken.toString())


            runOnUiThread {
                val intent = Intent(this@OAuthActivity, StreamsActivity::class.java)
                startActivity(intent)
                finish()
            }

        }


        // TODO: Create Twitch Service

        // TODO: Get Tokens from Twitch

        // TODO: Save access token and refresh token using the SessionManager class
    }
}