package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 */
object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"
    // TODO: Add all remaining endpoints
    const val authorization = "$oauthBaseUrl/authorize"
    const val tokens = "$oauthBaseUrl/token"
    const val validate = "$oauthBaseUrl/validate"
    const val revoke = "$oauthBaseUrl/revoke"

    // Twitch API Endpoints
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"
    // TODO: Add all remaining endpoints
    const val streams = "$twitchBaseUrl/streams"
    const val users = "$twitchBaseUrl/users"


}