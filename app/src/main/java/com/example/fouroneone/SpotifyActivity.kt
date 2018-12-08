package com.example.fouroneone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector

import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track




class SpotifyActivity : AppCompatActivity() {

    //private val CLIENT_ID = this.getString(R.string.spotify_key)
   // private val REDIRECT_URI = "com.yourdomain.yourapp://callback"
    //private val mSpotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var CLIENT_ID : String
    private  var mSpotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var REDIRECT_URI : String

    override fun onCreate(savedInstanceState: Bundle?) {
        CLIENT_ID = this.getString(R.string.spotify_key)
        REDIRECT_URI = "com.example.fouroneone://callback"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()


        SpotifyAppRemote.connect(this, connectionParams,
                object : Connector.ConnectionListener {

                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote
                        Log.d("SpotifyActivity", "Connected! Yay!")

                        // Now you can start interacting with App Remote
                        connected()
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("SpotifyActivity", throwable.message, throwable)

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                })
    }

    private fun connected() {
        mSpotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        mSpotifyAppRemote!!.playerApi
                .subscribeToPlayerState()
                .setEventCallback { playerState ->
                    val track = playerState.track
                    if (track != null) {
                        Log.d("SpotifyActivity", track.name + " by " + track.artist.name)
                    }
                }
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }
}
