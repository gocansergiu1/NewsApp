package com.example.newsapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.Models.Article
import com.example.newsapp.Misc.DateChanger

class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(),
    View.OnClickListener {

    companion object {
        var sharedPreferenceAnimation: Boolean? = null

    }

    var articlesList: List<Article> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var itemView: View

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titleTextView: TextView
        var sectionTextView: TextView
        var authorTextView: TextView
        var dateTextView: TextView
        var thumbnailImageView: ImageView

        init {
            titleTextView = v.findViewById(R.id.title)
            sectionTextView = v.findViewById(R.id.section)
            authorTextView = v.findViewById(R.id.author)
            dateTextView = v.findViewById(R.id.date)
            thumbnailImageView = v.findViewById(R.id.thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.article, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    override fun onClick(p0: View) {
        val context: Context = itemView.context
        // Convert the String URL into a URI object (to pass into the Intent constructor)
        var articleUri: Uri? = null
        var articol = articlesList[1].webUrl
        articleUri = Uri.parse(articol)


        // Create a new intent to view the news story URI
        val websiteIntent = Intent(Intent.ACTION_VIEW, articleUri)

        // Send the intent to launch a new activity
        context.startActivity(websiteIntent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article = articlesList[position]
        val data = DateChanger()

        // Display the picture of the current article in that TextView
        Glide.with(itemView).load(article.fields?.thumbnail).into(holder.thumbnailImageView)
        // Display the title of the current article in that TextView
        holder.titleTextView.text = article.webTitle
        //Display the section of the current article in that TextView
        holder.sectionTextView.text = article.sectionName
        //Display the author of the current article in that TextView
        if (article.tags.size != 0)
            holder.authorTextView.text = article.tags[0].webTitle
        //Display the date of the current article in that TextView
        holder.dateTextView.text = data.getDate(article.webPublicationDate)
    }
}