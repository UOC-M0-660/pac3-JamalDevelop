package edu.uoc.pac3.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

@Serializable
data class User(
    @SerialName("userName") val userName: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("data") val data: List<User>? = null,
)