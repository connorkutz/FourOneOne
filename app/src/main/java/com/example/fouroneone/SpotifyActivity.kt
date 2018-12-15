package com.example.fouroneone

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.squareup.picasso.Picasso
import kaaes.spotify.webapi.android.models.Playlist
import kotlinx.android.synthetic.main.playlist_recycler_view_item.view.*


class SpotifyActivity : AppCompatActivity() {


    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var playlistViewAdapter: PlaylistViewAdapter
    private lateinit var playlistViewManager: RecyclerView.LayoutManager
    private lateinit var spotifyButton: ImageButton
    private lateinit var rewButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var ffButton: ImageButton
    private lateinit var picasso: Picasso
    private lateinit var animation: AnimationDrawable


    // starts activity and starts SpotifyRemoteApp
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
        picasso = Picasso.get()
        SpotifyManager.init(this)

        val scopes = emptyArray<String>()
        val authRequest = AuthenticationRequest.Builder(getString(R.string.spotify_key), AuthenticationResponse.Type.TOKEN, "com.example.fouroneone://callback").setShowDialog(true).setScopes(scopes).build()
        AuthenticationClient.openLoginActivity(this, 0xa, authRequest)

        val container = findViewById<ConstraintLayout>(R.id.spotify_container)
        animation = container.background as AnimationDrawable
        animation.setEnterFadeDuration(4000)
        animation.setExitFadeDuration(8000)

        playlistRecyclerView = findViewById(R.id.playlist_recycler_view)
        spotifyButton = findViewById(R.id.spotify_button)
        rewButton = findViewById(R.id.rew_button)
        playButton = findViewById(R.id.play_button)
        ffButton = findViewById(R.id.ff_button)

        spotifyButton.setOnClickListener {
            val intent = this.packageManager.getLaunchIntentForPackage("com.spotify.music")
            startActivity(intent)
        }
        rewButton.setOnClickListener { SpotifyManager.rewind() }
        ffButton.setOnClickListener { SpotifyManager.fastForward() }
        playButton.setOnClickListener { SpotifyManager.togglePlay() }


    }

    private fun spotifyReady() {
        SpotifyManager.getFeaturedPlaylists(callback = { pager ->
            //Log.d("Image Height", pager.items[2].images[0].height.toString())
            runOnUiThread { fillRecyclerView(pager) }
        })

        runOnUiThread {
            playButton.setOnClickListener {
                SpotifyManager.togglePlay()
                SpotifyManager.isPaused { isPaused ->
                    if (isPaused) {
                        playButton.setImageDrawable(getDrawable(android.R.drawable.ic_media_play))
                    } else {
                        playButton.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause))
                    }
                }
            }
        }


    }

    private fun fillRecyclerView(playlists : MutableList<Playlist>) {
        playlistViewManager = LinearLayoutManager(this)
        playlistViewAdapter = PlaylistViewAdapter(playlists)
        playlistRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            //setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = playlistViewManager

            adapter = playlistViewAdapter
        }

    }

    inner class PlaylistViewAdapter(private val dataset: List<Playlist>?) : RecyclerView.Adapter<PlaylistViewAdapter.PlaylistViewHolder>() {
        inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): SpotifyActivity.PlaylistViewAdapter.PlaylistViewHolder {
            // create a new view
            val playlistView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.playlist_recycler_view_item, parent, false) as View
            // set the view's size, margins, paddings and layout parameters

            return PlaylistViewHolder(playlistView)
        }

        override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
            holder.itemView.playlist_title.text = (dataset!![position].name)
            val tracks = dataset[position].tracks
            val url = tracks.items[0].track.album.images[0].url
                picasso.load(url)
                        //.resize(500, 275)
                        //.centerInside()
                        //.onlyScaleDown()
                        .into(holder.itemView.playlist_thumbnail)
            holder.itemView.setOnClickListener {
                SpotifyManager.playPlaylist(dataset[position].uri)
            }
        }

        override fun getItemCount() = dataset!!.size
    }

    //private fun imageToBitmap(image: Image){
    //    image.
    //}




    override fun onStop() {
        super.onStop()
        SpotifyManager.destroy()
    }
    override fun onPause() {
        super.onPause()
        if(animation.isRunning){
            animation.stop()
        }
    }
    override fun onResume() {
        super.onResume()
        if(!animation.isRunning){
            animation.start()
        }
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
