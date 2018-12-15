package com.example.fouroneone

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyService
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.SpotifyCallback
import kaaes.spotify.webapi.android.models.*
import org.jetbrains.anko.doAsync
import retrofit.client.Response
import javax.security.auth.callback.Callback


object SpotifyManager {

    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private var spotify : SpotifyService? = null
    private lateinit var userId : String


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
        setUserId()
        callback(this)

        //getFeaturedPlaylists()
    }


    fun getFeaturedPlaylists(callback: (MutableList<Playlist>) -> Unit){
        spotify!!.getMyPlaylists(object : SpotifyCallback<Pager<PlaylistSimple>>(){
            override fun success(playlistPager : Pager<PlaylistSimple>, response: Response){
                //Log.d("Playlists", playlistPager.items[1].name.toString())
                val iterator = playlistPager.items.iterator()
                val playlists =  arrayListOf<Playlist>()
                doAsync {
                    while(iterator.hasNext()){
                        playlists.add(spotify!!.getPlaylist(userId, iterator.next().id))
                    }

                    callback(playlists)
                }

            }

            override fun failure(error: SpotifyError){
                Log.d("SpotifyManager", error.toString())
        }})
    }

    fun setUserId(){
        spotify!!.getMe(object : SpotifyCallback<UserPrivate>(){
            override fun success(user: UserPrivate?, response: Response?) {
                userId = user!!.id.toString()
            }

            override fun failure(error: SpotifyError?) {
                Log.d("SpotifyManager", error.toString())
            }
        })
    }


    fun playPlaylist(uri: String) {
        mSpotifyAppRemote!!.playerApi.play(uri)
    }

    fun isPaused(callback: (Boolean) -> Unit){
        mSpotifyAppRemote!!.playerApi.playerState.setResultCallback { playerState ->
            callback(playerState.isPaused)
        }

    }

    fun togglePlay(){
        mSpotifyAppRemote!!.playerApi.playerState.setResultCallback { playerState ->
            if (playerState.isPaused){

                mSpotifyAppRemote!!.playerApi.resume()
                /*
                spotify!!.getMySavedTracks(object : SpotifyCallback<Pager<SavedTrack>>(){
                    override fun success(pager: Pager<SavedTrack>?, response: Response?) {
                        mSpotifyAppRemote!!.playerApi.play(pager!!.items[0].track.uri)
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun failure(p0: SpotifyError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
                */
                }
            else{
                mSpotifyAppRemote!!.playerApi.pause()
            }
            }

    }

    fun fastForward(){
        mSpotifyAppRemote!!.playerApi.skipPrevious()
    }

    fun rewind(){
        mSpotifyAppRemote!!.playerApi.skipNext()
    }

    fun destroy(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }



}