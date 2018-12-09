package com.example.fouroneone

import android.graphics.drawable.AnimationDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Adapter
import android.widget.Toast
import kotlinx.android.synthetic.main.article_recycler_view_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Thread.sleep

class NewsActivity : AppCompatActivity() {

    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var articleViewAdapter: ArticleViewAdapter
    private lateinit var articleViewManager: RecyclerView.LayoutManager
    private lateinit var animation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val context = this
        var articles : MutableList<Article>? = arrayListOf()

        val container = findViewById<ConstraintLayout>(R.id.container)
        animation = container.background as AnimationDrawable
        animation.setEnterFadeDuration(4000)
        animation.setExitFadeDuration(8000)

        doAsync {
            NewsManager().getNews(
                    successCallback = { result ->
                        runOnUiThread {
                            //quoteText.text = quote
                            Log.d("Articles", result.size.toString())
                        }
                        articles = result
                    },
                    errorCallback = { exception ->
                        runOnUiThread {
                            Log.d("NewsActivity", exception.message)
                        }
                    },
                    context = context
            )
        }
        sleep(1000)
        if(articles!!.isEmpty()){
            Toast.makeText(this, "Failed to fetch news", Toast.LENGTH_SHORT).show()
        }
        else{
            articleViewManager = LinearLayoutManager(this)
            articleViewAdapter = ArticleViewAdapter(articles)
            articleRecyclerView = findViewById<RecyclerView>(R.id.article_recycler_view).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = articleViewManager

                adapter = articleViewAdapter
            }

        }
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

    class ArticleViewAdapter(private val dataset: List<Article>?) : RecyclerView.Adapter<ArticleViewAdapter.ArticleViewHolder>(){
        class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ArticleViewAdapter.ArticleViewHolder {
            // create a new view
            val articleView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.article_recycler_view_item, parent, false) as View
            // set the view's size, margins, paddings and layout parameters

            return ArticleViewHolder(articleView)
        }

        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            //load data into a new row
            //holder.itemView.alert_image.setImageDrawable(ResourcesCompat.getDrawable(R.drawable.ic_warning_black_24dp))
            val lineString = "Line: "
            holder.itemView.article_title.setText(dataset!!.get(position).Title)
            //holder.itemView.source.setText(dataset.get(position).source)
        }


        // Return the size of your dataset (invoked by the layout manager)
        //no dataset yet, need to hit metro API
        override fun getItemCount() = dataset!!.size
    }
}
