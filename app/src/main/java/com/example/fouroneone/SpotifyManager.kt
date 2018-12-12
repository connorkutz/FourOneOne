package com.example.fouroneone

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import com.google.android.gms.common.Feature
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.ContentApi
import com.spotify.android.appremote.api.SpotifyAppRemote
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.models.FeaturedPlaylists
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.Playlist
import kaaes.spotify.webapi.android.models.PlaylistSimple
import javax.security.auth.callback.Callback
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.models.SavedTrack
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
                        Log.d("SpotifyManager", "Connected! Yay!")
                        // Now you can start interacting with App Remote
                    }

                    override fun onFailure(throwable: Throwable) {
                        Log.e("SpotifyManager", throwable.message, throwable)

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                })
    }

    fun initWeb(accessToken: String){
        var api = SpotifyApi()
                .setAccessToken(accessToken)
        spotify = api.service
        Log.d("SpotifyManager", "spotify web api initialized")
    }


    fun getFeaturedPlaylists(){
        var list = listOf<Playlist>()
        //var playlists: FeaturedPlaylists
        spotify!!.getMyPlaylists(object : SpotifyCallback<Pager<PlaylistSimple>>(){
            override fun success(playlistPager : Pager<PlaylistSimple>, response: Response){
                Log.d("SpotifyManager", playlistPager.items[0].name.toString())
            }

            override fun failure(error: SpotifyError){
                Log.d("SpotifyManager", error.toString())
        }})

    }


    private fun playPlaylist() {
        mSpotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        //mSpotifyAppRemote!!.playerApi
        // .subscribeToPlayerState()
        // .setEventCallback { playerState ->
        //val track = playerState.track
        //if (track != null) {
        //   Log.d("SpotifyActivity", track.name + " by " + track.artist.name)
        //}
        // }


    }

    fun destroy(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }



}