package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.OrderDetailAdapter
import com.nerdygeek.grocerylistdemo.apps.Config
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.models.OrderProduct
import com.nerdygeek.grocerylistdemo.models.OrderResponse
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.tool_bar.*

class OrderDetailsActivity : AppCompatActivity() {

    lateinit var dbHelper: DBhelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        init()
    }


    private fun setUpToolBar() {
        var toolBar = tool_bar
        toolBar.title = "My Orders"
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

        var orderResponse = intent.getSerializableExtra(OrderResponse.KEY_ORDER) as OrderResponse

        order_detail_recycler_view.adapter = OrderDetailAdapter(this, orderResponse.products)

        order_detail_recycler_view.layoutManager = LinearLayoutManager(this)
    }
}