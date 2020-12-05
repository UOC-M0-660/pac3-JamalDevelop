package edu.uoc.pac3.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by alex on 07/09/2020.
 */

//@Serializable
//data class User(
//    @SerialName("userName") val userName: String? = null,
//    @SerialName("description") val description: String? = null,
//    @SerialName("data") val data: List<User>? = null,
//)
@Serializable
data class User(
    @SerialName("data") val data: List<UserModel>? = null,
)


@Serializable
data class UserModel(
    @SerialName("id") val id: String? = null,
    @SerialName("login") val loginUser: String? = null,
    @SerialName("display_name") val display_name: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("broadcaster_type") val broadcaster_type: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("profile_image_url") val profile_image_url: String? = null,
    @SerialName("offline_image_url") val offline_image_url: String? = null,
    @SerialName("view_count") val view_count: String? = null,
    @SerialName("email") val email: String? = null,
)