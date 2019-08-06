package com.example.newsapp.View

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Models.Article
import com.example.newsapp.Adapter.ArticlesAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

import com.example.newsapp.R
import com.example.newsapp.Repository.ApiClient
import retrofit2.Call
import retrofit2.Response
import com.example.newsapp.Repository.baseJsonResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity(),
    //android.app.LoaderManager.LoaderCallbacks<List<Article>>,
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {

    }

    /** Adapter for the list of articles */
    private lateinit var mAdapter: ArticlesAdapter

    private lateinit var loadingSpinner: View

    private lateinit var articlesRecycleView: RecyclerView

    private var myCompositeDisposable: CompositeDisposable? = null


   // private var articlesToShow: Int = 20
   // val service = RetrofitInstance.retrofitInstance.create(ArticlesWebService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingSpinner = findViewById(R.id.loading_spinner)

        val articles: LiveData<MutableList<Article>> = getData()

        myCompositeDisposable = CompositeDisposable()

        var searchrx  = findViewById<EditText>(R.id.searchrx)
//        val mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
//        var editor = mPreferences.edit()

        articlesRecycleView = findViewById(R.id.list)
        
            mAdapter = ArticlesAdapter(articles.value!!)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        articlesRecycleView.layoutManager = mLayoutManager

       articlesRecycleView.adapter = AlphaInAnimationAdapter(mAdapter)


        // EditText pentru RX
        searchrx.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                myCompositeDisposable?.add(ApiClient.client.getArticlesRX(queue=p0.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe{
                        mAdapter.articlesList = it.response.results
                        mAdapter.notifyDataSetChanged() })

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        myCompositeDisposable?.add(ApiClient.client.getArticlesRX()
            .observeOn(AndroidSchedulers.mainThread()) // sends observable notif to android main ui thread
                //ready to display data in our app UI
            .subscribeOn(Schedulers.io()).subscribe{ //with Schedulers we create an alternative thread io,to go out of UI Thread
                //here the observable should execute
                mAdapter.articlesList = it.response.results
                mAdapter.notifyDataSetChanged()})


        //data has changed , it will put articles
       /* articles.observe(this, Observer {
            mAdapter.articlesList = it
            mAdapter.notifyDataSetChanged()

        })*/

        // Obtain a reference to the SharedPreferences file for this app
        val prefs: SharedPreferences = getSharedPreferences("filters", 0)

        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    private fun getData(): LiveData<MutableList<Article>> {
        val articles = MutableLiveData<MutableList<Article>>()

        articles.value = mutableListOf()
        val call: Call<baseJsonResponse> = ApiClient.client.getArticles()
        call.enqueue(object : retrofit2.Callback<baseJsonResponse> {

            override fun onResponse(call: Call<baseJsonResponse>, response: Response<baseJsonResponse>) {

                var response: baseJsonResponse= response.body()!!

                articles.value = response.response.results

                loadingSpinner.visibility=View.GONE
                //articlesRecycleView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<baseJsonResponse>, t: Throwable?) {
                articles.value = mutableListOf()
            }

        })

        return articles
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.action_settings) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
