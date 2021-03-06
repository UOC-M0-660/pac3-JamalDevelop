package edu.uoc.pac3.data

import android.util.Log
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.request.*

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"


    /// Gets Access and Refresh Tokens on Twitch
    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {

        var response: OAuthTokensResponse? = null

        try {
            response = httpClient.post<OAuthTokensResponse>(Endpoints.tokens) {
                parameter("client_id", OAuthConstants.CLIENT_ID)
                parameter("client_secret", OAuthConstants.CLIENT_SECRET)
                parameter("code", authorizationCode)
                parameter("grant_type", "authorization_code")
                parameter("redirect_uri", OAuthConstants.REDIRECT_URI)
            }

            Log.d(
                TAG,
                "Access Token: ${response.accessToken}. Refresh Token: ${response.refreshToken}"
            )
            return response
        } catch (e: ClientRequestException) {
            Log.e(TAG, "getTokens(authorizationCode: String) from Twitch Unauthorized - 401")
            Log.e(TAG, e.toString())
            return response
        }

    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): StreamsResponse? {

        //TODO("Get Streams from Twitch")
        var response: StreamsResponse? = null

        // TODO("Support Pagination")
        try {
            if (cursor.isNullOrEmpty()) {
                response = httpClient.get<StreamsResponse>(Endpoints.streams) {
                    headers {
                        append("Client-Id", OAuthConstants.CLIENT_ID)
                    }
                    parameter("first", OAuthConstants.FIRST)
                }
            } else {
                response = httpClient.get<StreamsResponse>(Endpoints.streams) {
                    headers {
                        append("Client-Id", OAuthConstants.CLIENT_ID)
                    }
                    parameter("first", OAuthConstants.FIRST)
                    parameter("after", cursor)
                }
            }
        } catch (e: ClientRequestException) {
            Log.e(TAG, "getStreams() from Twitch Unauthorized - 401")
            Log.e(TAG, e.toString())
        }

        Log.d(TAG, "getStreams() from Twitch")

        // TODO("Get Streams from Twitch")
        return response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): User? {
//        TODO("Get User from Twitch")
        var response: User? = null
        try {
            response = httpClient.get<User>(Endpoints.users) {
                headers {
                    append("Client-Id", OAuthConstants.CLIENT_ID)
                }
                parameter("scopes", OAuthConstants.SCOPES)
            }

            Log.d(TAG, "getUser() from Twitch")

            return response
        } catch (e: ClientRequestException) {
            Log.e(TAG, "getUser() from Twitch Unauthorized - 401")
            Log.e(TAG, e.toString())
            return response
        }

    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
//        TODO("Update User Description on Twitch")
        var response: User? = null
        try {
            val response = httpClient.put<User>(Endpoints.users) {
                headers {
                    append("Client-Id", OAuthConstants.CLIENT_ID)
                }
                parameter("scopes", OAuthConstants.SCOPES)
                parameter("description", description)
            }

            Log.d(TAG, OAuthConstants.UPDATE_SUCCESS)

            return response
        } catch (e: ClientRequestException) {
            Log.e(TAG, "updateUserDescription() from Twitch Unauthorized - 401")
            Log.e(TAG, e.toString())
            return response
        }

    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun logoutUser(accessToken: String?): OAuthTokensResponse? {
//        TODO("Update User Description on Twitch")
        val response = httpClient.post<OAuthTokensResponse>(Endpoints.revoke) {
            parameter("client_id", OAuthConstants.CLIENT_ID)
            parameter("token", accessToken)
        }

        Log.d(TAG, "User logout successfully!")

        return response
    }


    /// Gets new Access and Refresh Tokens on Twitch by RefreshToken
    suspend fun getNewTokens(refreshToken: String?): OAuthTokensResponse? {
        var response: OAuthTokensResponse? = null
        try {
            response = httpClient.post<OAuthTokensResponse>(Endpoints.tokens) {
                parameter("client_id", OAuthConstants.CLIENT_ID)
                parameter("client_secret", OAuthConstants.CLIENT_SECRET)
                parameter("refresh_token", refreshToken)
                parameter("grant_type", "refresh_token")
            }

            Log.d(
                TAG,
                "Access Token new: ${response.accessToken}. Refresh Token new: ${response.refreshToken}"
            )
            return response
        } catch (e: ClientRequestException) {
            Log.e(TAG, "getNewTokens(refreshToken: String?) from Twitch Unauthorized - 401")
            Log.e(TAG, e.toString())
            return response
        }

    }


}