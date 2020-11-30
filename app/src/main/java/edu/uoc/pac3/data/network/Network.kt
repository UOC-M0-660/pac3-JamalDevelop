package edu.uoc.pac3.data.network

import android.content.Context
import android.util.Log
import edu.uoc.pac3.data.SessionManager
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * Created by alex on 07/09/2020.
 */
object Network {

    private const val TAG = "Network"

    fun createHttpClient(context: Context): HttpClient {
        return HttpClient(OkHttp) {

            // TODO: Setup HttpClient
            // Json
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }
            // Apply to All Requests
            defaultRequest {
                Log.i("Authorization", "Bearer ${SessionManager(context).getAccessToken()}")
//                parameter("Authorization", "Bearer ${SessionManager(context).getAccessToken()}")

                // Content Type
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                header("Authorization", "Bearer ${SessionManager(context).getAccessToken()}")
                accept(ContentType.Application.Json)
            }
            // Optional OkHttp Interceptors
//            engine {
////                addInterceptor(CurlInterceptor(Loggable { Log.v("Curl", it) }))
//            }
        }
    }


    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

}