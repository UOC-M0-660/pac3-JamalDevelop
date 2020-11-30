package edu.uoc.pac3.data

import android.R
import android.content.Context
import android.util.Log
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.oauth.OAuthActivity
import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.coroutines.coroutineContext

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"


    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {

        val response = httpClient.post<OAuthTokensResponse>(Endpoints.tokens) {
            parameter("client_id", OAuthConstants.CLIENT_ID)
            parameter("client_secret", OAuthConstants.CLIENT_SECRET)
            parameter("code", authorizationCode)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", OAuthConstants.REDIRECT_URI)
        }

        Log.d(TAG, "Access Token: ${response.accessToken}. Refresh Token: ${response.refreshToken}")
        return response
    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
//        TODO("Get Streams from Twitch")
        val response = httpClient.get<StreamsResponse>(Endpoints.streams) {
            headers{
                append("Client-Id", OAuthConstants.CLIENT_ID)
            }
        }
//        TODO("Support Pagination")

        return response
//        // TODO("Get Streams from Twitch")
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
//        TODO("Get User from Twitch")
        val response = httpClient.get<User>(Endpoints.users) {
            headers {
                append("Client_Id", OAuthConstants.CLIENT_ID)
            }
        }

        return  response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        TODO("Update User Description on Twitch")
    }
}