package edu.uoc.pac3.twitch.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.user.User
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    private lateinit var userDescriptionEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get user
        getUser()

        // Init Update Description Button
        initUpdateDescriptionButton()

        // Init Logout Button
        initLogoutButton()


    }


    // Get data user
    private fun getUser() {

        userDescriptionEditText = findViewById(R.id.userDescriptionEditText)

        // Run in background
        GlobalScope.launch {

            val response = loadUser()
            val data = response?.data?.get(0)

            // Run in foreground
            runOnUiThread {
                // Charge the profile image
                Glide.with(imageView.context)
                    .load(data?.profile_image_url)
                    .into(imageView)

                // Charge Views count
                viewsText.text = data?.view_count

                // Charge User Name
                userNameTextView.text = data?.display_name

                // Charge description
                userDescriptionEditText.setText(data?.description)
            }

        }

    }


    // Call to API of Twitch to return User
    private suspend fun loadUser(): User? {
        return TwitchApiService(Network.createHttpClient(this)).getUser()
    }


    // Initialization for the UPDATE DESCRIPTION Button
    private fun initUpdateDescriptionButton() {

        updateDescriptionButton.setOnClickListener {

            // Run in background
            GlobalScope.launch {

                try {
                    updateDescription() // Update description
                    Log.d(TAG, OAuthConstants.UPDATE_SUCCESS)

                    runOnUiThread { // Run in foreground
                        Toast.makeText(
                            this@ProfileActivity,
                            OAuthConstants.UPDATE_SUCCESS,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.d(TAG, OAuthConstants.UPDATE_FAILED)
                    runOnUiThread { // Run in foreground
                        Toast.makeText(
                            this@ProfileActivity,
                            OAuthConstants.UPDATE_FAILED,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }

        }
    }

    // Call to API of Twitch to update description
    private suspend fun updateDescription(): User? {
        return TwitchApiService(Network.createHttpClient(this)).updateUserDescription(
            userDescriptionEditText.text.toString()
        )
    }


    // Initialization for the LOGOUT button
    private fun initLogoutButton() {

        logoutButton.setOnClickListener {
            GlobalScope.launch {

                val token = SessionManager(it.context).getAccessToken()
                logout(token)

                SessionManager(it.context).clearAccessToken()
                SessionManager(it.context).clearRefreshToken()


                runOnUiThread {
                    val intent = Intent(it.context, LaunchActivity::class.java)
                    it.context.startActivity(intent)
                }


            }
        }

    }

    private suspend fun logout(token: String?) {
        TwitchApiService(Network.createHttpClient(this)).logoutUser(token)
    }


}