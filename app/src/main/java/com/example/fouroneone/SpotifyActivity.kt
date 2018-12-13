package com.example.fouroneone

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.squareup.picasso.Picasso
import kaaes.spotify.webapi.android.models.PlaylistSimple
import kotlinx.android.synthetic.main.article_recycler_view_item.view.*
import kotlinx.android.synthetic.main.playlist_recycler_view_item.view.*


class SpotifyActivity : AppCompatActivity() {
    var isReady = false


    private lateinit var playlistRecyclerView : RecyclerView
    private lateinit var playlistViewAdapter: PlaylistViewAdapter
    private lateinit var playlistViewManager: RecyclerView.LayoutManager
    private lateinit var spotifyButton: ImageButton
    private lateinit var rewButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var ffButton: ImageButton


    // starts activity and starts SpotifyRemoteApp
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)
        SpotifyManager.init(this)

        val scopes = emptyArray<String>()
        val authRequest = AuthenticationRequest.Builder(getString(R.string.spotify_key), AuthenticationResponse.Type.TOKEN, "com.example.fouroneone://callback").setShowDialog(true).setScopes(scopes).build()
        AuthenticationClient.openLoginActivity(this, 0xa, authRequest)

        playlistRecyclerView = findViewById(R.id.playlist_recycler_view)
        spotifyButton = findViewById(R.id.spotify_button)
        rewButton = findViewById(R.id.rew_button)
        playButton = findViewById(R.id.play_button)
        ffButton = findViewById(R.id.ff_button)




    }

    private fun spotifyReady(){
        SpotifyManager.getFeaturedPlaylists(callback = {pager ->
            Log.d("Image Height", pager.items[2].images[0].height.toString())
            fillRecyclerView(pager.items)
        })

        //update recyclerView with Pager of Playlists
        //onClick -> playPlaylist

    }

    private fun fillRecyclerView(playlists : MutableList<PlaylistSimple>) {
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

    inner class PlaylistViewAdapter(private val dataset: List<PlaylistSimple>?) : RecyclerView.Adapter<PlaylistViewAdapter.PlaylistViewHolder>() {
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
            //load data into a new row
            //holder.itemView.alert_image.setImageDrawable(ResourcesCompat.getDrawable(R.drawable.ic_warning_black_24dp))
            val lineString = "Line: "
            holder.itemView.playlist_title.text = (dataset!![position].name)
            //Picasso.get().setIndicatorsEnabled(true)
            //Picasso.get()
            //        .load(dataset!![position].)
            //        .resize(500, 275)
            //        .centerInside()
            //        .onlyScaleDown()
            //        .into(holder.itemView.article_thumbnail)
            //holder.itemView.playlist_thumbnail.setImageBitmap(dataset!![position].images[0])
            holder.itemView.setOnClickListener {
                SpotifyManager.playPlaylist(dataset[position].uri)
            }
            //holder.itemView.source.setText(dataset.get(position).source)
        }

        override fun getItemCount() = dataset!!.size
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
