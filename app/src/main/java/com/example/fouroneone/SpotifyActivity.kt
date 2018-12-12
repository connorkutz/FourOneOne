package com.example.fouroneone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse


class SpotifyActivity : AppCompatActivity() {


    // starts activity and starts SpotifyRemoteApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
        SpotifyManager.init(this)

        val scopes = emptyArray<String>()
        val authRequest = AuthenticationRequest.Builder(getString(R.string.spotify_key), AuthenticationResponse.Type.TOKEN, "com.example.fouroneone://callback").setShowDialog(true).setScopes(scopes).build()
        AuthenticationClient.openLoginActivity(this, 0xa, authRequest)




    }

    override fun onStart() {
        super.onStart()


    }

    private fun spotifyReady(){
        SpotifyManager.getFeaturedPlaylists(callback = {pager ->
            Log.d("First Playlist", pager.items[2].name.toString())
        })

        //update recyclerView with Pager of Playlists
        //onClick -> playPlaylist

    }




    override fun onStop() {
        super.onStop()
        SpotifyManager.destroy()
    }


    // initializes the webApi version of spotify to get user's playlists
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)
        println(response.error)
        if (0xa == requestCode) {
            val accessToken = response.accessToken
            SpotifyManager.initWeb(accessToken, callback = {
                spotifyReady()
            })
        }
    }
}
