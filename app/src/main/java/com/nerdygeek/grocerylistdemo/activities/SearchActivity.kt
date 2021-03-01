package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.SearchItemAdapter
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.models.ProductResponse
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*

class SearchActivity : AppCompatActivity() {

    lateinit var dbHelper: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init()


    }


    private fun setUpToolBar() {
        var toolBar = tool_bar
        toolBar.title = "Search List"
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        var item = menu.findItem(R.id.menu_cart)
        MenuItemCompat.setActionView(item, R.layout.menu_cart_layout)
        var view = MenuItemCompat.getActionView(item)
        var textCartCount = view.text_cart_count
        updateCartCount(textCartCount)
        view.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))

        }

        return true
    }

    private fun updateCartCount(textCartCount: TextView) {

        var count = dbHelper.getCartItemCount()
        Log.d("gld", count.toString())
        if (count >= 1) {
            textCartCount.visibility = View.VISIBLE
            textCartCount.text = "$count"
        } else {
            textCartCount?.visibility = View.GONE
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_profile -> Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            R.id.menu_setting -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()

        }

        return true
    }

    private fun init() {
        var searchItemAdapter = SearchItemAdapter(this)
        dbHelper = DBhelper(this)
        setUpToolBar()
        generateData(searchItemAdapter)
        search_recycler_view.adapter = searchItemAdapter
        search_recycler_view.layoutManager = LinearLayoutManager(this)

        search_bar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                searchItemAdapter.filter.filter(newQuery)
                return false
            }

        })

    }

    private fun generateData(searchItemAdapter: SearchItemAdapter) {
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getAllProducts(),
            Response.Listener {
                var productResponse = Gson().fromJson(it, ProductResponse::class.java)
                searchItemAdapter.setData(productResponse.data)
                Toast.makeText(applicationContext, "data loaded", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "failed to load data", Toast.LENGTH_SHORT).show()
            }

        )

        requestQueue.add(request)
    }
}