package edu.uoc.pac3.data.oauth

/**
 * Created by alex on 07/09/2020.
 */
object OAuthConstants {

    // TODO: Set OAuth2 Variables
    const val CLIENT_ID = "8i31vi7gx0av33gnwnwtxzopj1b4vg"
    const val REDIRECT_URI = "http://localhost"
    const val CLIENT_SECRET = "3ajm9wqd6w9p6v2t79bf8f0r1k15m9"
    const val RESPONSE_TYPE = "code"
    const val SCOPES = "user:edit user:read:email bits:read channel:read:hype_train analytics:read:games"

    // Tokens Variables
    const val ACCESS_TOKEN_PREFERENCES = "accessToken"
    const val REFRESH_TOKEN_PREFERENCES = "refreshToken"

    // Get Streams Variables
    const val FIRST = 5.toString()

    // Profile Variables
    const val UPDATE_SUCCESS = "Updated description successfully!"
    const val UPDATE_FAILED = "Update description failed!"




}