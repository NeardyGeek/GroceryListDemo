package com.nerdygeek.grocerylistdemo.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.nerdygeek.grocerylistdemo.R
import com.nerdygeek.grocerylistdemo.adapters.CategoryAdapter
import com.nerdygeek.grocerylistdemo.apps.Endpoint
import com.nerdygeek.grocerylistdemo.helper.DBhelper
import com.nerdygeek.grocerylistdemo.models.Category
import com.nerdygeek.grocerylistdemo.models.CategoryResponse
import com.nerdygeek.grocerylistdemo.models.UserResponse
import com.nerdygeek.registerlogindemo.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.menu_cart_layout.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.tool_bar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var mList = ArrayList<Category>()
    var categoryAdapter = CategoryAdapter(this)


    private lateinit var sessionManager: SessionManager
    private lateinit var dbHelper: DBhelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onRestart() {
        super.onRestart()
        init()
    }

    private fun setUpToolBar() {
        var toolBar = tool_bar
        toolBar.title = "home"
        setSupportActionBar(toolBar)

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

            R.id.menu_profile -> Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            R.id.menu_setting -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()

        }

        return true
    }

    private fun init() {
        dbHelper = DBhelper(this)
        setUpToolBar()
        sessionManager = SessionManager(this)
        var user = Gson().fromJson(sessionManager.getUser(), UserResponse::class.java).user


        var headerView = nav_view.getHeaderView(0)

        headerView.header_name.text = user.firstName
        headerView.header_email.text = user.email

        nav_view.setNavigationItemSelectedListener(this)

        var toggle = ActionBarDrawerToggle(
            this, drawer_layout, tool_bar, 0, 0
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()



        getCategoryResponse()
        main_recycler_view.adapter = categoryAdapter
        main_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



        fab.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }


    }

    private fun logout() {
        sessionManager.logOut()
        dbHelper.clearTable()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun getCategoryResponse() {


        var requestQueue = Volley.newRequestQueue(this)

        var request = StringRequest(
            Request.Method.GET,
            Endpoint.getCategory(),
            Response.Listener {
                main_progress_bar.visibility = View.GONE
                var gson = Gson()
                var categoryResponse = gson.fromJson(it, CategoryResponse::class.java)
                categoryAdapter.setData(categoryResponse.data)

            },
            Response.ErrorListener {
                main_progress_bar.visibility = View.GONE

            }
        )

        requestQueue.add(request)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_account -> startActivity(Intent(this, AccountActivity::class.java))
            R.id.item_order -> startActivity(Intent(this, OrdersActivity::class.java))
            R.id.item_logout -> dialogueLogout()
            R.id.item_help -> Toast.makeText(applicationContext, "feature working in progress", Toast.LENGTH_SHORT).show()
            R.id.item_rate_app -> Toast.makeText(applicationContext, "feature working in progress", Toast.LENGTH_SHORT).show()
            R.id.item_refer -> Toast.makeText(applicationContext, "feature working in progress", Toast.LENGTH_SHORT).show()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun dialogueLogout() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton(
            "Yes"
        ) { dialog, p1 ->
            logout()
            finish()
        }

        builder.setNegativeButton("No") { dialog, p1 ->

            dialog.dismiss()

        }

        builder.create().show()

    }
}