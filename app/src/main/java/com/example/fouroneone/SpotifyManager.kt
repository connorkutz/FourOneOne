package com.example.fouroneone

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.SpotifyCallback
import retrofit.client.Response


object SpotifyManager {

    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private var spotify : SpotifyService? = null


    fun init(context: Context) {

        val CLIENT_ID  = context.getString(R.string.spotify_key)
        val REDIRECT_URI = "com.example.fouroneone://callback"
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()

        SpotifyAppRemote.connect(context, connectionParams,
                object : Connector.ConnectionListener {

                    override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote
                        //Log.d("SpotifyManager", "Connected! Yay!")
                        // Now you can start interacting with App Remote
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("SpotifyManager", throwable.message, throwable)

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                })
    }

    fun initWeb(accessToken: String, callback : (SpotifyManager) -> Unit){
        val api = SpotifyApi()
                .setAccessToken(accessToken)
        spotify = api.service
        //Log.d("SpotifyManager", "spotify web api initialized")
        callback(this)

        //getFeaturedPlaylists()
    }


    fun getFeaturedPlaylists(callback: (Pager<PlaylistSimple>) -> Unit){
        spotify!!.getMyPlaylists(object : SpotifyCallback<Pager<PlaylistSimple>>(){
            override fun success(playlistPager : Pager<PlaylistSimple>, response: Response){
                //Log.d("Playlists", playlistPager.items[1].name.toString())
                callback(playlistPager)
            }

            override fun failure(error: SpotifyError){
                Log.d("SpotifyManager", error.toString())
        }})
    }


    fun playPlaylist(uri: String) {
        mSpotifyAppRemote!!.playerApi.play(uri)
    }

    fun destroy(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }



}