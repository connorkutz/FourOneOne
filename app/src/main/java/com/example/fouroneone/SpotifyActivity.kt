package com.example.fouroneone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector

import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track




class SpotifyActivity : AppCompatActivity() {

    private val CLIENT_ID = this.getString(R.string.spotify_key)
   // private val REDIRECT_URI = "com.yourdomain.yourapp://callback"
    private val mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                //.setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()
    }

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
    }

    private fun connected() {
        // Then we will write some more code here.
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}
