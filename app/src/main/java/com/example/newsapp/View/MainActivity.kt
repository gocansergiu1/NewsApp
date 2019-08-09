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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Adapter.ArticlesAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import com.example.newsapp.R
import com.example.newsapp.Repository.ApiClient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
    }

    private lateinit var mAdapter: ArticlesAdapter

    private lateinit var loadingSpinner: View

    private lateinit var articlesRecycleView: RecyclerView

    private var myCompositeDisposable: CompositeDisposable? = null
    private lateinit var database: DatabaseReference

    val firebaseUrl="https://newsapp-de8c7.firebaseio.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadingSpinner = findViewById(R.id.loading_spinner)

        myCompositeDisposable = CompositeDisposable()
        var searchrx  = findViewById<EditText>(R.id.searchrx)

        articlesRecycleView = findViewById(R.id.list)

        mAdapter = ArticlesAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        articlesRecycleView.layoutManager = mLayoutManager

       articlesRecycleView.adapter = AlphaInAnimationAdapter(mAdapter)


        database = FirebaseDatabase.getInstance().reference

        //ref:Firebase = new Firebase(Config.fire)
        // EditText
        val textObs = Observable.create<String> {

            val listener = object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {
                    if (text != null) {
                        it.onNext(text.toString())
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            }

            searchrx.addTextChangedListener(listener)

            it.setCancellable {
                searchrx.removeTextChangedListener(listener)
            }

        }

        textObs.switchMap {
            ApiClient.client.getArticlesRX(it)
                .subscribeOn(Schedulers.io()) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
               mAdapter.articlesList=it.response.results
                mAdapter.notifyDataSetChanged()
            }

        myCompositeDisposable?.add(ApiClient.client.getArticlesRX()
            .observeOn(AndroidSchedulers.mainThread()) // sends observable notif to android main ui thread
                //ready to display data in our app UI
            .subscribeOn(Schedulers.io()).subscribe{ //with Schedulers we create an alternative thread io,to go out of UI Thread
                //here the observable should execute
                mAdapter.articlesList = it.response.results
                mAdapter.notifyDataSetChanged()
                loadingSpinner.visibility=View.GONE
              })

        // Obtain a reference to the SharedPreferences file for this app
        val prefs: SharedPreferences = getSharedPreferences("filters", 0)

        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this)
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