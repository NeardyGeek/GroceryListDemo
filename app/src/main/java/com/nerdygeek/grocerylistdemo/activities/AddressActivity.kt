package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.AddressRowAdapter
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.models.AddressResponse
import com.nerdygeek.grocerylistdemo.models.User
import com.nerdygeek.grocerylistdemo.models.UserResponse
import com.nerdygeek.registerlogindemo.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.lang.reflect.Method

class AddressActivity : AppCompatActivity() {

    lateinit var dbHelper: DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        init()
    }


    private fun setUpToolBar() {
        var toolBar = tool_bar
        toolBar.title = "choose shipping address"
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
        dbHelper = DBhelper(this)
        setUpToolBar()
        var addressRowAdapter = AddressRowAdapter(this)
        generateData(addressRowAdapter)

        address_recycler_view.adapter = addressRowAdapter
        address_recycler_view.layoutManager = LinearLayoutManager(this)

        button_add_address.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }



    }

    private fun generateData(addressRowAdapter: AddressRowAdapter){

        var user = Gson().fromJson(SessionManager(this).getUser(), UserResponse::class.java).user
        var requestQueue = Volley.newRequestQueue(this)
        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getAddress(user._id),
            Response.Listener {
                var gson = Gson()
                var addressResponse = gson.fromJson(it, AddressResponse::class.java)
                addressRowAdapter.setData(addressResponse.data)

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
            }

        )

        requestQueue.add(request)

    }
}